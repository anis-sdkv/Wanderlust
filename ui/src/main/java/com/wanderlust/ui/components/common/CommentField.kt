package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wanderlust.domain.model.Comment
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme


@Composable
fun CommentField(modifier: Modifier, comment: Comment, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.secondaryBackground),
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier.clickable { onClick() },
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
        }
        Text(
            modifier = Modifier.padding(16.dp),
            text = comment.text ?: "",
            style = WanderlustTheme.typography.medium16,
            color = WanderlustTheme.colors.primaryText
        )
    }
}