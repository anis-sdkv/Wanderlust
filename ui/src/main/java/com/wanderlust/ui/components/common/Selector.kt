package com.wanderlust.ui.components.common

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.utils.toDp
import com.wanderlust.ui.utils.toEasing

data class SelectableItem(
    val name: String,
    val onSelect: () -> Unit
)

@Composable
fun Selector(label: String, items: List<SelectableItem>, initIndex: Int) {
    val itemsOffset = Array(items.size) { 0f }
    var offset by remember { mutableStateOf(0f) }
    var elementWidth by remember { mutableStateOf(0) }

    val offsetAnim by animateFloatAsState(
        targetValue = offset,
        animationSpec = tween(
            durationMillis = 800, easing = AnticipateOvershootInterpolator(1f).toEasing()
        ),
        label = "",
    )

    Column {

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = label,
            style = WanderlustTheme.typography.bold20,
            color = WanderlustTheme.colors.primaryText
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(WanderlustTheme.colors.secondaryBackground)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = offsetAnim
                    }
                    .clip(RoundedCornerShape(8.dp))
                    .background(WanderlustTheme.colors.primaryBackground)
                    .fillMaxHeight()
                    .width(elementWidth.toDp())
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEachIndexed { index, element ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .onGloballyPositioned {
                                itemsOffset[index] = it.positionInParent().x
                                elementWidth = it.size.width
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = element.name,
                            style = WanderlustTheme.typography.semibold14,
                            color = WanderlustTheme.colors.secondaryText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    element.onSelect()
                                    offset = itemsOffset[index]
                                }
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
        LaunchedEffect(key1 = Unit) {
            offset = itemsOffset[initIndex]
        }
    }
}
