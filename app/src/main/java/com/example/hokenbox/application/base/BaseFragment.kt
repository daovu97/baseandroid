package com.example.hokenbox.application.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.hokenbox.view.main.MainViewModel
import com.example.hokenbox.view.registration.RegistrationViewModel

typealias MyFragment = BaseFragment<*>
typealias MyActivity = BaseActivity<*>

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    val fragmentScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }

    val permissionsResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.all { it.value }) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    lateinit var binding: B

    private var view: ViewBinding? = null

    var shouldReloadView: Boolean = false

    open fun setupView() {}

    open fun makeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (shouldReloadView) {
            makeViewBinding(inflater, container, savedInstanceState)
            setupView()
            return binding.root
        }
        if (view == null) {
            makeViewBinding(inflater, container, savedInstanceState)
            view = binding
            setupView()
        }

        return this.binding.root
    }

    open fun onPermissionGranted() {}
    open fun onPermissionDenied() {}

    override fun onDestroy() {
        super.onDestroy()
        fragmentScope.cancelCoroutines()
    }
}

abstract class BaseVMFragment<V : BaseViewModel, B : ViewBinding> : BaseFragment<B>() {
    abstract val viewModel: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isShowProgress.observe(this) { isShow ->
            (mActivity as? BaseVMActivity<*, *>)?.viewModel?.isShowProgress?.postValue(isShow)
        }
    }
}

val BaseFragment<*>.mActivity: MyActivity?
    get() = this.activity as? MyActivity

val BaseFragment<*>.mainViewModel: MainViewModel?
    get() = (mActivity as? BaseVMActivity<*, *>)?.viewModel as? MainViewModel

val BaseFragment<*>.registrationViewModel: RegistrationViewModel?
    get() = (mActivity as? BaseVMActivity<*, *>)?.viewModel as? RegistrationViewModel

fun BaseFragment<*>.pushTo(
    @IdRes resId: Int,
    args: Bundle? = null,
    anim: PUSH_TYPE = PUSH_TYPE.SLIDE
) {
    mActivity?.pushTo(resId, args, anim)
}

fun BaseFragment<*>.popTo(@IdRes destinationId: Int? = null, inclusive: Boolean = false) {
    mActivity?.popTo(destinationId, inclusive)
}

fun BaseFragment<*>.popToRoot() {
    mActivity?.popToRoot()
}