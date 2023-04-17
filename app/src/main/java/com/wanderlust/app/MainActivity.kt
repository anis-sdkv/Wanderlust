package com.wanderlust.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wanderlust.ui.navigation.SetBottomNavigationBar
import com.wanderlust.ui.theme.WanderlustTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WanderlustTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SetBottomNavigationBar()
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BottomNavigationPreview() {
//    com.wanderlust.ui.navigation.SetBottomNavigationBar()
//}
