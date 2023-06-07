package com.wanderlust.ui.screens.place

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@Immutable
data class PlaceState(
    val id: String = "",
    val lat: Double = 55.790278,
    val lon: Double = 49.13472200000001,
    val placeName: String = "Place",
    val placeDescription: String = "123",
    val comments: PersistentList<Comment> = persistentListOf(
        Comment(
            "777",
            "Name1",
            1,
            Date(),
            "text1",
        ),
        Comment(
            "0000",
            "Name2",
            1,
            Date(),
            "text2",
        )
    ),
    val createdAt: Date = Date(),
    val totalRating: Int = 10,
    val ratingCount: Int = 2,
    val imagesUrl: PersistentList<String> = persistentListOf(),
    val placeTags: PersistentList<String> = persistentListOf("Day", "Long distance", "In the city", "dsldffmlkefmwkedl"),
    val placeCity: String = "Kazan",
    val placeCountry: String = "Russia",
    val authorName: String = "Author",

    // дефолтный комментарий
    val userComment: Comment =
        Comment(
            "777",
            "Name1",
            5,
            Date(),
            "",
        ),

    // Отображать ли комментарий пользователя:
    val isShowUserComment: Boolean = true,

    // это нужно для того, чтобы имя(post/edit) и цвет кнопки определять
    val isEditComment: Boolean = false
)

sealed interface PlaceEvent {
    object OnAuthorClick: PlaceEvent
    object OnBackBtnClick: PlaceEvent
    object OnEditCommentIconClick: PlaceEvent
    object OnDeleteCommentIconClick: PlaceEvent
    data class OnInputCommentTextChange(val inputCommentText: String) : PlaceEvent
    data class OnUserPlaceRatingChange(val rating: Int) : PlaceEvent
    object OnEditComment : PlaceEvent
    object OnCreateComment : PlaceEvent
}

sealed interface PlaceSideEffect {
    object NavigateToUserProfileScreen : PlaceSideEffect
    object NavigateBack : PlaceSideEffect
}


@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // получить из бд
    private val comment: Comment = Comment(
        "777",
        "Name1",
        4,
        Date(),
        "text1",
    )

    private val _state: MutableStateFlow<PlaceState> = MutableStateFlow(
        PlaceState(
            // If в бд есть коммент:
            userComment = comment,
            isShowUserComment = true
            // Else ничего не переопределяем
        )
    )
    val state: StateFlow<PlaceState> = _state

    private val _action = MutableSharedFlow<PlaceSideEffect?>()
    val action: SharedFlow<PlaceSideEffect?>
        get() = _action.asSharedFlow()


    fun event(placeEvent: PlaceEvent){
        when(placeEvent){
            PlaceEvent.OnAuthorClick -> onAuthorClick()
            PlaceEvent.OnBackBtnClick -> onBackBtnClick()

            PlaceEvent.OnEditComment -> onEditComment()
            PlaceEvent.OnCreateComment -> onCreateComment()

            PlaceEvent.OnEditCommentIconClick -> onEditCommentIconClick()
            PlaceEvent.OnDeleteCommentIconClick -> onDeleteCommentIconClick()

            is PlaceEvent.OnInputCommentTextChange -> onInputCommentTextChange(placeEvent)
            is PlaceEvent.OnUserPlaceRatingChange -> onUserPlaceRatingChange(placeEvent)
        }
    }

    private fun onDeleteCommentIconClick(){
        // TODO удаление из бд
        // плюс вот это:
        _state.tryEmit(
            _state.value.copy(
                isShowUserComment = false,
                isEditComment = false,

                // ставим дефолтные значения
                userComment = _state.value.userComment.copy(
                    text = "",
                    score = 5
                ),

                // обновить рейтинг у маршрута
                totalRating = _state.value.totalRating - _state.value.userComment.score,
                ratingCount = _state.value.ratingCount - 1
            )
        )
    }

    private fun onEditCommentIconClick(){
        _state.tryEmit(
            _state.value.copy(
                isShowUserComment = false,
                isEditComment = true,
            )
        )
    }

    private fun onEditComment(){
        // TODO сохранить в бд userComment
        // плюс вот это:
        _state.tryEmit(
            _state.value.copy(
                isShowUserComment = true,

                // обновить в бд рейтинг у места:
                // вычитаем рейтинг изначального комментрия и добавляем новый рейтинг, хз
                totalRating = _state.value.totalRating - comment.score + _state.value.userComment.score,
            )
        )
    }

    private fun onCreateComment(){
        // TODO сохранить в бд userComment
        // плюс вот это:
        _state.tryEmit(
            _state.value.copy(
                isShowUserComment = true,

                // обновить в бд рейтинг у места:
                totalRating = _state.value.totalRating + _state.value.userComment.score,
                ratingCount = _state.value.ratingCount + 1,
            )
        )
    }

    private fun onUserPlaceRatingChange(event: PlaceEvent.OnUserPlaceRatingChange){
        _state.tryEmit(
            _state.value.copy(
                userComment = _state.value.userComment.copy(
                    score = event.rating
                )
            )
        )
    }

    private fun onInputCommentTextChange(event: PlaceEvent.OnInputCommentTextChange){
        _state.tryEmit(
            _state.value.copy(
                userComment = _state.value.userComment.copy(
                    text = event.inputCommentText
                )
            )
        )
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(PlaceSideEffect.NavigateBack)
        }
    }

    private fun onAuthorClick(){
        viewModelScope.launch {
            _action.emit(PlaceSideEffect.NavigateToUserProfileScreen)
        }
    }
}