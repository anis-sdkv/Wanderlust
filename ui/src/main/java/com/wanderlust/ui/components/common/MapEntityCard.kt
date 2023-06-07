package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapEntityCard(
    name: String,
    rating: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(top = 12.dp, bottom = 12.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.solid),
        onClick = onClick
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val (routeName,
                routeImage,
                routeInfo,
                iconShowMore) = createRefs()

            Image(
                painterResource(R.drawable.ic_in_route_list),
                contentDescription = "route_image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(routeImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
            Text(
                text = name,
                modifier = Modifier
                    .constrainAs(routeName) {
                        top.linkTo(parent.top)
                        start.linkTo(routeImage.end, margin = 16.dp)
                        bottom.linkTo(routeInfo.top, margin = 4.dp)
                    },
                style = WanderlustTheme.typography.semibold16,
                color = WanderlustTheme.colors.primaryText
            )
            Row( verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(routeInfo) {
                        top.linkTo(routeName.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(routeImage.end, margin = 16.dp)
                    }) {
                if (!rating.isNaN()) {
                    Icon(
                        painterResource(R.drawable.ic_star),
                        contentDescription = "icon_star",
                        modifier = Modifier,
                        tint = WanderlustTheme.colors.starColor
                    )
                    Text(
                        text = rating.toString(),
                        modifier = Modifier.padding(start = 8.dp),
                        style = WanderlustTheme.typography.semibold14,
                        color = WanderlustTheme.colors.secondaryText
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.no_ratings),
                        style = WanderlustTheme.typography.semibold14,
                        color = WanderlustTheme.colors.secondaryText
                    )
                }
            }

            Image(
                painterResource(R.drawable.ic_more),
                contentDescription = "icon_more",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(iconShowMore) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .alpha(0.5f)
            )
        }
    }
}