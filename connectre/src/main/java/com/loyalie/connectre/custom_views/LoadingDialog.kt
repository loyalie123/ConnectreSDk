package com.loyalie.connectre.custom_views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.dpToPx
import com.loyalie.connectre.widget.LoaderView


class LoadingDialog(contextIn: Context) : Dialog(contextIn) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(FrameLayout(context))

        window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.blackAlphaLight
                )
            )
        )
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setCanceledOnTouchOutside(true)
    }

    fun startLoading(color: Int = ConnectReApp.themeColor) {
        val v = LoaderView(
            context, context.dpToPx(8), 4, color
        )
        val params =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        params.gravity = Gravity.CENTER
        v.setLayoutParams(params)
        (window?.decorView?.rootView as FrameLayout?)?.removeAllViews()
        (window?.decorView?.rootView as FrameLayout?)?.addView(v)
        show()
    }


    private val MIN_SHOW_TIME = 500L // ms
    private val MIN_DELAY = 200L // ms

    private var mStartTime: Long = -1

    private var mPostedHide = false

    private var mPostedShow = false

    private var mDismissed = false
    private val handler = Handler()

    private val mDelayedHide = Runnable {
        mPostedHide = false
        mStartTime = -1
        dismiss()
    }

    private val mDelayedShow = Runnable {
        mPostedShow = false
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis()
            startLoading()
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        removeCallbacks()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        handler.removeCallbacks(mDelayedHide)
        handler.removeCallbacks(mDelayedShow)
    }


    fun remove() {
        mDismissed = true
        handler.removeCallbacks(mDelayedShow)
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= MIN_SHOW_TIME || mStartTime == -1.toLong()) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            dismiss()
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to remove it when its been
            // shown long enough.
            if (!mPostedHide) {
                handler.postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, remove() is called, the view is never made visible.
     */
    fun load() {
        // Reset the start time.
        mStartTime = -1
        mDismissed = false
        handler.removeCallbacks(mDelayedHide)
        if (!mPostedShow) {
            handler.postDelayed(mDelayedShow, MIN_DELAY)
            mPostedShow = true
        }
    }

    fun isLoading() = mPostedShow || isShowing


}