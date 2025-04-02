package com.kirwa.dogsbreedsapp.utils

import android.graphics.pdf.LoadParams
import androidx.paging.PagingSource
import androidx.paging.PagingState

class TestPagingSource<T : Any>(private val data: List<T>) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 0
        val fromIndex = page * params.loadSize
        val toIndex = (fromIndex + params.loadSize).coerceAtMost(data.size)
        val pageData = if (fromIndex < data.size) data.subList(fromIndex, toIndex) else emptyList()

        return LoadResult.Page(
            data = pageData,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (toIndex < data.size) page + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.anchorPosition
}
