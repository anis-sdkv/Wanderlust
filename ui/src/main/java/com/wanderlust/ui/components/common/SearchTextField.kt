package com.wanderlust.ui.components.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun SearchTextField(modifier: Modifier, searchValue: String, onChange: (String) -> Unit) {

    var value by remember { mutableStateOf(searchValue) }
    val maxChar = 25

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.solid)
    ) {
        TextField(
            value = value,
            onValueChange = {
                if (it.length <= maxChar) {
                    onChange(it)
                    value = it
                }
            },
            singleLine = true,
            textStyle = WanderlustTheme.typography.bold16,
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Text ),
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = WanderlustTheme.typography.semibold14,
                    color =WanderlustTheme.colors.secondaryText
                )
            },
            modifier = Modifier
                .height(52.dp)
                .width(220.dp)
                /*.border(
                    width = (1.5).dp,
                    color = WanderlustTheme.colors.outline,
                    shape = RoundedCornerShape(8.dp)
                )*/,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = WanderlustTheme.colors.solid,
                unfocusedContainerColor = WanderlustTheme.colors.solid,
                disabledContainerColor = WanderlustTheme.colors.solid,
                focusedTextColor = WanderlustTheme.colors.primaryText,
                unfocusedTextColor = WanderlustTheme.colors.primaryText,
                cursorColor = WanderlustTheme.colors.accent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }

    /*TextField(
        value = value,
        onValueChange = ,
        singleLine = true,
        textStyle = WanderlustTheme.typography.bold16,
        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Text ),
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        textStyle = WanderlustTheme.typography.bold16,
        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Text ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = WanderlustTheme.typography.semibold14,
                color =WanderlustTheme.colors.secondaryText
            )
                      },
        singleLine = true,
        textStyle = WanderlustTheme.typography.semibold16,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = WanderlustTheme.colors.solid,
            unfocusedContainerColor = WanderlustTheme.colors.solid,
            disabledContainerColor = WanderlustTheme.colors.solid,
            focusedTextColor = WanderlustTheme.colors.primaryText,
            unfocusedTextColor = WanderlustTheme.colors.primaryText,
            cursorColor = WanderlustTheme.colors.accent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )*/
}
