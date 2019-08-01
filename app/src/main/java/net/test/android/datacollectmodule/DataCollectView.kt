package net.test.android.datacollectmodule

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DataCollectView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun checkPermission(permissions: List<String>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showMessage(message: String?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: String)
}