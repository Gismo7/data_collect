package net.test.android.datacollectmodule

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import net.test.android.datacollection.DataCollectInteractor
import net.test.android.datacollection.di.BaseSchedulerProvider
import net.test.android.datacollection.entities.MobileDevice
import javax.inject.Inject

@InjectViewState
class DataCollectPresenter @Inject constructor(private val dataCollectInteractor: DataCollectInteractor,
                                               private val schedulerProvider: BaseSchedulerProvider) :
        MvpPresenter<DataCollectView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showLoading(false)
    }

    fun collectData() {
        compositeDisposable.add(
                dataCollectInteractor.collectAndSendData()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .doOnSubscribe {
                            viewState.showLoading(false)
                            viewState.showData("")
                        }
                        .doFinally { viewState.showLoading(false) }
                        .subscribe(
                                { data ->
                                    viewState.showMessage("success")
                                    (data as? MobileDevice).let { viewState.showData(it.toString()) }
                                },
                                { th ->
                                    viewState.showMessage(th.message)
                                }
                        )
        )
    }

    fun onButtonClick() {
        viewState.checkPermission(dataCollectInteractor.getNotGrantedPermissions())
    }
}