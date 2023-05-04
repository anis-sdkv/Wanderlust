package com.wanderlust.ui.components.profile_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.wanderlust.ui.R
import com.wanderlust.ui.theme.WanderlustTextStyles

@Composable
fun CreateRouteCard(
    //route: Route
){
    Card(modifier = Modifier
        .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ){
        ConstraintLayout(
            Modifier
                .clickable {
                    // TODO
                }
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
                text = //route.name
                "Route Name",
                modifier = Modifier
                    .constrainAs(routeName) {
                        top.linkTo(parent.top)
                        start.linkTo(routeImage.end, margin = 16.dp)
                        bottom.linkTo(routeInfo.top, margin = 4.dp)
                    },
                style = WanderlustTextStyles.ProfileRouteTitleAndBtnText,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row( verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(routeInfo) {
                        top.linkTo(routeName.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(routeImage.end, margin = 16.dp)
                    }) {
                Image(
                    painterResource(R.drawable.ic_star),
                    contentDescription = "icon_star",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
                Text(
                    text = //route.rating
                    stringResource(id = R.string.default_rating),
                    modifier = Modifier.padding(start = 8.dp),
                    style = WanderlustTextStyles.ProfileLocationText,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = //route.distance
                    stringResource(id = R.string.default_distance),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .alpha(0.5f),
                    style = WanderlustTextStyles.ProfileLocationText,
                    color = MaterialTheme.colorScheme.onBackground
                )
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