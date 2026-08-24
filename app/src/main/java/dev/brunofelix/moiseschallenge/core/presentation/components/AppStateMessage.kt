package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.extraLargeSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16

@Composable
fun AppStateMessage(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(extraLargeSpacing),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(spacing16))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(smallSpacing))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        onRetry?.let {
            Spacer(modifier = Modifier.height(spacing16))
            AppButton(
                text = stringResource(R.string.retry),
                icon = Icons.Rounded.Refresh,
                onClick = it
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyAppStatePreview() {
    AppTheme {
        AppStateMessage(
            icon = Icons.Rounded.LibraryMusic,
            title = stringResource(R.string.empty_state_title),
            subtitle = stringResource(R.string.empty_state_body)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorAppStatePreview() {
    AppTheme {
        AppStateMessage(
            icon = Icons.Rounded.ErrorOutline,
            title = stringResource(R.string.error_title),
            subtitle = stringResource(R.string.error_local_database),
            onRetry = {}
        )
    }
}