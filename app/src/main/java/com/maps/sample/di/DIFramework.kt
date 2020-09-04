package com.maps.sample.di

import android.content.Context
import com.maps.sample.utils.MapUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * The dependency injection framework used by the app.
 * uses Koin for DI.
 * @author Malik Dawar
 */
object DIFramework {

    fun init(context: Context) {

        val repoModule = module {
            single { MapUtils.getInstance() }
        }


        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(context)
            // declare modules
            modules(repoModule)
        }
    }
}