package com.loyalie.connectre.ui.splash


import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.loyalie.connectre.AppSignatureHelper
import com.loyalie.connectre.R

import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.ui.developers.SelectDeveloperActivity
import com.loyalie.connectre.ui.enter_phn.EnterPhoneNumberActivity
import com.loyalie.connectre.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {

    private val navigateRunnable = Runnable {
        if (!preferenceStorage.loginStatus) {
            var appSignatureHelper = AppSignatureHelper(this);
            appSignatureHelper.getAppSignatures()
            edelweissShimmer.stopShimmer()
//            if (sP != null&&sP!!.isRunning())
//                sP!!.cancel()
            EnterPhoneNumberActivity.start(this, false, false, true)
//            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            startActivity(  Intent(this, CircularRevealAct::class.java))
            finish()
        } else {
            edelweissShimmer.stopShimmer()

//            if (sP != null&&sP!!.isRunning())
//                sP!!.cancel()
            this.startActivity(
                Intent(this, HomeActivity::class.java).putExtra("From", "splash").addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )
            finish()
//            this.overridePendingTransition(android.R.anim.linear_interpolator,0)

        }
    }

    private val handler = Handler()

    fun presentActivity(view: View) {
//        btn.visibility=View.VISIBLE
        /* shapoorji_logo.visibility=View.GONE
         bottom_logo.visibility=View.GONE
         rootLayout.setBackgroundResource(R.color.black)*/
        btn.visibility = View.GONE
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition")
        val revealX = (view.x + view.width / 2).toInt()
        val revealY = (view.y + view.height / 2).toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            val viewTreeObserver: ViewTreeObserver = rootLayout.getViewTreeObserver()

            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE)
        }

        val intent = Intent(this, SelectDeveloperActivity::class.java)
        intent.putExtra("From", "splash")
        /*  intent.putExtra(SelectDeveloperActivity.EXTRA_CIRCULAR_REVEAL_X, revealX)
          intent.putExtra(SelectDeveloperActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY)*/
        ActivityCompat.startActivity(this, intent, options.toBundle())
//rootLayout.setBackgroundResource(R.color.black)

    }

    protected fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = (Math.max(
                rootLayout.getWidth(),
                rootLayout.getHeight()
            ) * 1.1).toFloat() as Float
            // create the animator for this view (the start radius is zero)
            val circularReveal =
                ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0f, finalRadius)
            circularReveal.duration = 900
            circularReveal.startDelay = 0
            circularReveal.interpolator = AccelerateInterpolator()
            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE)
            circularReveal.start()
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*   requestWindowFeature(Window.FEATURE_NO_TITLE);
           this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splash)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

//
        edelweissShimmer.startShimmer()
        /*    val animation: Animation = TranslateAnimation(0f, shapoorji_logo.getWidth().toFloat() + shine.width.toFloat(), 0f, 0f)
            animation.duration = 5000
            animation.fillAfter = false
            animation.interpolator = AccelerateDecelerateInterpolator()*/


        val zoomOutAnimation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.zoomout
        )
        shapoorji_logo.startAnimation(zoomOutAnimation)
        zoomOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
//                shapoorji_logo.clearAnimation()
//                presentActivity(btn)
                //open your second activity


            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })


//        bottom_logo.startAnimation(animation1)
//        bottomShimmer.startShimmer()
        handler.postDelayed(navigateRunnable, 3500)

    }

    override fun onStop() {
        super.onStop()
//        if (sP != null&&sP!!.isRunning())
//            sP!!.cancel()
    }

    override fun onDestroy() {
        handler.removeCallbacks(navigateRunnable)
        super.onDestroy()
    }


}
