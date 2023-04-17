package com.wanderlust.ui.screens.create_place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun CreatePlaceScreen() {
    ConstraintLayout(
        Modifier
            .background(Color.White)
            .fillMaxSize()
    ){
        val (text) = createRefs()
        Text(text = "CreatePlace",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top, margin = 200.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}