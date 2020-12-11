package com.loyalie.connectre.ui.privacy_policy

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.Utils
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class PrivacyPolicyActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: PrivacyVM

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        initToolbar()

        viewModel.getPrivacyPolicy()
        observeVM()
        initClickListener()

    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        privacyPolicyToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.privacy_policy)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    private fun observeVM() {
        viewModel.privacyHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    privacyPolicyWebview.loadUrl(it.data.privacyPolicy)
                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.load()
                }
            }
        })
    }
}
