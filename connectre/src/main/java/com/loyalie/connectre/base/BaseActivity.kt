package com.loyalie.connectre.base

import com.loyalie.connectre.R
import com.loyalie.connectre.data.ApiException
import com.loyalie.connectre.data.PreferenceStorage
import com.loyalie.connectre.util.logout
import com.loyalie.connectre.util.toast
import com.loyalie.connectre.widget.Toasty
import dagger.android.support.DaggerAppCompatActivity
import java.net.*
import javax.inject.Inject

open class BaseActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var preferenceStorage: PreferenceStorage


    // shows error description toast and returns if network error, handles authentication error
    fun onApiError(e: Throwable): Boolean {
        when (e) {
            is UnknownHostException -> getString(R.string.no_interenet).toast(this)
            is SocketTimeoutException -> getString(R.string.no_interenet).toast(this)
            is ConnectException -> getString(R.string.no_interenet).toast(this)
            is NoRouteToHostException -> getString(R.string.no_interenet).toast(this)
            is SocketException -> getString(R.string.no_interenet).toast(this)
            is ApiException -> {
                if (e.code == 2) logout(preferenceStorage, true)
                else Toasty(this, true).toast(e.description)
                return false
            }
            else -> {
                getString(R.string.un_expected_error).toast(this)
                return false
            }
        }
        return true

    }

}