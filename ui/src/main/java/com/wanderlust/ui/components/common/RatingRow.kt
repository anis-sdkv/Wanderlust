package com.wanderlust.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.screens.route.RouteEvent


@Composable
fun RatingRow(modifier: Modifier, rating: Int, ratingCount: Int, userRouteRating: Int?, onStarClick: (Int) -> Unit) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rating.toString(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier,
                    style = WanderlustTheme.typography.semibold20,
                    color = WanderlustTheme.colors.primaryText
                )
                Icon(
                    painterResource(R.drawable.ic_star),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(14.dp),
                    contentDescription = "icon_dropdown_menu",
                    tint = WanderlustTheme.colors.accent
                )
            }
            Text(
                text = ratingCount.toString() + " " + stringResource(id = R.string.ratings),
                textAlign = TextAlign.Start,
                modifier = Modifier,
                style = WanderlustTheme.typography.semibold20,
                color = WanderlustTheme.colors.secondaryText
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            val stars = listOf<Int>(1,2,3,4,5)
            stars.forEach{
                Icon(
                    painterResource(R.drawable.ic_star),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .clickable {
                            onStarClick(it)
                        },
                    contentDescription = "icon_dropdown_menu",
                    tint = if(userRouteRating != null && userRouteRating >= it)
                        WanderlustTheme.colors.accent else WanderlustTheme.colors.secondaryText
                )
            }
        }
    }
}