package com.wanderlust.ui.screens.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.RoutePoint

import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.usecases.AddRouteCommentUseCase
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.GetRouteUseCase
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

enum class RouteCardState {
    PROGRESS_BAR, CONTENT, NOT_FOUND
}

@Immutable
data class RouteState(
    val cardState: RouteCardState = RouteCardState.PROGRESS_BAR,
    val inputCommentText: String = "",
    val userRouteRating: Int? = null,
    val currentUser: UserProfile? = null,
    val inEditMode: Boolean = false,
    val showLoadingDialog: Boolean = false,
    val showErrorsDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf(),


    val routeId: String = "",
    val routeName: String = "",
    val routeDescription: String = "",
    val createdAt: Date = Date(),
    val points: PersistentList<RoutePoint> = persistentListOf(),
    val comments: PersistentList<Comment> = persistentListOf(),
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val routeTags: PersistentList<String> = persistentListOf(),
    val routeCity: String = "",
    val routeCountry: String = "",
    val authorName: String = "",
    val authorId: String = "",
)

sealed interface RouteEvent {
    object OnAuthorClick : RouteEvent
    object OnBackBtnClick : RouteEvent
    data class OnInputCommentTextChange(val inputCommentText: String) : RouteEvent
    data class OnUserRouteRatingChange(val rating: Int) : RouteEvent
    object OnCreateComment : RouteEvent
    object OnDismissProgressbarDialog : RouteEvent
    object OnDismissErrorsDialog : RouteEvent
    object OnEditCommentIconClick : RouteEvent
    object OnDeleteCommentIconClick : RouteEvent
    object OnEditComment : RouteEvent
}


sealed interface RouteSideEffect {
    data class NavigateToUserProfileScreen(val id: String) : RouteSideEffect
    object NavigateBack : RouteSideEffect
}


@HiltViewModel
class RouteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRouteUseCase: GetRouteUseCase,
    private val currentUserUseCase: GetCurrentUserUseCase,
    private val addRouteCommentUseCase: AddRouteCommentUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<RouteState> = MutableStateFlow(
        RouteState()
    )
    val state: StateFlow<RouteState> = _state

    private val _action = MutableSharedFlow<RouteSideEffect?>()
    val action: SharedFlow<RouteSideEffect?>
        get() = _action.asSharedFlow()

    init {
        val id = savedStateHandle[HomeNavScreen.ROUTE_ID_KEY] ?: HomeNavScreen.INVALID_ID
        viewModelScope.launch {
            _state.emit(state.value.copy(currentUser = currentUserUseCase()))
            loadRoute(id)
        }
    }

    private var currentJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }

    fun event(routeEvent: RouteEvent) {
        when (routeEvent) {
            RouteEvent.OnAuthorClick -> onAuthorClick()
            RouteEvent.OnBackBtnClick -> onBackBtnClick()

            RouteEvent.OnCreateComment -> onCreateComment()
            RouteEvent.OnEditComment -> onEditCommentIconClick()

            RouteEvent.OnEditCommentIconClick -> onEditCommentIconClick()
            RouteEvent.OnDeleteCommentIconClick -> onDeleteCommentIconClick()

            is RouteEvent.OnInputCommentTextChange -> onInputCommentTextChange(routeEvent)
            is RouteEvent.OnUserRouteRatingChange -> onUserRouteRatingChange(routeEvent)
            RouteEvent.OnDismissErrorsDialog -> dismissErrorsDialog()
            RouteEvent.OnDismissProgressbarDialog -> dismissProgressbarDialog()
        }
    }

    private fun dismissProgressbarDialog() {
        currentJob?.cancel()
        _state.tryEmit(_state.value.copy(showLoadingDialog = false))
    }

    private fun dismissErrorsDialog() {
        _state.tryEmit(_state.value.copy(showErrorsDialog = false))
    }

    private fun onDeleteCommentIconClick() {
        _state.tryEmit(
            _state.value.copy(
                showErrorsDialog = true,
                errors = persistentListOf("TODO")
            )
        )
    }

    //    Шёл медведь по лесу, видит — машина стоит посреди опушки. Поджёг её, сел и сгорел.
    private fun onEditCommentIconClick() {
        _state.tryEmit(
            _state.value.copy(
                showErrorsDialog = true,
                errors = persistentListOf("TODO")
            )
        )
    }
    private fun onCreateComment() {
        if (state.value.userRouteRating == null)
            _state.tryEmit(_state.value.copy(showErrorsDialog = true, errors = persistentListOf("Поставьте оценку.")))

        val rating = state.value.userRouteRating ?: return
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
            val result = addRouteCommentUseCase(state.value.routeId, comment)
            _state.emit(_state.value.copy(showLoadingDialog = false))

            when (result) {
                is FirestoreActionResult.SuccessResult -> {
                    loadRoute(state.value.routeId)
                }

                is FirestoreActionResult.FailResult -> {
                    result.message?.let { errors.add(it) }
                    _state.emit(_state.value.copy(showErrorsDialog = true, errors = errors.toPersistentList()))
                }
            }
        }
    }

    private fun onUserRouteRatingChange(event: RouteEvent.OnUserRouteRatingChange) {
        _state.tryEmit(_state.value.copy(userRouteRating = event.rating))
    }

    private fun onInputCommentTextChange(event: RouteEvent.OnInputCommentTextChange) {
        _state.tryEmit(
            _state.value.copy(inputCommentText = event.inputCommentText)
        )
    }

    private fun onBackBtnClick() {
        viewModelScope.launch { _action.emit(RouteSideEffect.NavigateBack) }
    }

    private fun onAuthorClick() {
        viewModelScope.launch { _action.emit(RouteSideEffect.NavigateToUserProfileScreen(state.value.authorId)) }
    }

    private suspend fun loadRoute(id: String) {
        if (id == HomeNavScreen.INVALID_ID) {
            _state.emit(_state.value.copy(cardState = RouteCardState.NOT_FOUND))
            return
        }

        _state.emit(_state.value.copy(cardState = RouteCardState.PROGRESS_BAR))
        val result = getRouteUseCase(id)
        _state.emit(
            if (result == null)
                _state.value.copy(cardState = RouteCardState.NOT_FOUND)
            else
                _state.value.copy(
                    cardState = RouteCardState.CONTENT,
                    routeName = result.routeName,
                    routeDescription = result.routeDescription,
                    createdAt = result.createdAt,
                    points = result.points.toPersistentList(),
                    comments = result.comments.toPersistentList(),
                    rating = result.calculateRating(),
                    ratingCount = result.ratingCount,
                    routeTags = result.tags.toPersistentList(),
                    routeCountry = result.country,
                    routeCity = result.city,
                    authorName = result.authorName ?: throw IllegalArgumentException(),
                    routeId = result.id ?: throw IllegalArgumentException(),
                    authorId = result.authorId ?: throw IllegalArgumentException()
                )
        )
    }
}