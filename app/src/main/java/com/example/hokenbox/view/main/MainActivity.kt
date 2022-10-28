package com.example.hokenbox.view.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.example.hokenbox.R
import com.example.hokenbox.application.base.BaseVMActivity
import com.example.hokenbox.application.base.BaseViewModel
import com.example.hokenbox.application.base.hideProgress
import com.example.hokenbox.application.base.showProgress
import com.example.hokenbox.data.local.Settings
import com.example.hokenbox.data.network.APIRequest
import com.example.hokenbox.data.network.getCommon
import com.example.hokenbox.databinding.ActivityMainTabbarBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseVMActivity<MainViewModel, ActivityMainTabbarBinding>() {

    override val viewModel: MainViewModel by viewModels()

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        navContainer =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController
    }

    override fun makeViewBinding() {
        super.makeViewBinding()
        binding = ActivityMainTabbarBinding.inflate(layoutInflater)
        viewModel.load()
    }

    override val rootDes: Int = R.id.emptyFragment
}

@HiltViewModel
class MainViewModel @Inject constructor(val apiRequest: APIRequest) :
    BaseViewModel() {

    fun load() {
        viewModelScope.launch {
            //  showProgress()
//            apiRequest.getCommon()
//                .catch { error ->
//                    Log.d("test", error.message.toString())
//                }
//                .collect {
//                    hideProgress()
//                    Log.d("test", it.toString())
//                }
//                    .collectLatest {
//                        Log.d("daovu",it.toString())
//                    }
        }

    }
}



