package com.example.hokenbox.application.base

import androidx.lifecycle.ViewModel
import com.example.hokenbox.resource.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    val isShowProgress = SingleLiveEvent<Boolean>()

}

fun BaseViewModel.showProgress() {
    if (isShowProgress.value == true) {
        return
    }
    isShowProgress.postValue(true)
}

fun BaseViewModel.hideProgress() {
    if (isShowProgress.value == false) {
        return
    }
    isShowProgress.postValue(false)
}