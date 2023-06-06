package com.wanderlust.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wanderlust.domain.model.RoutePoint
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.screens.create_route.CreateRouteEvent


@Composable
fun CommentTextField (modifier: Modifier, inputValue: String, onChange: (String) -> Unit, onSendBtnClick: () -> Unit) {
    val maxChar = 300
    val maxLines = 15
    TextField(
        value = inputValue,
        onValueChange = { if (it.length <= maxChar)
            onChange(it)
        },
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = RoundedCornerShape(16.dp),
        textStyle = WanderlustTheme.typography.medium16,
        supportingText = {
            Text(
                text = "${inputValue.length} / $maxChar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.End,
                color = WanderlustTheme.colors.secondaryText
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.enter_the_comment),
                style = WanderlustTheme.typography.semibold14,
                color =WanderlustTheme.colors.secondaryText
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WanderlustTheme.colors.solid)
            .border(
                width = (1.5).dp,
                color = WanderlustTheme.colors.outline,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = WanderlustTheme.colors.primaryText,
            unfocusedTextColor = WanderlustTheme.colors.primaryText,
            cursorColor = WanderlustTheme.colors.accent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = WanderlustTheme.colors.solid,
            unfocusedContainerColor = WanderlustTheme.colors.solid,
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Button(
            onClick = {
                      onSendBtnClick()
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .height(34.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WanderlustTheme.colors.accent),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.send),
                style = WanderlustTheme.typography.medium16,
                color = WanderlustTheme.colors.onAccent
            )

            Icon(
                painterResource(id = R.drawable.ic_more),
                contentDescription = "icon_send",
                modifier = Modifier.padding(start = 6.dp).size(12.dp),
                WanderlustTheme.colors.onAccent
            )
        }

    }

}