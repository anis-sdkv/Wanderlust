package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wanderlust.domain.model.Comment
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme


@Composable
fun CommentField(
    modifier: Modifier,
    comment: Comment,
    images: List<String>,
    onDeleteClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null
) {

    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(top = 12.dp, bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.secondaryBackground),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(size = 64.dp),
                painter = painterResource(id = R.drawable.test_user_photo),
                contentDescription = "user avatar",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier,
                    text = comment.authorNickname,
                    style = WanderlustTheme.typography.bold16,
                    color = WanderlustTheme.colors.primaryText
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = comment.createdAt.toString(),
                    style = WanderlustTheme.typography.medium13,
                    color = WanderlustTheme.colors.secondaryText
                )
            }
            if (onDeleteClick != null && onEditClick != null) {
                IconButton(
                    onClick = { dropdownMenuExpanded = !dropdownMenuExpanded },
                    modifier = Modifier
                        .weight(0.2f)
                ) {
                    Icon(
                        painterResource(R.drawable.ic_dropdown_menu),
                        modifier = Modifier,
                        contentDescription = "icon_dropdown_menu",
                        tint = WanderlustTheme.colors.secondaryText
                    )
                    Box(modifier.offset(x = -(120).dp)) {
                        DropdownMenu(
                            modifier = Modifier
                                .background(color = WanderlustTheme.colors.secondaryBackground)
                                .clip(RoundedCornerShape(8.dp)),
                            expanded = dropdownMenuExpanded,
                            onDismissRequest = { dropdownMenuExpanded = false },
                        ) {
                            CustomDropDownItem(text = stringResource(id = R.string.delete), onClick = onDeleteClick)
                            CustomDropDownItem(text = stringResource(id = R.string.edit), onClick = onEditClick)
                        }
                    }
                }
            }
        }

        Text(
            modifier = Modifier.padding(16.dp),
            text = comment.text ?: "",
            style = WanderlustTheme.typography.medium16,
            color = WanderlustTheme.colors.primaryText
        )

        if (images.isNotEmpty()) {
            ImagesRow(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                gradientColor = WanderlustTheme.colors.secondaryBackground,
                isAddingEnable = false,
                imagesUrl = images
            ) { }
        }
    }
}