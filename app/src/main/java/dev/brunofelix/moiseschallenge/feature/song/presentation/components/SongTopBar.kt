package dev.brunofelix.moiseschallenge.feature.song.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.components.AppTopBar
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing

@Composable
internal fun SongTopBar(
    showSearchBar: Boolean,
    onShowSearchBarChange: (Boolean) -> Unit,
    onCancelSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppTopBar(
        title = stringResource(R.string.songs),
        modifier = modifier,
        actions = {
            AnimatedContent(
                targetState = showSearchBar,
                contentAlignment = Alignment.CenterEnd,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut() using SizeTransform(clip = false)
                }
            ) { isSearching ->
                if (isSearching) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = smallSpacing)
                            .clickable { onCancelSearch() }
                    )
                } else {
                    IconButton(onClick = { onShowSearchBarChange(true) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = stringResource(R.string.cd_search_button)
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        SongTopBar(
            showSearchBar = false,
            onShowSearchBarChange = {},
            onCancelSearch = {}
        )
    }
}