package com.example.baseandroid.view.main_activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.baseandroid.R
import com.example.baseandroid.application.base.BaseVMActivity
import com.example.baseandroid.application.base.BaseViewModel
import com.example.baseandroid.data.network.APIRequest
import com.example.baseandroid.data.network.getCommon
import com.example.baseandroid.databinding.ActivityMainTabbarBinding
import com.example.baseandroid.data.network.getShopInfo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseVMActivity<MainViewModel, ActivityMainTabbarBinding>() {

    override val viewModel: MainViewModel by viewModels()

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
    }

    override fun makeViewBinding() {
        super.makeViewBinding()
        binding = ActivityMainTabbarBinding.inflate(layoutInflater)
        navContainer = binding.navHostFragment.findNavController()
        viewModel.load()
    }

    override var rootDes: Int? = R.id.emptyFragment
}

@HiltViewModel
class MainViewModel @Inject constructor(val apiRequest: APIRequest) :
    BaseViewModel() {

    fun load() {
        viewModelScope.launch {
            apiRequest.getCommon()
                .catch { error ->
                    Log.d("test", error.message.toString())
                }
                .collect {
                    Log.d("test", it.toString())
                }
//                    .collectLatest {
//                        Log.d("daovu",it.toString())
//                    }
        }

    }
}



