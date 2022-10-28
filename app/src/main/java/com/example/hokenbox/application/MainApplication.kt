package com.example.hokenbox.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
        lateinit var visibleActivity: AppCompatActivity
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
    }
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun dpFromPx(px: Int): Float {
    return px / Resources.getSystem().displayMetrics.density
}

