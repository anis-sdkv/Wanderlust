package com.wanderlust.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsField(modifier: Modifier, onTagClick: (String) -> Unit, selectedTags: List<String>,) {
    Card(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.secondaryBackground),
    ) {
        val tags = stringArrayResource(id = R.array.tags_array)
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            for (tag in tags) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = if(selectedTags.contains(tag)){
                                WanderlustTheme.colors.accent
                            } else {
                                WanderlustTheme.colors.primaryBackground
                            }
                        )
                        .clickable { onTagClick(tag) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = tag,
                        style = WanderlustTheme.typography.semibold14,
                        color =
                        if(selectedTags.contains(tag)){
                            WanderlustTheme.colors.onAccent
                        } else {
                            WanderlustTheme.colors.secondaryText
                        }
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .alpha(0.6f),
            text = stringResource(id = R.string.select_tags),
            textAlign = TextAlign.Center,
            style = WanderlustTheme.typography.semibold14,
            color = WanderlustTheme.colors.primaryText
        )
    }
}