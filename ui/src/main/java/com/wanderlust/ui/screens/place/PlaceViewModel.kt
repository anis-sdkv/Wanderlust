package com.wanderlust.ui.screens.place

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.usecases.AddPlaceCommentUseCase
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.GetPlaceUseCase
import com.wanderlust.ui.navigation.graphs.bottom_navigation.HomeNavScreen
import com.wanderlust.ui.utils.calculateRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

enum class PlaceCardState {
    PROGRESS_BAR, CONTENT, NOT_FOUND
}

@Immutable
data class PlaceState(
    val cardState: PlaceCardState = PlaceCardState.PROGRESS_BAR,
    val inputCommentText: String = "",
    val userPlaceRating: Int? = null,
    val currentUser: UserProfile? = null,
    val showLoadingDialog: Boolean = false,
    val showErrorsDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf(),

    val placeId: String = "",
    val lat: Double = 55.790278,
    val lon: Double = 49.13472200000001,
    val placeName: String = "",
    val placeDescription: String = "",
    val comments: PersistentList<Comment> = persistentListOf(),
    val createdAt: Date = Date(),
    val rating: Float = Float.NaN,
    val ratingCount: Int = 0,
    val imagesUrl: PersistentList<String> = persistentListOf(),
    val placeTags: PersistentList<String> = persistentListOf(),
    val placeCity: String = "",
    val placeCountry: String = "",
    val authorName: String = "",
    val authorId: String = ""
)

sealed interface PlaceEvent {
    object OnAuthorClick : PlaceEvent
    object OnBackBtnClick : PlaceEvent
    data class OnInputCommentTextChange(val inputCommentText: String) : PlaceEvent
    data class OnUserPlaceRatingChange(val rating: Int) : PlaceEvent
    object OnCreateComment : PlaceEvent
    object OnDismissProgressbarDialog : PlaceEvent
    object OnDismissErrorsDialog : PlaceEvent
}

sealed interface PlaceSideEffect {
    object NavigateToUserProfileScreen : PlaceSideEffect
    object NavigateBack : PlaceSideEffect
}


@HiltViewModel
class PlaceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPlace: GetPlaceUseCase,
    private val currentUserUseCase: GetCurrentUserUseCase,
    private val addPlaceCommentUseCase: AddPlaceCommentUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<PlaceState> = MutableStateFlow(PlaceState())
    val state: StateFlow<PlaceState> = _state

    private val _action = MutableSharedFlow<PlaceSideEffect?>()
    val action: SharedFlow<PlaceSideEffect?>
        get() = _action.asSharedFlow()

    init {
        val id = savedStateHandle[HomeNavScreen.PLACE_ID_KEY] ?: HomeNavScreen.INVALID_ID
        viewModelScope.launch {
            _state.emit(state.value.copy(currentUser = currentUserUseCase()))
            loadPlace(id)
        }
    }

    private var currentJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }


    fun event(placeEvent: PlaceEvent) {
        when (placeEvent) {
            PlaceEvent.OnAuthorClick -> onAuthorClick()
            PlaceEvent.OnBackBtnClick -> onBackBtnClick()
            PlaceEvent.OnCreateComment -> onCreateComment()
            is PlaceEvent.OnInputCommentTextChange -> onInputCommentTextChange(placeEvent)
            is PlaceEvent.OnUserPlaceRatingChange -> onUserPlaceRatingChange(placeEvent)
            PlaceEvent.OnDismissErrorsDialog -> dismissErrorsDialog()
            PlaceEvent.OnDismissProgressbarDialog -> dismissProgressbarDialog()
        }
    }

    private fun dismissProgressbarDialog() {
        currentJob?.cancel()
        _state.tryEmit(_state.value.copy(showLoadingDialog = false))
    }

    private fun dismissErrorsDialog() {
        _state.tryEmit(_state.value.copy(showErrorsDialog = false))
    }

    private fun onUserPlaceRatingChange(event: PlaceEvent.OnUserPlaceRatingChange) {
        _state.tryEmit(
            _state.value.copy(
                userPlaceRating = if (event.rating == _state.value.userPlaceRating) null else event.rating,
            )
        )
    }

    private fun onCreateComment() {
        if (state.value.userPlaceRating == null)
            _state.tryEmit(_state.value.copy(showErrorsDialog = true, errors = persistentListOf("Поставьте оценку.")))

        val rating = state.value.userPlaceRating ?: return
        val user = state.value.currentUser ?: return

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val errors = mutableListOf<String>()
            val comment = Comment(
                user.id,
                user.username,
                rating,
                Date(),
                state.value.inputCommentText
            )
            _state.emit(_state.value.copy(showLoadingDialog = true))
            val result = addPlaceCommentUseCase(state.value.placeId, comment)
            _state.emit(_state.value.copy(showLoadingDialog = false))

            when (result) {
                is FirestoreActionResult.SuccessResult -> {
                    loadPlace(state.value.placeId)
                }

                is FirestoreActionResult.FailResult -> {
                    result.message?.let { errors.add(it) }
                    _state.emit(_state.value.copy(showErrorsDialog = true, errors = errors.toPersistentList()))
                }
            }
        }
    }

    private fun onInputCommentTextChange(event: PlaceEvent.OnInputCommentTextChange) {
        _state.tryEmit(
            _state.value.copy(
                inputCommentText = event.inputCommentText
            )
        )
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(PlaceSideEffect.NavigateBack)
        }
    }

    private fun onAuthorClick() {
        viewModelScope.launch {
            _action.emit(PlaceSideEffect.NavigateToUserProfileScreen)
        }
    }


    private suspend fun loadPlace(id: String) {
        if (id == HomeNavScreen.INVALID_ID) {
            _state.emit(_state.value.copy(cardState = PlaceCardState.NOT_FOUND))
            return
        }

        _state.emit(_state.value.copy(cardState = PlaceCardState.PROGRESS_BAR))
        val result = getPlace(id)

        _state.emit(
            if (result == null) _state.value.copy(cardState = PlaceCardState.NOT_FOUND)
            else
                _state.value.copy(
                    cardState = PlaceCardState.CONTENT,
                    lat = result.lat,
                    lon = result.lon,
                    placeName = result.placeName,
                    placeDescription = result.placeDescription,
                    comments = result.comments.toPersistentList(),
                    createdAt = result.createdAt,
                    rating = result.calculateRating(),
                    ratingCount = result.ratingCount,
                    imagesUrl = result.imagesUrl.toPersistentList(),
                    placeTags = result.tags.toPersistentList(),
                    placeCity = result.city,
                    placeCountry = result.country,
                    authorName = result.authorName ?: throw IllegalArgumentException(),
                    authorId = result.authorId ?: throw IllegalArgumentException(),
                    placeId = result.id ?: throw IllegalArgumentException(),
                )
        )
    }
}