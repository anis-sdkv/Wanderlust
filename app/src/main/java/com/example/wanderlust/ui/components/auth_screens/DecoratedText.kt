package com.example.wanderlust.ui.components.auth_screens

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.wanderlust.R
import com.example.wanderlust.ui.theme.WanderlustTextStyles

@Composable
fun DecoratedText(text: String, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier.clipToBounds()) {
        val (leftLine, centerText, rightLine) = createRefs()
        Icon(
            contentDescription = "icon",
            modifier = Modifier
                .constrainAs(leftLine)
                {
                    top.linkTo(parent.top)
                    end.linkTo(centerText.start)
                    bottom.linkTo(parent.bottom)
                },
            painter = painterResource(id = R.drawable.ic_auth_line_left),
            tint = MaterialTheme.colorScheme.background
        )
        Text(
            modifier = Modifier.constrainAs(centerText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = text,
            style = WanderlustTextStyles.AuthorizationRegular,
            color = MaterialTheme.colorScheme.background
        )
        Icon(
            contentDescription = "icon",
            modifier = Modifier
                .constrainAs(rightLine) {
                    top.linkTo(parent.top)
                    start.linkTo(centerText.end)
                    bottom.linkTo(parent.bottom)
                },
            painter = painterResource(id = R.drawable.ic_auth_line_right),
            tint = MaterialTheme.colorScheme.background
        )
    }
}