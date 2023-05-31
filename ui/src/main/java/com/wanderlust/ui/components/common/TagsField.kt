package com.wanderlust.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceTint),
    ) {
        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .alpha(0.6f),
            text = stringResource(id = R.string.select_tags),
            textAlign = TextAlign.Center,
            style = WanderlustTheme.typography.semibold14,
            color = MaterialTheme.colorScheme.onBackground
        )
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
                        .height(IntrinsicSize.Min)
                        .padding(4.dp)
                        //.padding(start = 4.dp, end = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = if(selectedTags.contains(tag)){
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                        .clickable { onTagClick(tag) }
                    ,
                    //contentPadding = PaddingValues(vertical = 0.dp, horizontal = 0.dp),

                    //shape = RoundedCornerShape(8.dp),
                    /*colors =
                    if(selectedTags.contains(tag)){
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    } else {
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    },*/
                    /*onClick = {
                        onTagClick(tag)
                    }*/
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = tag,
                        style = WanderlustTheme.typography.semibold14,
                        color =
                        if(selectedTags.contains(tag)){
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                }
            }
        }
    }
}