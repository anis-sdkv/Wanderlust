package com.wanderlust.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun HomeScreen() {
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