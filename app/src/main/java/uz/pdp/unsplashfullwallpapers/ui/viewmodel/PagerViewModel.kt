package uz.pdp.unsplashfullwallpapers.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import uz.pdp.unsplashapp.retrofit.ApiClient
import uz.pdp.unsplashfullwallpapers.model.ImageModel
import uz.pdp.unsplashfullwallpapers.repository.PhotoDataSource

class PagerViewModel(private val topicId: String, private val order_by: String) : ViewModel() {

    val flow = Pager(PagingConfig(15)) {
        PhotoDataSource(topicId, order_by)
    }.flow.cachedIn(viewModelScope)
}