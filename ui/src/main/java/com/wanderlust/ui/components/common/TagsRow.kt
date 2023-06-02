package com.wanderlust.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme


@Composable
fun TagsRow(modifier: Modifier, tags: List<String>) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            items(tags){ tag ->
                Box(
                    modifier = Modifier
                        .padding(
                            end = 16.dp
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background( color = WanderlustTheme.colors.accent )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = tag,
                        style = WanderlustTheme.typography.semibold14,
                        color = WanderlustTheme.colors.onAccent
                    )
                }
            }
        }
    )

}