package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    @DrawableRes actionIcon: Int? = null,
    onBack: (() -> Unit)? = null,
    onAction: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {
        if (actionIcon != null && onAction != null) {
            IconButton(onClick = onAction) {
                Icon(
                    painter = painterResource(actionIcon),
                    contentDescription = null
                )
            }
        }
    }
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = titleStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }
            }
        },
        actions = actions,
        windowInsets = TopAppBarDefaults.windowInsets.add(WindowInsets(top = smallSpacing)),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SearchPreview() {
    AppTheme {
        AppTopBar(
            title = stringResource(R.string.songs),
            actionIcon = R.drawable.ic_search,
            onAction = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun NavigationPreview() {
    AppTheme {
        AppTopBar(
            title = stringResource(R.string.now_playing),
            titleStyle = MaterialTheme.typography.titleSmall,
            actionIcon = R.drawable.ic_more,
            onBack = {},
            onAction = {}
        )
    }
}