package com.loyalie.connectre.ui.faq

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.FaqItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.Utils
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class FAQActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: FAQVM

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
        setContentView(R.layout.activity_faq)

        initToolbar()

        observeVM()
        viewModel.getFaq()

        initClickListener()
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        faqToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.faqs)
    }

    private fun observeVM() {
        viewModel.faqHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    initRV(it.data)
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

    private fun initRV(data: List<FaqItem>) {
        faqRV.layoutManager = LinearLayoutManager(this)
        faqRV.adapter = FAQAdapter(this, data, false)
    }

}
