package com.loyalie.connectre.ui.feedback

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject


class FeedbackActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: FeedbackVM

    lateinit var vendorId: String

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
        setContentView(R.layout.activity_feedback)
        viewModel = viewModelProvider(viewModelFactory)
        vendorId = intent.getStringExtra(VENDOR_ID)!!
        initToolbar()
        initClickListener()
        setRating()
        submitBtn.setOnClickListener {
            if (feedbackET.getTrimText().isNullOrEmpty()) {
                "Please fill the comment".toast(this)
            } else
                viewModel.sendFeedback(
                    vendorId,
                    feedbackET.getTrimText(),
                    feedbackRB.rating.toInt().toString()
                )
        }

        viewModel.successHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.dismiss()
                    "Thank you for your feedback, our representative will contact you shortly".toast(
                        this
                    )
                    finish()
                }
                is ViewState.Error -> {
                    loadingDialog.dismiss()
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })


    }

    private fun setRating() {
        feedbackRB.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (rating <= 1)
                    feedbackTV.text = "Very Bad"
                else if (rating > 1 && rating <= 2)
                    feedbackTV.text = "Bad"
                else if (rating > 2 && rating <= 3)
                    feedbackTV.text = "Ok"
                else if (rating > 3 && rating <= 4)
                    feedbackTV.text = "Good"
                else
                    feedbackTV.text = "Very Good"
            }
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        feedbackToolbar.setBackgroundColor(extractedColor)
        window.statusBarColor = Utils.darkenColor(extractedColor, 0.8f)
        titleTV.text = getString(R.string.feed_back)
        submitBtn.setBackgroundColor(extractedColor)

        setRatingBarColor(extractedColor)
    }


    private fun setRatingBarColor(extractedColor: Int) {
        val stars = feedbackRB.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(extractedColor, PorterDuff.Mode.SRC_ATOP)
        stars.getDrawable(1).setColorFilter(
            ContextCompat.getColor(this, R.color.rating_bg),
            PorterDuff.Mode.SRC_ATOP
        )
        stars.getDrawable(0).setColorFilter(
            ContextCompat.getColor(this, R.color.rating_bg),
            PorterDuff.Mode.SRC_ATOP
        )

    }

    companion object {
        const val VENDOR_ID = "vendor_id"
        fun start(context: Context, vendorId: String) {
            context.startActivity(Intent(context, FeedbackActivity::class.java).apply {
                putExtra(
                    VENDOR_ID,
                    vendorId
                )
            })
        }
    }
}
