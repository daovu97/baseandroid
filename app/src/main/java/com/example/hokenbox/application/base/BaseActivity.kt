package com.example.hokenbox.application.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.AnimBuilder
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.example.hokenbox.R
import com.example.hokenbox.application.MainApplication
import com.example.hokenbox.resource.customView.ProgressView

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    val activityScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }

    internal var progress: ProgressView? = null

    lateinit var binding: B

    var navContainer: NavController? = null

    @IdRes
    open val rootDes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.visibleActivity = this
        makeViewBinding()
        setContentView(binding.root)
        setupView(savedInstanceState)
    }

    open fun makeViewBinding() {}

    open fun setupView(savedInstanceState: Bundle?) {}

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancelCoroutines()
    }
}

abstract class BaseVMActivity<VM : BaseViewModel, B : ViewBinding> : BaseActivity<B>() {
    abstract val viewModel: VM

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        viewModel.isShowProgress.observe(this) { isShow ->
            showProgress(isShow)
        }
    }

    private fun showProgress(isShow: Boolean) {
        if (isShow) {
            if (progress == null) {
                progress = ProgressView()
            }
            if (progress?.isVisible == true) {
                return
            }
            progress?.show(supportFragmentManager, "")
        } else {
            progress?.dismiss()
            progress = null
        }
    }
}

enum class PUSH_TYPE(val anim: AnimBuilder) {
    NONE(AnimBuilder().apply {}),
    SLIDE(
        AnimBuilder().apply {
            enter = R.anim.enter_from_right
            exit = R.anim.exit_to_left
            popEnter = R.anim.enter_from_left
            popExit = R.anim.exit_to_right
        }
    ),
    FADE(AnimBuilder().apply {
        enter = R.anim.fade_in
        exit = R.anim.fade_out
    })
}

fun BaseActivity<*>.pushTo(@IdRes resId: Int, args: Bundle? = null, anim: PUSH_TYPE = PUSH_TYPE.SLIDE) {
    navContainer?.currentDestination?.getAction(resId)?.navOptions?.let {
        navContainer?.navigate(
            resId,
            args,
            navOptions {
                anim {
                    enter = anim.anim.enter
                    exit = anim.anim.exit
                    popEnter = anim.anim.popEnter
                    popExit = anim.anim.popExit
                }
                popUpTo(it.popUpToId) {
                    inclusive = it.isPopUpToInclusive()
                }
            }
        )
    }
}

fun BaseActivity<*>.popTo(@IdRes destinationId: Int? = null, inclusive: Boolean = false) {
    navContainer?.apply {
        if (destinationId == null) popBackStack()
        else popBackStack(destinationId, inclusive)
    }
}

fun BaseActivity<*>.popToRoot() {
    rootDes?.let { popTo(it, false) }
}
