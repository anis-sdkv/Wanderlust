package com.wanderlust.ui.screens.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Route
import com.wanderlust.ui.screens.home.HomeEvent
import com.wanderlust.ui.screens.home.HomeSideEffect
import com.wanderlust.ui.screens.home.SortCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchState(
    val searchValue: String,
    val selectedCategory: SortCategory,
    val selectedTags: List<String>,
    val typeOfSearch: Boolean
)

sealed interface SearchEvent{
    object OnSearchBtnClick: SearchEvent
    data class OnSearchValueChanged(val searchValue: String): SearchEvent
    data class OnSelectedTagsChanged(val tag: String) : SearchEvent
    data class OnTypeOfSearchChanged(val typeOfSearch: Boolean): SearchEvent
}

sealed interface SearchSideEffect {
    object NavigateToHomeScreen: SearchSideEffect
}

@HiltViewModel
class SearchViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val searchValue: String = savedStateHandle["searchValue"] ?: "empty"

    private val internalState: MutableStateFlow<SearchState> = MutableStateFlow(
        SearchState(
            if (searchValue == "empty") "" else searchValue,
            SortCategory.ALL_ROUTES,
            emptyList(),
            true
        )
    )
    val state: StateFlow<SearchState> = internalState

    private val _action = MutableSharedFlow<SearchSideEffect?>()
    val action: SharedFlow<SearchSideEffect?>
        get() = _action.asSharedFlow()

    fun event(searchEvent: SearchEvent){
        when(searchEvent){
            SearchEvent.OnSearchBtnClick -> onSearchBtnClick()
            is SearchEvent.OnTypeOfSearchChanged -> onTypeOfSearchChanged(searchEvent)
            is SearchEvent.OnSearchValueChanged -> onSearchValueChanged(searchEvent)
            is SearchEvent.OnSelectedTagsChanged -> onSelectedTagsChanged(searchEvent)
        }
    }

    private fun onTypeOfSearchChanged(event: SearchEvent.OnTypeOfSearchChanged){
        internalState.tryEmit(
            internalState.value.copy(
                typeOfSearch = event.typeOfSearch
            )
        )
    }

    private fun onSelectedTagsChanged(event: SearchEvent.OnSelectedTagsChanged){
        internalState.tryEmit(
            internalState.value.copy(
                selectedTags = internalState.value.selectedTags.toMutableList().also {
                    if(it.contains(event.tag)) it.remove(event.tag) else it.add(event.tag)
                }
            )
        )
    }

    private fun onSearchValueChanged(event: SearchEvent.OnSearchValueChanged){
        internalState.tryEmit(
            internalState.value.copy(
                searchValue = event.searchValue
            )
        )
    }

    private fun onSearchBtnClick(){
        viewModelScope.launch {
            _action.emit(SearchSideEffect.NavigateToHomeScreen)
        }
    }
}
