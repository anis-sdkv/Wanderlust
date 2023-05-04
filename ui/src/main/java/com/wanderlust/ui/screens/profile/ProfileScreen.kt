package com.wanderlust.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.wanderlust.ui.R
import com.wanderlust.ui.components.profile_screen.CreateRouteCard
import com.wanderlust.ui.theme.WanderlustTextStyles

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val profileState by viewModel.state.collectAsState()

    val lazyListState = rememberLazyListState()
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }

    // TODO Авторизован ли пользователь?
    val isAuthorized = profileState.isUserAuthorized

    // TODO Пользователь открыл свой профиль?
    val isMyProfile = profileState.isMyProfile

    // TODO Подписан ли пользователь на того, чей профиль открыл?
    val isSubscribe = profileState.isSubscribe

    var btnSelected by remember { mutableStateOf(isSubscribe) }
    val btnColor = if (btnSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val btnText = if(btnSelected) stringResource(id = R.string.unsubscribe) else stringResource(id = R.string.subscribe)

    val firstItemTranslationY by remember {
        derivedStateOf {
            when {
                lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty() && lazyListState.firstVisibleItemIndex == 0 ->
                    lazyListState.firstVisibleItemScrollOffset * .6f
                else -> 0f
            }
        }
    }
    val imageVisibility by remember {
        derivedStateOf {
            when {
                lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty() && lazyListState.firstVisibleItemIndex == 0 -> {
                    val imageSize = lazyListState.layoutInfo.visibleItemsInfo[0].size
                    val scrollOffset = lazyListState.firstVisibleItemScrollOffset

                    scrollOffset / imageSize.toFloat()
                }
                else -> 1f
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(bottom = 64.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy((-32).dp),
        content = {
            item {
                Image(
                    painterResource(R.drawable.test_user_photo),
                    contentDescription = "user_photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(372.dp)
                        .graphicsLayer {
                            //анимация и изменение непрозрачности изображения при скролле:
                            alpha = 1f - imageVisibility
                            translationY = firstItemTranslationY
                        }
                        .drawWithCache {
                            val gradient = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.LightGray),
                                startY = size.height / 3 * 2,
                                endY = size.height
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient, blendMode = BlendMode.Multiply)
                            }
                        }
                )
            }

            item{
                Card(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                ) {

                    // Если пользователь не авторизован и открыл свой профиль:
                    if(!isAuthorized && isMyProfile) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(id = R.string.empty_profile_title),
                                modifier = Modifier
                                    .padding(top = 32.dp),
                                style = WanderlustTextStyles.ProfileTitleText,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                text = stringResource(id = R.string.empty_profile_description),
                                modifier = Modifier.padding(top = 32.dp),
                                style = WanderlustTextStyles.ProfileLocationText,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Button(
                                onClick = {
                                    onNavigateToSignIn()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp, start = 28.dp, end = 28.dp)
                                    .height(42.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = btnColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.sign_in),
                                    style = WanderlustTextStyles.ProfileRouteTitleAndBtnText,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                        }
                    } else {
                        ConstraintLayout(
                            Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxSize()
                        ) {
                            val (userName,
                                dropdownMenuIcon,
                                dropdownMenu,
                                location,
                                subscribers,
                                btnSubscribeOrEdit,
                                userInfo,
                                routesTitle,
                                listOfRoutes,
                                showAllRoutes) = createRefs()

                            // Имя пользователя
                            Text(
                                text = profileState.userName,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .constrainAs(userName) {
                                        top.linkTo(parent.top, margin = 32.dp)
                                        start.linkTo(parent.start, margin = 20.dp)
                                        end.linkTo(parent.end, margin = 20.dp)},
                                style = WanderlustTextStyles.ProfileTitleText,
                                color = MaterialTheme.colorScheme.onBackground
                            )


                            // Кнопка DropdownMenu
                            IconButton(
                                onClick = { isDropdownMenuExpanded = true },
                                modifier = Modifier.constrainAs(dropdownMenuIcon) {
                                    top.linkTo(parent.top, margin = 28.dp)
                                    end.linkTo(parent.end, margin = 20.dp)}

                            ) {
                                Image(
                                    painterResource(R.drawable.ic_dropdown_menu),
                                    modifier = Modifier.alpha(0.5f),
                                    contentDescription = "icon_dropdown_menu",
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // DropdownMenu
                            Card(
                                modifier = Modifier
                                    .constrainAs(dropdownMenu) {
                                        top.linkTo(parent.top, margin = 68.dp)
                                        end.linkTo(parent.end, margin = 26.dp)
                                    }
                            ) {
                                DropdownMenu(
                                    modifier = Modifier.background(color = MaterialTheme.colorScheme.outline),
                                    expanded = isDropdownMenuExpanded,
                                    onDismissRequest = { isDropdownMenuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(id = R.string.copy)) },
                                        onClick = {
                                            // TODO
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(
                                        text = { Text(stringResource(id = R.string.settings)) },
                                        onClick = {
                                            // TODO
                                        }
                                    )
                                }
                            }

                            // Местоположение
                            Row(
                                modifier = Modifier.constrainAs(location) {
                                    top.linkTo(userName.bottom, margin = 16.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(R.drawable.baseline_location_on_24),
                                    contentDescription = "icon_location",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "${profileState.userCity}, ${profileState.userCountry}",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = WanderlustTextStyles.ProfileLocationText,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            // Маршруты, Подписчики, Подписки
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp)
                                    .constrainAs(subscribers) {
                                        top.linkTo(location.bottom, margin = 32.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            // TODO
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = profileState.userNumberOfRoutes.toString(),
                                        style = WanderlustTextStyles.ProfileNumbersText,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = stringResource(id = R.string.routes),
                                        style = WanderlustTextStyles.ProfileMedium13,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            // TODO
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = profileState.userNumberOfSubscribers.toString(),
                                        style = WanderlustTextStyles.ProfileNumbersText,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = stringResource(id = R.string.subscribers),
                                        style = WanderlustTextStyles.ProfileMedium13,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            // TODO
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = profileState.userNumberOfSubscriptions.toString(),
                                        style = WanderlustTextStyles.ProfileNumbersText,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = stringResource(id = R.string.subscriptions),
                                        style = WanderlustTextStyles.ProfileMedium13,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                            }

                            // Кнопка подписаться / редактировать профиль
                            Button(
                                onClick = {
                                    if (!isMyProfile) {
                                        btnSelected = !btnSelected

                                    } else {
                                        onNavigateToEditProfile()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp)
                                    .height(42.dp)
                                    .constrainAs(btnSubscribeOrEdit) {
                                        top.linkTo(subscribers.bottom, margin = 24.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isMyProfile) MaterialTheme.colorScheme.surface else btnColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isMyProfile) stringResource(id = R.string.edit) else btnText,
                                    style = WanderlustTextStyles.ProfileRouteTitleAndBtnText,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }

                            // Описание
                            Text(
                                text = profileState.userDescription
                                /*"1. Шел медведь по лесу, видит — машина горит, ну он вызвал пожарных, они приехали, оценили ситуацию, сели в машину и сгорели.\n" +
                                        "2. Шел медведь по лесу, видит — машина горит, он сел в неё, а она ему как раз."*/,
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp)
                                    .constrainAs(userInfo) {
                                        top.linkTo(btnSubscribeOrEdit.bottom, margin = 20.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                style = WanderlustTextStyles.ProfileUserInfoText,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            // Популярное у автора
                            Text(
                                text = stringResource(id = R.string.popular_from_author),
                                modifier = Modifier
                                    .constrainAs(routesTitle) {
                                        top.linkTo(userInfo.bottom, margin = 28.dp)
                                        start.linkTo(parent.start, margin = 20.dp)
                                    },
                                style = WanderlustTextStyles.ProfileRoutesTitleText,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            // Список маршрутов
                            Column(
                                modifier = Modifier
                                    .constrainAs(listOfRoutes) {
                                        top.linkTo(routesTitle.bottom, margin = 8.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            ) {
                                //  Надо бы что-то такое:
                                //  top5routes.forEach { route ->
                                //          CreateRouteCard(route)
                                //  }

                                //  Но пока что так:
                                val items = listOf(1, 2, 3, 4, 5)
                                items.forEach { _ ->
                                    CreateRouteCard()
                                }
                            }

                            TextButton(
                                onClick = {
                                    // TODO
                                },
                                modifier = Modifier
                                    .constrainAs(showAllRoutes) {
                                        top.linkTo(listOfRoutes.bottom, margin = 20.dp)
                                        bottom.linkTo(parent.bottom, margin = 56.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                            ) {
                                Text(
                                    text = stringResource(id = R.string.show_all_routes),
                                    style = WanderlustTextStyles.ProfileUserInfoText,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}





