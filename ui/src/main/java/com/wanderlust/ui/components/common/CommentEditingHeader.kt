package com.wanderlust.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


@Composable
fun CommentEditingHeader(modifier: Modifier, onEditBtnClick: () -> Unit, onDeleteBtnClick: () -> Unit) {
    Box(modifier = modifier.fillMaxWidth()){
        Text(
            text = stringResource(id = R.string.your_comment),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 16.dp)
                .align(Alignment.CenterStart),
            textAlign = TextAlign.Start,
            color = WanderlustTheme.colors.secondaryText,
            style = WanderlustTheme.typography.semibold16
        )

        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd).padding(top = 8.dp, end = 40.dp),
            onClick = { onEditBtnClick() }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_edit),
                contentDescription = "icon_send",
                modifier = Modifier
                    .size(32.dp),
                WanderlustTheme.colors.accent
            )
        }

        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd).padding(top = 4.dp),
            onClick = { onDeleteBtnClick() }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_trash),
                contentDescription = "icon_send",
                modifier = Modifier
                    .size(30.dp),
                WanderlustTheme.colors.secondaryText
            )
        }
    }
}