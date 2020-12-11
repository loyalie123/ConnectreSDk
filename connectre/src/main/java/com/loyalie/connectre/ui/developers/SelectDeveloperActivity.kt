package com.loyalie.connectre.ui.developers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.DeveloperItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.home.HomeActivity
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_select_developer.*
import javax.inject.Inject


class SelectDeveloperActivity : BaseActivity() {
    private var developerAdapter: DeveloperAdapter? = null
    private val developers = ArrayList<DeveloperItem>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: SelectDeveloperVM

    @Inject
    lateinit var picasso: Picasso

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    var fromactiivty = ""
    private var revealX = 0
    private var revealY = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move)

        setContentView(R.layout.activity_select_developer)

        if (intent != null) {
            fromactiivty = intent.getStringExtra("From")!!
            if (fromactiivty == "splash")
                animationCall(savedInstanceState)
        }


        ConnectReApp.themeColor = ContextCompat.getColor(this, R.color.black)

        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getDevelopers()

        initRV()
        observeVM()

        reloadBtn.setOnClickListener {
            reloadBtn.setGone()
            viewModel.getDevelopers()
        }
    }

    private fun animationCall(savedInstanceState: Bundle?) {


        if (savedInstanceState == null) {
            root_layout.setVisibility(View.INVISIBLE)
            val viewTreeObserver: ViewTreeObserver = root_layout.getViewTreeObserver()
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            root_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                        } else {
                            root_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        }
                    }
                })
            }
        }
    }

    private fun circularRevealActivity() {
        val cx: Int = root_layout.getWidth() / 2
        val cy: Int = root_layout.getHeight() / 2
        val finalRadius: Float =
            Math.max(root_layout.getWidth(), root_layout.getHeight()).toFloat()
        // create the animator for this view (the start radius is zero)
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(root_layout, cx, cy, 0f, finalRadius)
        circularReveal.duration = 1000
        // make the view visible and start the animation
        root_layout.setVisibility(View.VISIBLE)
        circularReveal.start()
    }

    private fun doCircularReveal(view: View) { // `get the center for the clipping circle
        val centerX: Int = (view.getLeft() + view.getRight()) / 2
        val centerY: Int = (view.getTop() + view.getBottom()) / 2
        val startRadius = 0
        // get the final radius for the clipping circle
        val endRadius: Int = Math.max(view.getWidth(), view.getHeight())
        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(
            view,
            centerX, centerY, startRadius.toFloat(), endRadius.toFloat()
        )
        anim.duration = 1000
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        view.setVisibility(View.VISIBLE)
        anim.start()
    }

    private fun initRV() {
        developerRV.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        developerAdapter = DeveloperAdapter(object : DeveloperAdapter.OnSelectionCallBack {
            override fun onSelection(dev: DeveloperItem) {
                HomeActivity.start(this@SelectDeveloperActivity, dev)
                finish()
            }
        }, developers, picasso)
        developerRV.adapter = developerAdapter
    }

    private fun observeVM() {
        viewModel.developerHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    developers.clear()
                    if (it.data != null) {
                        developers.addAll(it.data)
                        developerAdapter?.notifyDataSetChanged()
                    } else
                        "Oops !Not a part of any referral program".toast(this)
                    loadingDialog.remove()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    onApiError(it.exception)
                    reloadBtn.setVisible()
                }
                is ViewState.Loading -> {
                    loadingDialog.load()
                }
            }
        })
    }

    companion object {
        const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
    }

    /* override fun onBackPressed() {
         if(fromactiivty=="splash"){
             finish()
         }else{
            super.onBackPressed()
         }
     }*/
}
