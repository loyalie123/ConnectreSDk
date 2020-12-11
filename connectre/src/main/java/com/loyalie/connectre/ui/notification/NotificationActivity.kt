package com.loyalie.connectre.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.NotificationItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.fcm.PushNotification
import com.loyalie.connectre.ui.lead.details.LeadStatusDetailsActivity
import com.loyalie.connectre.ui.offer.OfferDetailsActivity
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class NotificationActivity : BaseActivity(), View.OnClickListener {

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val notifications = ArrayList<NotificationItem>()
    lateinit var notificationsAdapter: NotificationAdapter
    lateinit var scrollListener: PaginatingScrollListener

    lateinit var viewModel: NotificationVM
    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initToolbar()

        initRV()

        initClickListener()
        viewModel = viewModelFactory.create(NotificationVM::class.java)
        observeVM()

        swipeRL.setOnRefreshListener {
            notifications.clear()
            notificationsAdapter.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getNotification(isInitial = true, isRefresh = true)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotification(isInitial = true, isRefresh = false)
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(this)
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getNotification(false, false)
            }
        }
        notificationsAdapter = NotificationAdapter(this, notifications, ::onNotificationItemClicked)
        notificationsRV.layoutManager = layoutManager
        notificationsRV.adapter = notificationsAdapter
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
        notificationToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.notifications)
    }


    private fun observeVM() {
        viewModel.notificationHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    notifications.clear()
                    notifications.addAll(it.data)

                    if (notifications.isEmpty()) {
                        errorView.setVisible()
                    } else {
                        errorView.setGone()
                    }

                    loadingDialog.remove()
                    notificationsAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    notificationsAdapter.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    notificationsAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) notificationsAdapter.showLoading(true)
                }
            }
        })
    }

    private fun onNotificationItemClicked(notification: NotificationItem,position:Int) {
        if (notification.read_status == 0) viewModel.changeStatus(notification.push_id, position)

        when (notification.type) {
            PushNotification.OFFER_TYPE -> {
                startActivity(Intent(this, OfferDetailsActivity::class.java).apply {
                    putExtra(OfferDetailsActivity.PROGRAM_ID, notification.prog_id)
                    putExtra(OfferDetailsActivity.REFERAL_LINK, "")
                    putExtra(OfferDetailsActivity.PROGRAM_NAME, "")
                    putExtra(OfferDetailsActivity.PROGRAM_DESC, "")
                }
                )
            }


            PushNotification.LEAD_ADDED_TYPE -> {
                startActivity(Intent(this, LeadStatusDetailsActivity::class.java).apply {
                    putExtra(LeadStatusDetailsActivity.PROGRAM_ID, notification.prog_id)
                    putExtra(LeadStatusDetailsActivity.DESCRIPTION, "")
                    putExtra(LeadStatusDetailsActivity.NAME, "")
                    putExtra(LeadStatusDetailsActivity.IMAGE, "")
                })

            }
        }
    }
}
