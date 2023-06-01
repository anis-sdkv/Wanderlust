package com.wanderlust.ui.screens.home

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Route
import com.wanderlust.ui.R
import com.wanderlust.ui.screens.edit_profile.EditProfileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeState(
    val routes: List<Route>,
    val searchValue: String,
    val selectedCategory: SortCategory,
    val selectedTags: List<String>,
    val searchType: Boolean
)

sealed interface HomeEvent{
    object OnRouteClick : HomeEvent
    object OnFilterBtnClick: HomeEvent
    object OnSearchBtnClick: HomeEvent
    data class OnSearchValueChanged(val searchValue: String): HomeEvent
    data class OnCategoryClick(val selectedCategory: SortCategory) : HomeEvent
}

sealed interface HomeSideEffect {
    object NavigateToSearchScreen: HomeSideEffect
    object NavigateToRouteScreen: HomeSideEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val searchValue: String = savedStateHandle["searchValue"] ?: ""
    private val searchType: Boolean = savedStateHandle["searchType"] ?: true

    //private val selectedTags: TagsList = savedStateHandle["searchTags"] ?: TagsList(emptyList())

    private val internalState: MutableStateFlow<HomeState> = MutableStateFlow(
        HomeState(
            emptyList(),
            if (searchValue == "empty") "" else searchValue,
            SortCategory.ALL_ROUTES,
            emptyList(),// selectedTags.tags
            searchType
        )
    )
    val state: StateFlow<HomeState> = internalState

    private val _action = MutableSharedFlow<HomeSideEffect?>()
    val action: SharedFlow<HomeSideEffect?>
        get() = _action.asSharedFlow()

    fun event(homeEvent: HomeEvent){
        when(homeEvent){
            HomeEvent.OnFilterBtnClick -> onFilterBtnClick()
            HomeEvent.OnRouteClick -> onRouteClick()
            HomeEvent.OnSearchBtnClick -> onSearchBtnClick()
            is HomeEvent.OnSearchValueChanged -> onSearchValueChanged(homeEvent)
            is HomeEvent.OnCategoryClick -> onCategoryClick(homeEvent)
        }
    }

    private fun onCategoryClick(event: HomeEvent.OnCategoryClick){
        internalState.tryEmit(
            internalState.value.copy(
                selectedCategory = event.selectedCategory
            )
        )
    }

    private fun onSearchValueChanged(event: HomeEvent.OnSearchValueChanged){
        internalState.tryEmit(
            internalState.value.copy(
                searchValue = event.searchValue
            )
        )
    }

    private fun onSearchBtnClick(){
        viewModelScope.launch {
            // TODO
        }
    }

    private fun onRouteClick(){
        viewModelScope.launch {
            _action.emit(HomeSideEffect.NavigateToSearchScreen)
        }
    }

    private fun onFilterBtnClick(){
        viewModelScope.launch {
            _action.emit(HomeSideEffect.NavigateToSearchScreen)
        }
    }
}