package com.example.challenge.ui.blog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge.data.repository.PostRepositoryImpl
import com.example.challenge.domain.model.Post
import com.example.challenge.utils.SingleLiveEvent
import com.example.challenge.utils.onFailure
import com.example.challenge.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Handle UI related data for [BlogFragment]
 * @property isLoading MutableLiveData<Boolean> handle loading
 * @property isError MutableLiveData<Boolean> handle error
 * @property postList MutableLiveData<List<Post>> handle articles
 * @property state SingleLiveEvent<BlogFragmentState> handle fragment actions
 */
class BlogsViewModel : ViewModel() {
    val isLoading = MutableLiveData(false)
    val isError = MutableLiveData(false)
    val postList = MutableLiveData<List<Post>>(listOf())
    val state =  SingleLiveEvent<BlogFragmentState>()

    fun loadData(){
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            PostRepositoryImpl().getPosts()
                .onSuccess {
                    postList.postValue(it)
                    isLoading.postValue(false)
                }
                .onFailure {
                    isError.postValue(true)
                    isLoading.postValue(false)
                }
        }
    }

    fun openDetail(url : String){
        state.postValue(BlogFragmentState.OpenDetailView(url))
    }


}