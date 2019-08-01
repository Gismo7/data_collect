package net.test.android.datacollection

import io.reactivex.Observable
import io.reactivex.Single
import net.test.android.datacollection.di.BaseSchedulerProvider
import net.test.android.datacollection.entities.MobileDevice
import javax.inject.Inject

class DataCollectInteractor @Inject constructor(private val dataCollectManager: AbstractDataCollectManager,
                                                private val schedulerProvider: BaseSchedulerProvider) {

    fun getNotGrantedPermissions(): List<String> = dataCollectManager.getNotGrantedPermissions()

    fun collectAndSendData(): Single<Any?> {
        return Single.just(MobileDevice())
                .observeOn(schedulerProvider.ui())
                .flatMap { device ->
                    Observable.just(dataCollectManager.infoProviders)
                            .flatMapIterable { list -> list }
                            .flatMap { provider ->
                                provider.putInfo(device).toObservable()
                            }
                            .last(device)
                }
    }
}