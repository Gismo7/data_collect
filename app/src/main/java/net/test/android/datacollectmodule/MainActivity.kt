package net.test.android.datacollectmodule

import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import net.test.android.datacollection.permissions.PermissionsUtils
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), DataCollectView {

    companion object {
        private const val REQUEST_CODE_INFO_PERMISSIONS = 1231
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: DataCollectPresenter

    @ProvidePresenter
    fun providePresenter(): DataCollectPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { presenter.onButtonClick() }
    }

    override fun showLoading(visible: Boolean) {
        button.visible(!visible)
        progress.visible(visible)
    }

    override fun showData(data: String) {
        textView.text = data
    }

    override fun checkPermission(permissions: List<String>) {
        if (permissions.isEmpty()) {
            presenter.collectData()
        } else {
            PermissionsUtils.requestPermissionsSafely(
                    activity = this,
                    permissions = permissions,
                    requestCode = REQUEST_CODE_INFO_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_INFO_PERMISSIONS) {
            //т.к. был запрос разрешений для сбора данных, то пробуем отправить те данные, которые получится
            presenter.collectData()
        }
    }

    override fun showMessage(message: String?) {
        Toast.makeText(baseContext,
                message ?: getString(R.string.smth_went_wrong),
                Toast.LENGTH_SHORT).show()
    }
}
