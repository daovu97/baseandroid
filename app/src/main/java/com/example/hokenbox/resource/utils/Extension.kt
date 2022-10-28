package com.example.hokenbox.resource.utils

import android.animation.Animator
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.hokenbox.R
import com.example.hokenbox.application.MainApplication
import com.example.hokenbox.application.base.MyActivity
import com.example.hokenbox.application.base.MyFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun MyFragment.delay(
    timeMillis: Long, execute: () -> Unit
) {
    fragmentScope.launch {
        kotlinx.coroutines.delay(timeMillis)
        execute()
    }
}

fun MyActivity.delay(
    timeMillis: Long, execute: () -> Unit
) {
    activityScope.launch {
        kotlinx.coroutines.delay(timeMillis)
        execute()
    }
}

fun View.stopAnimation() {
//    this.animation.cancel()
}

fun View.visible() {
    this.visibility = View.VISIBLE
    this.isEnabled = true
}

fun View.hidden() {
    this.visibility = View.INVISIBLE
    this.isEnabled = false
}

fun View.gone() {
    this.visibility = View.GONE
    this.isEnabled = false
}

fun View.animeFade(isShow: Boolean, duration: Long = 0) {
    if (isShow == isVisible) {
        return
    }
    val toAlpha = if (isShow) 1f else 0f
    this.visible()
    this.alpha = if (isShow) 0f else 1f
    animate()
        .alpha(toAlpha)
        .setDuration(duration)
        .setComplete { if (isShow) visible() else gone() }
        .start()
}

fun Context.showSingleActionAlert(
    title: String, message: String,
    actionTitle: String = "OK",
    completion: () -> Unit = {}
) {
    androidx.appcompat.app.AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(actionTitle) { _, _ ->
            completion()
        }
        .setCancelable(false)
        .create()
        .apply {
            setCanceledOnTouchOutside(false)
            show()
        }
}

fun Context.showTwoActionAlert(
    title: String, message: String,
    positiveTitle: String = "OK",
    negativeTitle: String = "Cancel",
    positiveAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null,
    isDestruction: Boolean = false,
) {
    CoroutineScope(Dispatchers.Main).launch {
        androidx.appcompat.app.AlertDialog.Builder(
            this@showTwoActionAlert
        )
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveTitle) { _, _ ->
                positiveAction?.let { it() }
            }
            .setNegativeButton(negativeTitle) { _, _ ->
                negativeAction?.let { it() }
            }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }
}

suspend fun Context.showTwoActionAlert(
    title: String, message: String,
    positiveTitle: String = "OK",
    negativeTitle: String = "Cancel"
) = suspendCoroutine<Boolean> { continuation ->
    showTwoActionAlert(title, message, positiveTitle, negativeTitle, positiveAction = {
        continuation.resume(true)
    }, negativeAction = { continuation.resume(false) })
}


fun View.jumping(translationY: Float = 20F, duration: Long, loop: Boolean = true) {
    animate()
        .translationY(translationY)
        .setDuration(duration / 2)
        .setComplete {
            animate()
                .translationY(-translationY)
                .setDuration(1500L)
                .setComplete {
                    if (loop) {
                        jumping(translationY, duration, loop)
                    }
                }
        }
}


//fun <T> Flow<T>.catchRetry(
//    viewModel: BaseViewModel,
//    cancel: suspend FlowCollector<T>.() -> Unit = {},
//    retry: suspend FlowCollector<T>.(Throwable) -> Unit
//): Flow<T> {
//    return catch { error ->
//        viewModel.hideProgress()
//        if (error is NoConnectivityException) {
//            if (MainApplication.CONTEXT.showTwoActionAlert(
//                    MainApplication.CONTEXT.getString(R.string.errorTitle),
//                    MainApplication.CONTEXT.getString(R.string.defaultErrorTitle),
//                    "Retry",
//                    MainApplication.CONTEXT.getString(R.string.cancelTitle)
//                )
//            ) {
//                retry(error)
//            } else {
//                cancel()
//            }
//        } else if (error.httpCode() == HTTPError.SERVER_ERROR.code) {
//            MainApplication.CONTEXT.showServerError()
//        } else {
//            if (error is HttpException) {
//                error.response()?.errorBody()?.string()?.let {
//                    val errorResponse: ErrorResponse = Gson().fromJson(it)
//                    errorResponse.message?.let {
//                        MainApplication.CONTEXT.showSingleActionAlert(
//                            MainApplication.CONTEXT.getString(
//                                R.string.errorTitle
//                            ), errorResponse.message
//                        ) {}
//                    } ?: MainApplication.CONTEXT.showDefaultAlert()
//                } ?: MainApplication.CONTEXT.showDefaultAlert()
//            } else {
//                MainApplication.CONTEXT.showDefaultAlert()
//            }
//        }
//    }
//}

fun View.toggleSelected() {
    isSelected = !isSelected
}

fun View.toggleVisible() {
    if (isVisible) {
        gone()
    } else {
        visible()
    }
}

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.string(pattern: String = "yyyy-MM-dd HH:mm"): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

fun Context.hasPermissions(permissions: Array<String>): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

fun MyFragment.checkPermission(
    perms: Array<String>
) {
    if (requireContext().hasPermissions(perms)) {
        onPermissionGranted()
    } else {
        permissionsResult.launch(perms)
    }
}

fun ViewPropertyAnimator.setComplete(completion: (Animator?) -> Unit): ViewPropertyAnimator {
    return setListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(p0: Animator) {

        }

        override fun onAnimationEnd(p0: Animator) {
            completion(p0)
        }

        override fun onAnimationCancel(p0: Animator) {

        }

        override fun onAnimationRepeat(p0: Animator) {

        }
    })
}

fun View.setPaddingAsDP(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    setPadding(asPixels(left), asPixels(top), asPixels(right), asPixels(bottom))
}

fun View.asPixels(value: Int): Int {
    val scale = resources.displayMetrics.density
    val dpAsPixels = (value * scale + 0.5f)
    return dpAsPixels.toInt()
}