package com.wanderlust.ui.components.auth_screens

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

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
            tint = WanderlustTheme.colors.primaryBackground
        )
        Text(
            modifier = Modifier.constrainAs(centerText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = text,
            style = WanderlustTheme.typography.semibold16,
            color = WanderlustTheme.colors.primaryBackground
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
            tint = WanderlustTheme.colors.primaryBackground
        )
    }
}