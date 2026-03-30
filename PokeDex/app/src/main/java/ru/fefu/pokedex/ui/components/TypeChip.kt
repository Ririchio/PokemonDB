package ru.fefu.pokedex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.fefu.pokedex.ui.theme.getTypeColor

@Composable
fun TypeChip(
    typeName: String,
    modifier: Modifier = Modifier
) {
    val typeColor = getTypeColor(typeName)

    Surface(
        modifier = modifier,
        shape = androidx.compose.material3.MaterialTheme.shapes.small,
        color = Color(typeColor),
        shadowElevation = 2.dp
    ) {
        Text(
            text = typeName.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}