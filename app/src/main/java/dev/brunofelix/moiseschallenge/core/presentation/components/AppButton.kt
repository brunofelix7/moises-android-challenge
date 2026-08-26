package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing18
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing24

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(smallSpacing)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(spacing24),
                    tint = Color.Black
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        AppButton(
            text = stringResource(R.string.retry),
            icon = Icons.Rounded.Refresh,
            onClick = {}
        )
    }
}