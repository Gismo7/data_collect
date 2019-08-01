package net.test.android.datacollectmodule.di

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.test.android.datacollection.AbstractDataCollectManager
import net.test.android.datacollection.di.BaseSchedulerProvider
import net.test.android.datacollectmodule.App
import net.test.android.datacollectmodule.DataCollectManager
import net.test.android.datacollectmodule.MainActivity
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
}

@Module
class AppModule(private val appContext: Context) {

    @Singleton
    @Provides
    fun appContext(): Context = appContext

    @Provides
    @Singleton
    fun scheduler(): BaseSchedulerProvider {
        return object : BaseSchedulerProvider {
            override fun computation(): Scheduler = Schedulers.computation()

            override fun io(): Scheduler = Schedulers.io()

            override fun ui(): Scheduler = AndroidSchedulers.mainThread()
        }
    }

    @Provides
    @Singleton
    fun provideDataCollectManager(context: Context): AbstractDataCollectManager {
        return DataCollectManager(context)
    }
}