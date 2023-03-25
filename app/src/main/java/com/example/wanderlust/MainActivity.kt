package com.example.wanderlust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wanderlust.ui.screens.sign_in.SignInScreen
import com.example.wanderlust.ui.theme.WanderlustTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WanderlustTheme {
                SignInScreen()
            }
        }
    }
}