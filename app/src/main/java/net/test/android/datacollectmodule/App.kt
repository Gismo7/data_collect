package net.test.android.datacollectmodule

import android.app.Application
import net.test.android.datacollectmodule.di.AppComponent
import net.test.android.datacollectmodule.di.AppModule
import net.test.android.datacollectmodule.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)

    }
}