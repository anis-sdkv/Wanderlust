package com.wanderlust.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    ConstraintLayout(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(bottom = 64.dp)
    ){
        val (text, column) = createRefs()
        Column(
            modifier = Modifier.constrainAs(column) {
                top.linkTo(text.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ){
            Text(text = "Войти",
                modifier = Modifier
                    .clickable {
                        onNavigateToSignIn()
                    }
            )
            Text(text = "Редактировать Профиль",
                modifier = Modifier
                    .clickable {
                        onNavigateToEditProfile()
                    }
            )
        }

    }
}