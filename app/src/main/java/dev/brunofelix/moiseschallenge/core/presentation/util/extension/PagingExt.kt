package dev.brunofelix.moiseschallenge.core.presentation.util.extension

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

/**
 * Extension function to create a [Flow] of [PagingData] from a [PagingConfig].
 *
 * @param T The type of data being paged.
 * @param pagingSourceFactory A lambda that creates the [PagingSource] for the pager.
 * @return A [Flow] emitting [PagingData].
 */
fun <T : Any> PagingConfig.asPagerFlow(
    pagingSourceFactory: () -> PagingSource<Int, T>
): Flow<PagingData<T>> {
    return Pager(
        config = this,
        pagingSourceFactory = pagingSourceFactory
    ).flow
}
