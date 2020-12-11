package com.loyalie.connectre.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.NotificationItem
import com.loyalie.connectre.ui.notification.NotificationAdapter
import com.loyalie.connectre.ui.notification.NotificationVM
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.center_title_toolbar.*

class DashNotificationAct : BaseActivity(),View.OnClickListener {
    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

   /* @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
*/
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
        setContentView(R.layout.activity_dash_notification)
        initToolbar()

        initRV()

        initClickListener()
//        viewModel = viewModelFactory.create(NotificationVM::class.java)
        observeVM()

        /*swipeRL.setOnRefreshListener {
//            notifications.clear()
//            notificationsAdapter.notifyDataSetChanged()
//            scrollListener.reset(0, false)
//            viewModel.getNotification(isInitial = true, isRefresh = true)
        }*/
    }
    override fun onResume() {
        super.onResume()
//        viewModel.getNotification(isInitial = true, isRefresh = false)
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(this)
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
//                viewModel.getNotification(false, false)
            }
        }
        notifications.clear()
        notifications.add(NotificationItem("1","Notification1","Notification1","",0,"",0,"",""))
        notifications.add(NotificationItem("2","Notification2","Notification2","",0,"",0,"",""))
       var notificationsAdapter = NotificationAdapter(this, notifications, ::onNotificationItemClicked)
        notificationsRV.layoutManager = layoutManager
        notificationsRV.adapter = notificationsAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item!!)
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
companion object {
    fun start(context:Context){
    context.    startActivity(Intent(context, DashNotificationAct::class.java))
    }
}

    private fun observeVM() {

    }

    private fun onNotificationItemClicked(notification: NotificationItem, position:Int) {

        }
    }
