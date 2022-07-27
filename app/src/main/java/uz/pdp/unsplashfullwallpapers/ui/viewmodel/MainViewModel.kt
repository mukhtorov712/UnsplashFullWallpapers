package uz.pdp.unsplashfullwallpapers.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.pdp.unsplashapp.retrofit.ApiClient
import uz.pdp.unsplashfullwallpapers.room.entity.TopicModel

class MainViewModel : ViewModel() {

    fun fetchTopics(): LiveData<List<TopicModel>> {
        val liveData = MutableLiveData<List<TopicModel>>()
        viewModelScope.launch {
            val response = ApiClient.apiService.getTopics()
            if (response.isSuccessful) {
                liveData.value = response.body()
            }

        }
        return liveData
    }
}