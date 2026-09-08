package dev.brunofelix.moiseschallenge.util.extension

import androidx.compose.runtime.Composable
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf

/**
 * Helper to create [CombinedLoadStates] for Compose Previews.
 */
fun createCombinedLoadStates(
    refresh: LoadState = LoadState.NotLoading(false),
    prepend: LoadState = LoadState.NotLoading(false),
    append: LoadState = LoadState.NotLoading(false)
): CombinedLoadStates {
    return CombinedLoadStates(
        refresh = refresh,
        prepend = prepend,
        append = append,
        source = LoadStates(refresh, prepend, append)
    )
}

/**
 * Extension to create [LazyPagingItems] directly from a List for Compose Previews.
 */
@Composable
fun <T : Any> List<T>.collectAsPreviewLazyPagingItems(
    refresh: LoadState = LoadState.NotLoading(false),
    append: LoadState = LoadState.NotLoading(false),
    prepend: LoadState = LoadState.NotLoading(false)
): LazyPagingItems<T> {
    val pagingData = PagingData.from(
        data = this,
        sourceLoadStates = LoadStates(
            refresh = refresh,
            append = append,
            prepend = prepend
        )
    )
    return flowOf(pagingData).collectAsLazyPagingItems()
}
