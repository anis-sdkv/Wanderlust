package com.wanderlust.ui.screens.home

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetAllPlacesUseCase
import com.wanderlust.domain.usecases.GetAllRoutesUseCase
import com.wanderlust.ui.screens.search.TagsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject


@Immutable
data class HomeState(
    val routes: PersistentList<Route> = persistentListOf(),
    val places: PersistentList<Place> = persistentListOf(),
    val searchValue: String = "",
    val selectedCategory: SortCategory = SortCategory.ALL_ROUTES,
    val selectedTags: PersistentList<String> = persistentListOf(),
    val searchByName: Boolean = true
)

sealed interface HomeEvent {
    object OnLaunch : HomeEvent
    object OnDispose : HomeEvent
    object OnFilterBtnClick : HomeEvent
    object OnSearchBtnClick : HomeEvent
    data class OnSearchValueChanged(val searchValue: String) : HomeEvent
    data class OnCategoryClick(val selectedCategory: SortCategory) : HomeEvent
    data class OnRouteCardClick(val id: String) : HomeEvent
    data class OnPlaceCardClick(val id: String) : HomeEvent
}

sealed interface HomeSideEffect {
    object NavigateToSearchScreen : HomeSideEffect
    data class NavigateToRouteScreen(val id: String) : HomeSideEffect
    data class NavigateToPlaceScreen(val id: String) : HomeSideEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getAllRoutesUseCase: GetAllRoutesUseCase
) : ViewModel() {


    private val searchValue: String = savedStateHandle["searchValue"] ?: ""
    private val searchByName: Boolean = savedStateHandle["searchType"] ?: true

    private val selectedTags: TagsList = Gson().fromJson(
        Uri.decode(savedStateHandle["searchTags"]), TagsList::class.java
    ) ?: TagsList(emptyList())

    private val _state: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState(searchValue = if (searchValue == "empty") "" else searchValue, searchByName = searchByName, selectedTags = selectedTags.tags.toPersistentList()))
    val state: StateFlow<HomeState> = _state

    private val _action = MutableSharedFlow<HomeSideEffect?>()
    val action: SharedFlow<HomeSideEffect?>
        get() = _action.asSharedFlow()

    private var allRoutes: List<Route> = listOf()
    private var allPlaces: List<Place> = listOf()

    fun event(homeEvent: HomeEvent) {
        ViewModelProvider.NewInstanceFactory
        when (homeEvent) {
            HomeEvent.OnLaunch -> onLaunch()
            HomeEvent.OnDispose -> onDispose()
            HomeEvent.OnFilterBtnClick -> onFilterBtnClick()
            HomeEvent.OnSearchBtnClick -> filter()
            is HomeEvent.OnSearchValueChanged -> onSearchValueChanged(homeEvent)
            is HomeEvent.OnCategoryClick -> onCategoryClick(homeEvent)
            is HomeEvent.OnRouteCardClick -> onRouteClick(homeEvent.id)
            is HomeEvent.OnPlaceCardClick -> onPlaceClick(homeEvent.id)
        }
    }

    private fun onLaunch() {
        viewModelScope.launch {
            allRoutes = getAllRoutesUseCase()
            allPlaces = getAllPlacesUseCase()
            _state.emit(
                _state.value.copy(
                    places = allPlaces.toPersistentList(),
                    routes = allRoutes.toPersistentList()
                )
            )
            filter()
        }
    }

    private fun onDispose() {
        allPlaces = listOf()
        allRoutes = listOf()
    }

    private fun onCategoryClick(event: HomeEvent.OnCategoryClick) {
        _state.tryEmit(
            _state.value.copy(
                selectedCategory = event.selectedCategory
            )
        )
    }

    private fun onSearchValueChanged(event: HomeEvent.OnSearchValueChanged) {
        _state.tryEmit(_state.value.copy(searchValue = event.searchValue))
    }

    private fun filter() {
        viewModelScope.launch {
            val pattern = Regex(".*${state.value.searchValue}.*")
            when (state.value.selectedCategory) {
                SortCategory.ALL_ROUTES -> {
                    val result = allRoutes.filter {
                        it.tags.containsAll(state.value.selectedTags) &&
                                if (state.value.searchByName) pattern.matches(it.routeName)
                                else pattern.matches(it.authorName!!)
                    }
                    _state.emit(_state.value.copy(routes = result.toPersistentList()))
                }

                SortCategory.ALL_PLACES -> {
                    val result = allPlaces.filter {
                        it.tags.containsAll(state.value.selectedTags) &&
                                if (state.value.searchByName) pattern.matches(it.placeName)
                                else pattern.matches(it.authorName!!)
                    }
                    _state.emit(_state.value.copy(places = result.toPersistentList()))
                }

                else -> {}
            }

        }
    }

    private fun onRouteClick(id: String) {
        viewModelScope.launch {
            _action.emit(HomeSideEffect.NavigateToRouteScreen(id))
        }
    }

    private fun onPlaceClick(id: String) {
        viewModelScope.launch {
            _action.emit(HomeSideEffect.NavigateToPlaceScreen(id))
        }
    }

    private fun onFilterBtnClick() {
        viewModelScope.launch {
            _action.emit(HomeSideEffect.NavigateToSearchScreen)
        }
    }
}