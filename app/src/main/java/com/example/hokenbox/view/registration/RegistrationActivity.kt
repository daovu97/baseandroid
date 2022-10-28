package com.example.hokenbox.view.registration

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.hokenbox.R
import com.example.hokenbox.application.base.BaseVMActivity
import com.example.hokenbox.application.base.BaseViewModel
import com.example.hokenbox.data.local.Settings
import com.example.hokenbox.data.network.APIRequest
import com.example.hokenbox.databinding.ActivityMainTabbarBinding
import com.example.hokenbox.databinding.ActivityRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : BaseVMActivity<RegistrationViewModel, ActivityRegistrationBinding>() {

    override val viewModel: RegistrationViewModel by viewModels()

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        navContainer =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController
    }

    override fun makeViewBinding() {
        super.makeViewBinding()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)

        Settings.ACCESS_TOKEN.put("jvbsdkjvbjskdnbvkjsdn")
        val token = Settings.ACCESS_TOKEN.get("")
        Log.d("daovu", token)
    }

    override val rootDes: Int = R.id.emptyFragment
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(val apiRequest: APIRequest) :
    BaseViewModel() {

}
