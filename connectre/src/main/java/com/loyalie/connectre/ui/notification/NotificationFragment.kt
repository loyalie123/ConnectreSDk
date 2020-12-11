package com.loyalie.connectre.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.NotificationItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.fcm.PushNotification
import com.loyalie.connectre.ui.lead.details.LeadStatusDetailsActivity
import com.loyalie.connectre.ui.offer.OfferDetailsActivity
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import kotlinx.android.synthetic.main.activity_notification.*
import javax.inject.Inject


class NotificationFragment : BaseFragment() {
    private val loadingDialog by lazy {
        LoadingDialog(context!!)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val notifications = ArrayList<NotificationItem>()
    lateinit var notificationsAdapter: NotificationAdapter
    lateinit var scrollListener: PaginatingScrollListener

    lateinit var viewModel: NotificationVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_notification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRV()


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

    override fun setUserVisibleHint(isVisibleToUser: Boolean): Unit {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            viewModel.getNotification(isInitial = true, isRefresh = false)
        }
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(context!!)
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getNotification(false, false)
            }
        }
        notificationsAdapter =
            NotificationAdapter(context!!, notifications, ::onNotificationItemClicked)
        notificationsRV.layoutManager = layoutManager
        notificationsRV.adapter = notificationsAdapter
        notificationsRV.addOnScrollListener(scrollListener)
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

        viewModel.readHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    notifications[it.position].read_status=1
//                    viewModel.getNotification(isInitial = true, isRefresh = false)
                    notificationsAdapter.notifyItemChanged(it.position)

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

    private fun onNotificationItemClicked(notification: NotificationItem,position:Int) {
        if (notification.read_status == 0) viewModel.changeStatus(notification.push_id,position)

        when (notification.type) {
            PushNotification.OFFER_TYPE -> {
                startActivity(Intent(context!!, OfferDetailsActivity::class.java).apply {
                    putExtra(OfferDetailsActivity.PROGRAM_ID, notification.prog_id)
                    putExtra(OfferDetailsActivity.REFERAL_LINK, "")
                    putExtra(OfferDetailsActivity.PROGRAM_NAME, "")
                    putExtra(OfferDetailsActivity.PROGRAM_DESC, "")
                }
                )
            }


            PushNotification.LEAD_ADDED_TYPE -> {
                startActivity(Intent(context!!, LeadStatusDetailsActivity::class.java).apply {
                    putExtra(LeadStatusDetailsActivity.PROGRAM_ID, notification.prog_id)
                    putExtra(LeadStatusDetailsActivity.DESCRIPTION, "")
                    putExtra(LeadStatusDetailsActivity.NAME, "")
                    putExtra(LeadStatusDetailsActivity.IMAGE, "")
                })

            }
        }
    }

    companion object {

        fun newInstance() = NotificationFragment()
    }
}
