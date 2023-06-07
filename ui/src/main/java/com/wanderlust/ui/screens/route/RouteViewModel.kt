package com.wanderlust.ui.screens.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.RoutePoint
import com.wanderlust.ui.screens.place.PlaceEvent
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
data class RouteState(
    val routeName: String = "Route",
    val routeDescription: String = "12345",
    val createdAt: Date = Date(),
    val points: PersistentList<RoutePoint> = persistentListOf(
        RoutePoint(
            lat = 55.790278,
            lon = 49.13472200000001,
            "place1",
            "1234",
            emptyList()
        ),
        RoutePoint(
            lat = 55.780278,
            lon = 49.13072200000001,
            "place2",
            "5678",
            emptyList()
        )
    ),
    val comments: PersistentList<Comment> = persistentListOf(
        Comment(
            "777",
            "Name1",
            5,
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
    val totalRating: Int = 25,
    val ratingCount: Int = 6,
    val routeTags: PersistentList<String> = persistentListOf("Day", "Long distance", "In the city", "dsldffmlkefmwkedl"),
    val routeCity: String = "Kazan",
    val routeCountry: String = "Russia",
    val authorName: String = "Author",

    // Отображать ли комментарий пользователя:
    val isShowUserComment: Boolean = false,

    // это нужно для того, чтобы имя(post/edit) и цвет кнопки определять
    val isEditComment: Boolean = false,

    // дефолтный комментарий
    val userComment: Comment =
        Comment(
            "777",
            "Name1",
            5,
            Date(),
            "",
        ),
)

sealed interface RouteEvent {
    object OnAuthorClick: RouteEvent
    object OnBackBtnClick: RouteEvent
    data class OnInputCommentTextChange(val inputCommentText: String) : RouteEvent
    data class OnUserRouteRatingChange(val rating: Int) : RouteEvent
    object OnCreateComment : RouteEvent
    object OnEditCommentIconClick: RouteEvent
    object OnDeleteCommentIconClick: RouteEvent
    object OnEditComment : RouteEvent
}


sealed interface RouteSideEffect {
    object NavigateToUserProfileScreen : RouteSideEffect
    object NavigateBack : RouteSideEffect
}


@HiltViewModel
class RouteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // получить из бд
    private val comment: Comment =
        Comment(
            "777",
            "Name1",
            4,
            Date(),
            "text1",
        )

    private val _state: MutableStateFlow<RouteState> = MutableStateFlow(
        RouteState(
            // If в бд есть коммент:
            userComment = comment,
            isShowUserComment = true
            // Else ничего не переопределяем
        )
    )
    val state: StateFlow<RouteState> = _state

    private val _action = MutableSharedFlow<RouteSideEffect?>()
    val action: SharedFlow<RouteSideEffect?>
        get() = _action.asSharedFlow()


    fun event(routeEvent: RouteEvent){
        when(routeEvent){
            RouteEvent.OnAuthorClick -> onAuthorClick()
            RouteEvent.OnBackBtnClick -> onBackBtnClick()

            RouteEvent.OnCreateComment -> onCreateComment()
            RouteEvent.OnEditComment -> onEditComment()

            RouteEvent.OnEditCommentIconClick -> onEditCommentIconClick()
            RouteEvent.OnDeleteCommentIconClick -> onDeleteCommentIconClick()

            is RouteEvent.OnInputCommentTextChange -> onInputCommentTextChange(routeEvent)
            is RouteEvent.OnUserRouteRatingChange -> onUserRouteRatingChange(routeEvent)
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

//    Шёл медведь по лесу, видит — машина стоит посреди опушки. Поджёг её, сел и сгорел.

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
                // вычитаем рейтинг изначального комментрия и добавляем новый рейтинг,
                // хз будет это так работать или нет
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

    private fun onUserRouteRatingChange(event: RouteEvent.OnUserRouteRatingChange){
        _state.tryEmit(
            _state.value.copy(
                userComment = _state.value.userComment.copy(
                    score = event.rating
                )
            )
        )
    }

    private fun onInputCommentTextChange(event: RouteEvent.OnInputCommentTextChange){
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
            _action.emit(RouteSideEffect.NavigateBack)
        }
    }

    private fun onAuthorClick(){
        viewModelScope.launch {
            _action.emit(RouteSideEffect.NavigateToUserProfileScreen)
        }
    }
}