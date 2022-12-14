package uz.pdp.unsplashapp.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import uz.pdp.unsplashapp.retrofit.ApiClient.ACCESS_KEY
import uz.pdp.unsplashfullwallpapers.model.ImageModel
import uz.pdp.unsplashfullwallpapers.room.entity.TopicModel

interface ApiService {

    @Headers("Authorization: Client-ID $ACCESS_KEY")
    @GET("topics/{id}/photos")
    suspend fun getPhotos(
        @Path("id") topicId: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15,
        @Query("orientation") orientation: String = "portrait",
        @Query("order_by") order_by: String
    ): Response<List<ImageModel>>


    @Headers("Authorization: Client-ID $ACCESS_KEY")
    @GET("topics")
    suspend fun getTopics(
    ): Response<List<TopicModel>>

    @Headers("Authorization: Client-ID $ACCESS_KEY")
    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("page") page: Int,
        @Query("orientation") orientation: String = "portrait",
        @Query("count") count: Int = 15
    ): Response<List<ImageModel>>

    @Headers("Authorization: Client-ID $ACCESS_KEY")
    @GET("search/photos")
    suspend fun getSearchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("orientation") orientation: String = "portrait",
        @Query("per_page") perPage: Int = 30
    ): Response<List<ImageModel>>
}