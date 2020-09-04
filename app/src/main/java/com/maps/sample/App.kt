package com.maps.sample

import android.app.Application
import android.content.Context
import com.maps.sample.di.DIFramework

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        //initialized KOIN, DI framework
        DIFramework.init(this)
    }

    companion object {
        var instance: App? = null
        fun getAppContext(): Context {
            return instance as Context
        }
    }
}