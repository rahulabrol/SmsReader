package com.rahulabrol.smsreader

import androidx.annotation.MainThread
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.rahulabrol.smsreader.base.LiveCoroutinesViewModel
import com.rahulabrol.smsreader.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Rahul Abrol on 14/3/21.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private var homeRepository: HomeRepository) :
    LiveCoroutinesViewModel() {

    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var toastLiveData: MutableLiveData<String> = MutableLiveData()

//    var postsLiveData: LiveData<List<Post>?>
    private var postMutualLiveData: MutableLiveData<Boolean> = MutableLiveData()

//    var postDetailLiveData: LiveData<PostDetail?>
    private var postDetailMutualLiveData: MutableLiveData<String> = MutableLiveData()

    init {
//        postsLiveData = postMutualLiveData.switchMap {
//            isLoading.set(it)
//            launchOnViewModelScope {
//                this.homeRepository.getPosts({ size ->
//                    isLoading.set(false)
//                }, {
//                    isLoading.set(false)
//                    toastLiveData.postValue(it)
//                }).asLiveData()
//            }
//        }

//        postDetailLiveData = postDetailMutualLiveData.switchMap {
//            isLoading.set(true)
//            launchOnViewModelScope {
//                this.homeRepository.getPostDetail(it, {
//                    isLoading.set(false)
//                }, {
//                    isLoading.set(false)
//                    toastLiveData.postValue(it)
//                }).asLiveData()
//            }
//        }
    }

    @MainThread
    fun getPostList() {
        postMutualLiveData.value = true
    }

    @MainThread
    fun getUserDetail(userId: String?) {
        postDetailMutualLiveData.value = userId
    }

}