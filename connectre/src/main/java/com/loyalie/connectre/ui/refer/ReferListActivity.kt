package com.loyalie.connectre.ui.refer

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
import com.loyalie.connectre.data.ProgramItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_refer_list.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class ReferListActivity : BaseActivity(), View.OnClickListener {
    @Inject
    lateinit var picasso: Picasso

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    private val programs = ArrayList<ProgramItem>()
    lateinit var programsAdapter: ReferListAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var viewModel: ReferListVM
    lateinit var vendorId: String


    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_list)
        vendorId = intent.getStringExtra(VENDOR_ID)!!
        viewModel = viewModelProvider(viewModelFactory)
        initToolbar()
        initRV()
        viewModel.getPrograms(vendorId)

        initClickListener()
        observeVM()

        swipeRL.setOnRefreshListener {
            programs.clear()
            programsAdapter.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getPrograms(vendorId, isInitial = true, isRefresh = true)
        }
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        referToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.programs)
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(this)
        referRV.layoutManager = layoutManager
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getPrograms(vendorId, false, false)
            }
        }
        programsAdapter = ReferListAdapter(this, programs, picasso)
        referRV.adapter = programsAdapter
        referRV.addOnScrollListener(scrollListener)

    }

    private fun observeVM() {
        viewModel.programHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    programs.clear()
                    programs.addAll(it.data)

                    if (programs.isEmpty()) {
                        errorView.setVisible()
                    } else {
                        errorView.setGone()
                    }

                    loadingDialog.remove()
                    programsAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    programsAdapter.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    programsAdapter.showLoading(false)
                    scrollListener.setLoading(false)
                    swipeRL.isRefreshing = false
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) programsAdapter.showLoading(true)
                }
            }
        })
    }

    companion object {
        const val VENDOR_ID = "vendor_id"
        fun start(context: Context, vendorId: String) {
            context.startActivity(Intent(context, ReferListActivity::class.java).apply {
                putExtra(VENDOR_ID, vendorId)
            })

        }
    }
}
