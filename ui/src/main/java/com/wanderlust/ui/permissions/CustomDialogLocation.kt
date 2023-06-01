package com.wanderlust.ui.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun CustomDialogLocation(
    title: String? = "",
    desc: String? = "",
    enableLocation: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onClick: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            onNavigateBack()
            enableLocation.value = false
        }
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .background(
                    color = WanderlustTheme.colors.solid,
                    shape = RoundedCornerShape(25.dp, 5.dp, 25.dp, 5.dp)
                )
                .verticalScroll(rememberScrollState())

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_in_route_list),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 10.dp)
                        .height(300.dp)
                        .fillMaxWidth(),

                    )

                Text(
                    text = title!!,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = WanderlustTheme.typography.bold24,
                    color = WanderlustTheme.colors.primaryText,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = desc!!,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    letterSpacing = 1.sp,
                    style = WanderlustTheme.typography.medium16,
                    color = WanderlustTheme.colors.primaryText,
                )

                Spacer(modifier = Modifier.height(24.dp))

                val cornerRadius = 16.dp
                val gradientColors = listOf(WanderlustTheme.colors.accent, WanderlustTheme.colors.tint)
                val roundedCornerShape = RoundedCornerShape(topStart = 30.dp,bottomEnd = 30.dp)

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp),
                    onClick=onClick,
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(colors = gradientColors),
                                shape = roundedCornerShape
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.enable),
                            style = WanderlustTheme.typography.semibold16,
                            color = WanderlustTheme.colors.onAccent
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}