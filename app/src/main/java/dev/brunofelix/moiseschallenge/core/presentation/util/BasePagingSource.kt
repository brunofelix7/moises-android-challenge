package dev.brunofelix.moiseschallenge.core.presentation.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.fold

/**
 * Base implementation of a PagingSource.
 * @param pageSize The number of items to fetch per page.
 * @param fetch A function that fetches the data for the given page.
 * @return A PagingSource that can be used with a Jetpack Compose LazyColumn.
 */
class BasePagingSource<T : Any>(
    private val pageSize: Int = 20,
    private val fetch: suspend (Int) -> Resource<List<T>>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return fetch(page).fold(
            onSuccess = { data ->
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size < pageSize) null else page + 1
                )
            },
            onFailure = { cause ->
                LoadResult.Error(cause)
            }
        )
    }
}