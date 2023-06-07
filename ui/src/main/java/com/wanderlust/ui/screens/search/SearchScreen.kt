package com.wanderlust.ui.screens.search

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import com.google.gson.Gson
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.DefaultButton
import com.wanderlust.ui.components.common.SearchTextField
import com.wanderlust.ui.components.common.SelectableItem
import com.wanderlust.ui.components.common.Selector
import com.wanderlust.ui.components.common.TagsField
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.graphs.bottom_navigation.HomeNavScreen
import com.wanderlust.ui.navigation.graphs.bottom_navigation.MapNavScreen
import kotlinx.parcelize.Parcelize


@Parcelize
data class TagsList(val tags: List<String>) : Parcelable

/*class TagsType : NavType<TagsList>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): TagsList? {
        return bundle.getParcelable(key)
    }
    override fun parseValue(value: String): TagsList {
        return Gson().fromJson(value, TagsList::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: TagsList) {
        bundle.putParcelable(key, value)
    }
}*/

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchScreen(
    navController: NavController,
    screenName: String?,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)


    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            SearchSideEffect.NavigateToHomeScreen -> {
                if(screenName == "home") {
                    navController.navigate(
                        HomeNavScreen.Home.passValues(
                            searchValue = if (searchState.searchValue == "") "empty" else searchState.searchValue,
                            searchType = searchState.typeOfSearch,
                            searchTags = Uri.encode(Gson().toJson(TagsList(searchState.selectedTags)))
                        )
                    )
                } else if (screenName == "map"){
                    navController.navigate(
                        MapNavScreen.Map.passValues(
                            searchValue = if (searchState.searchValue == "") "empty" else searchState.searchValue,
                            searchType = searchState.typeOfSearch,
                            searchTags = Uri.encode(Gson().toJson(TagsList(searchState.selectedTags)))
                        )
                    )
                }
            }
        }
    }

    val typesOfSearch = listOf(
        SelectableItem(stringResource(id = R.string.by_name)) { eventHandler.invoke(SearchEvent.OnTypeOfSearchChanged(true))},
        SelectableItem(stringResource(id = R.string.by_author)) { eventHandler.invoke(SearchEvent.OnTypeOfSearchChanged(false)) },
    )

    Column(
        modifier = Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize()
    ) {
        SearchTextField(
            modifier = Modifier
                .padding(top = 60.dp, bottom = 32.dp)
                .fillMaxWidth(),
            searchValue = searchState.searchValue,
            onChange = { searchValue ->
                eventHandler.invoke(SearchEvent.OnSearchValueChanged(searchValue))
            }
        )
        Selector(
            label = stringResource(id = R.string.type_of_search),
            items = typesOfSearch,
            initIndex = if (searchState.typeOfSearch) 0 else 1
        )
        Text(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            text = stringResource(id = R.string.choose_tags),
            style = WanderlustTheme.typography.bold20,
            color = WanderlustTheme.colors.primaryText
        )
        TagsField(
            modifier = Modifier,
            onTagClick = {tag -> eventHandler.invoke(SearchEvent.OnSelectedTagsChanged(tag))},
            selectedTags = searchState.selectedTags
        )

        DefaultButton(
            modifier = Modifier.padding(top = 22.dp, bottom = 80.dp),
            text = stringResource(id = R.string.search),
            buttonColor = WanderlustTheme.colors.accent,
            textColor = WanderlustTheme.colors.onAccent
        ) {
            eventHandler.invoke(SearchEvent.OnSearchBtnClick)
        }
    }
}