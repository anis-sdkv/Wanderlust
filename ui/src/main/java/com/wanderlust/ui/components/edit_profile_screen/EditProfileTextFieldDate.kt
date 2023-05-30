package com.wanderlust.ui.components.edit_profile_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.components.edit_profile_screen.DateDefaults.DATE_LENGTH
import com.wanderlust.ui.components.edit_profile_screen.DateDefaults.DATE_MASK
import com.wanderlust.ui.screens.edit_profile.EditProfileEvent
import com.wanderlust.ui.theme.WanderlustTextStyles
import kotlin.math.absoluteValue


@Composable
fun EditProfileTextFieldDate(label: String, inputValue: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
    ) {
        var value by remember { mutableStateOf(inputValue) }

        Text(
            text = label,
            style = WanderlustTextStyles.EditProfileInputTextLabel,
            modifier = Modifier
                .padding(start = 18.dp, bottom = 4.dp)
                .alpha(0.5f)
        )

        TextField(
            value = value,
            onValueChange = {
                if (it.length <= DATE_LENGTH) {
                    //eventHandler.invoke(EditProfileEvent.OnUserChanged(it))
                    value = it
                }
            },
            singleLine = true,
            textStyle = WanderlustTextStyles.EditProfileInputText,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = MaskVisualTransformation(DATE_MASK),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .border(width = (1.5).dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}

object DateDefaults {
    const val DATE_MASK = "##.##.####"
    const val DATE_LENGTH = 8 // Equals to "##.##.####".count { it == '#' }
}