package uz.pdp.unsplashfullwallpapers.repository

import kotlinx.coroutines.flow.flow
import uz.pdp.unsplashapp.retrofit.ApiService

class PhotoRepository(private val apiService: ApiService) {

    fun getRandomPhoto(page: Int) = flow { emit(apiService.getRandomPhotos(page)) }

    fun getPhoto(topicId: String, page: Int, order_by: String) =
        flow { emit(apiService.getPhotos(topicId, page, order_by = order_by)) }
}