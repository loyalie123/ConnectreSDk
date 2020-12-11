package com.loyalie.connectre.ui.event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.EventItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import kotlinx.android.synthetic.main.event_applied_bottom_sheet.*
import javax.inject.Inject


class EventActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: EventVM

    @Inject
    lateinit var picasso: Picasso

    lateinit var eventItem: EventItem

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }

            R.id.appliedBtn -> {
                if (eventItem.userStatus == 3) {
                    viewModel.applyEvent(eventItem.eventId)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        eventItem = intent.getSerializableExtra(EVENT_ITEM) as EventItem
        viewModel = viewModelProvider(viewModelFactory)
        initToolbar()

        initClickListener()


        val imageUrl = eventItem.eventImage

        val imageTransitionName = intent.extras?.getString(EVENT_TRANSITION)
        eventIV.setTransitionName(imageTransitionName)


        picasso
            .load(imageUrl)
            .fit().centerCrop()
            .noFade()
            .into(eventIV, object : Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError(e: Exception?) {
                    startPostponedEnterTransition()
                }
            })

//        if (!eventItem.eventImage.isNullOrBlank())
//            picasso.load(eventItem.eventImage!!).fit().centerCrop()
//                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(eventIV)
//        else eventIV.setInvisible()

        observeVM()

        eventTitleTV.setText(eventItem.eventName)
        dateTV.setText(eventItem.eventStartDate.convertToMMMDDYYY() + " - " + eventItem.eventEndDate.convertToMMMDDYYY())
        descriptionTV.setText(eventItem.eventLocation)
        subDescriptionTV.setText(eventItem.eventDescription)
        if (eventItem.userStatus == 0) {
            appliedTV.setText(R.string.pending)
        } else if (eventItem.userStatus == 1) {
            appliedTV.setText(R.string.accepted)
            applyTickIV.visibility = View.VISIBLE
        } else if (eventItem.userStatus == 2) {
            appliedTV.setText(R.string.rejected)
        } else {
            appliedBtn.setOnClickListener(this)
        }
    }

    private fun showBottomSheet() {

        BottomSheetDialog(this).apply {
            setContentView(R.layout.event_applied_bottom_sheet)
            eventOkBtn.setTextColor(ConnectReApp.themeColor)
            eventOkBtn.setOnClickListener { dismiss() }
            show()
        }
    }

    private fun observeVM() {
        viewModel.statusHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    if (it.data.eventStatus == 0) {
                        eventItem.userStatus = it.data.eventStatus
                        showBottomSheet()
                        appliedTV.setText(R.string.pending)
                    } else if (it.data.eventStatus == 1) {
                        appliedTV.setText(R.string.accepted)
                        applyTickIV.visibility = View.VISIBLE
                        "Event applied successfully".toast(this)
                    } else if (it.data.eventStatus == 2) {
                        appliedTV.setText(R.string.rejected)
                    } else {
                        appliedBtn.setOnClickListener(this)
                    }
                    setResult(Activity.RESULT_OK)
                    loadingDialog.dismiss()

                }
                is ViewState.Error -> {
                    loadingDialog.dismiss()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })
    }


    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    companion object {
        const val EVENT_ITEM = "event_item"
        const val EVENT_TRANSITION = "event_transition"
        const val EVENT_REQUEST = 121
        fun start(activity: Activity, event: EventItem, v: View) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                v,
                v.transitionName
            )


            activity.startActivityForResult(Intent(activity, EventActivity::class.java).apply {
                putExtra(EVENT_ITEM, event)
                putExtra(EVENT_TRANSITION, ViewCompat.getTransitionName(v))
            }, EVENT_REQUEST, options.toBundle())
        }
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        eventToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = "Events"
        appliedBtn.setBackgroundColor(extractedColor)
        eventOkBtn.setTextColor(extractedColor)

    }
}
