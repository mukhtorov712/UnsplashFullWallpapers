package uz.pdp.unsplashfullwallpapers.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import uz.pdp.unsplashapp.retrofit.ApiClient
import uz.pdp.unsplashfullwallpapers.model.ImageModel

class RandomDataSource : PagingSource<Int, ImageModel>() {

    private val photoRepository = PhotoRepository(ApiClient.apiService)

    override fun getRefreshKey(state: PagingState<Int, ImageModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        try {
            val currentPage = params.key ?: 1

            var loadResult: LoadResult.Page<Int, ImageModel>? = null

            if (params.key ?: 1 >= 1) {
                photoRepository.getRandomPhoto(currentPage)
                    .catch {
                        loadResult = LoadResult.Page(
                            emptyList(),
                            currentPage - 1, currentPage + 1
                        )
                    }.collect {
                        loadResult =
                            LoadResult.Page(
                                it.body()!!,
                                currentPage - 1, currentPage + 1
                            )
                    }
            } else {
                loadResult =
                    LoadResult.Page(
                        emptyList(),
                        null, currentPage + 1
                    )
            }
            return loadResult!!
        } catch (e: Exception) {
            return LoadResult.Error(e.fillInStackTrace())
        }
    }

}