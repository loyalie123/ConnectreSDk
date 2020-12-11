package com.loyalie.connectre.ui.tutorial

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.TutorialItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tutorial_listing.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class TutorialListingActivity : BaseActivity(), View.OnClickListener {

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val tutorialsList = ArrayList<TutorialItem>()
    lateinit var tutorialsAdap: TutorialListAdapter
    lateinit var scrollListener: PaginatingScrollListener

    lateinit var viewModel: TutorialsVM

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_listing)

        initToolbar()

        initRV()

        initClickListener()
        viewModel = viewModelProvider(viewModelFactory)
        observeVM()
        viewModel.getTutorials(isInitial = true, isRefresh = false)

        swipeRL.setOnRefreshListener {
            tutorialsList.clear()
            tutorialsAdap.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getTutorials(isInitial = true, isRefresh = true)
        }
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(this)
        tutorialsAdap = TutorialListAdapter(this, picasso, tutorialsList)
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getTutorials(false, false)
            }
        }
        tutorialsRV.layoutManager = layoutManager
        tutorialsRV.adapter = tutorialsAdap
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
        tutorialToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.app_tutorials)
    }


    private fun observeVM() {
        viewModel.tutorialsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    tutorialsList.clear()
                    tutorialsList.addAll(it.data.AppTutorials)

                    if (tutorialsList.isEmpty()) {
                        errorView.setVisible()
                    } else {
                        errorView.setGone()
                    }

                    loadingDialog.remove()
                    tutorialsAdap.showLoading(false)
                    swipeRL.isRefreshing = false
                    tutorialsAdap.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    tutorialsAdap.showLoading(false)
                    scrollListener.setLoading(false)
                    swipeRL.isRefreshing = false
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) tutorialsAdap.showLoading(true)
                }
            }
        })
    }
}
