package com.geekbrains.tests.app

import android.app.Application
import com.geekbrains.tests.di.diModules
import org.koin.core.context.startKoin

class AndroidTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(diModules)
        }
    }
}
