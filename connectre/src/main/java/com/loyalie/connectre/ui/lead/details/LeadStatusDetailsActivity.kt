package com.loyalie.connectre.ui.lead.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.LeadItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.developers.SelectDeveloperActivity
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_lead_status_details.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class LeadStatusDetailsActivity : BaseActivity(), View.OnClickListener {
    @Inject
    lateinit var picasso: Picasso


    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    private val leads = ArrayList<LeadItem>()
    lateinit var leadListAdapter: ProgramStatusAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: LeadStatusVM
    lateinit var programName: String
    lateinit var programImage: String
    lateinit var programDesc: String

    private var programId = ""


    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lead_status_details)
        programId = intent.getStringExtra(PROGRAM_ID)!!
        programName = intent.getStringExtra(NAME)!!
        programImage = intent.getStringExtra(IMAGE)!!
        programDesc = intent.getStringExtra(DESCRIPTION)!!
        viewModel = viewModelProvider(viewModelFactory)
        initToolbar()
        viewModel.getLeads(programId)

        initRV()

        initClickListener()

        observeVM()


    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        leadStatusToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.lead_status)
    }

    private fun initRV() {

        val layoutManager = LinearLayoutManager(this)
        detailsRV.layoutManager = layoutManager
        leadListAdapter = ProgramStatusAdapter(this, leads)
        detailsRV.adapter = leadListAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getLeads(programId, false, false)
            }
        }


        detailsRV.addOnScrollListener(scrollListener)

        if (programImage.isNullOrBlank()) leadDetailsIV.setImageResource(R.drawable.placeholder)
        else leadDetailsIV.loadUrl(programImage, picasso)
        programTitleTV.setText(programName)
        programSubTitleTV.setText(programDesc)

    }

    private fun observeVM() {
        viewModel.leadsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    leads.clear()
                    leads.addAll(it.data.first)
                    programName = it.data.second.first
                    programDesc = it.data.second.second
                    programImage = it.data.third
                    if (programImage.isNullOrBlank()) leadDetailsIV.setImageResource(R.drawable.placeholder)
                    else leadDetailsIV.loadUrl(programImage, picasso)
                    programTitleTV.setText(programName)
                    programSubTitleTV.setText(programDesc)
                    if (leads.isEmpty()) {
                        errorView.setVisible()
                    } else {
                        errorView.setGone()
                    }

                    loadingDialog.remove()
                    leadListAdapter.showLoading(false)
                    leadListAdapter.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    leadListAdapter.showLoading(false)
                    scrollListener.setLoading(false)
                    leadListAdapter.notifyDataSetChanged()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) leadListAdapter.showLoading(true)
                }
            }
        })
    }

    override fun onBackPressed() {
        if (intent.hasExtra(FROM_PUSH) && intent.getBooleanExtra(FROM_PUSH, false)) {
            startActivity(Intent(this, SelectDeveloperActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }.putExtra("From", "push"))
            finish()
        } else super.onBackPressed()
    }

    companion object {
        const val PROGRAM_ID = "program_id"
        const val DESCRIPTION = "description"
        const val NAME = "name"
        const val IMAGE = "image"
        fun start(context: Context, programId: String, name: String, desc: String, image: String) {
            context.startActivity(Intent(context, LeadStatusDetailsActivity::class.java).apply {
                putExtra(PROGRAM_ID, programId)
                putExtra(DESCRIPTION, desc)
                putExtra(NAME, name)
                putExtra(IMAGE, image)
            })
        }
    }
}
