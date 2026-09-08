package dev.brunofelix.moiseschallenge.util

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * A PagingSource that paginates a local list of items.
 * Useful for APIs that don't support server-side pagination but return a large enough batch.
 */
class LocalPagingSource<T : Any>(
    private val items: List<T>,
    private val pageSize: Int = 20
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, items.size)

        return try {
            if (startIndex >= items.size) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = null
                )
            } else {
                val pagedData = items.subList(startIndex, endIndex)
                LoadResult.Page(
                    data = pagedData,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (endIndex < items.size) page + 1 else null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
