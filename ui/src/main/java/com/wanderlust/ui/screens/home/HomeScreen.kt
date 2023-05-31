package com.wanderlust.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wanderlust.ui.screens.edit_profile.EditProfileViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val HomeState by viewModel.state.collectAsStateWithLifecycle()
    //val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)
    ConstraintLayout(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(bottom = 100.dp)
    ){
        val (text) = createRefs()
        Text(text = "Home",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}