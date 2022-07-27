package uz.pdp.unsplashfullwallpapers.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import kotlinx.coroutines.launch
import uz.pdp.unsplashapp.retrofit.ApiClient
import uz.pdp.unsplashfullwallpapers.model.ImageModel


class SearchViewModel : ViewModel() {

    fun fetchSearch(query: String): LiveData<List<ImageModel>> {
        val liveData = MutableLiveData<List<ImageModel>>()
        viewModelScope.launch {
            val response = ApiClient.apiService.getSearchPhotos(query)
            if (response.isSuccessful) {
                liveData.value = response.body()
            }

        }
        return liveData
    }
}