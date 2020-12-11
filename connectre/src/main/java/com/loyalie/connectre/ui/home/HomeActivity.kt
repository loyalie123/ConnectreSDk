package com.loyalie.connectre.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.custom_views.LogoutDialog
import com.loyalie.connectre.custom_views.UpdateDialog
import com.loyalie.connectre.data.*
import com.loyalie.connectre.ui.dashboard.DashboardAct
import com.loyalie.connectre.ui.developers.SelectDeveloperActivity
import com.loyalie.connectre.ui.event.EventActivity
import com.loyalie.connectre.ui.feedback.FeedbackActivity
import com.loyalie.connectre.ui.profile.ProfileActivity
import com.loyalie.connectre.ui.refer.ReferListActivity
import com.loyalie.connectre.ui.rewards.NotiLeadsActivity
import com.loyalie.connectre.ui.rewards.RewardsActivity
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.navigation_bottom_sheet.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeActivity : BaseActivity(), View.OnClickListener, HomeEventsAdapter.OnEventClickListener,
    HomeAllProjectsAdapter.onRequestCallBack, HomePropertyAdapter.OnPropertyClickListner {
    override fun onGetProjects(
        vendorId: String,
        pcatId: Int,
        position: Int,
        adapter1: HomeProjectsAdapter
    ) {
        viewModel.getProjects(vendorId, pcatId, position)

    }

    override fun notifyAdapter(adapter: HomeProjectsAdapter) {

        adapter.notifyDataSetChanged()

    }


    private val bannerImages = ArrayList<BannerImageItem>()
    private val footerImages = ArrayList<BannerImageItem>()
    private val projects = ArrayList<ProjectItem>()
    private val all_projects = ArrayList<projectCategorys>()
    private val events = ArrayList<EventItem>()
    private val propertyList = ArrayList<propertiesList>()


    private var devImageAdapter: DevImgPagerAdapter? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    //    private var projectsAdapter: HomeProjectsAdapter? = null
    private var bannerAdapter: BottomBannerAdapter? = null
    private var eventsAdapter: HomeEventsAdapter? = null
    private var vendorId = ""
    private var vendorPhone = ""
    private var projectScrollListener: PaginatingScrollListener? = null
    private var eventScrollListener: PaginatingScrollListener? = null
    private var propertyScrollListener: PaginatingScrollListener? = null

    private var totalSlideCount = 0
    private var adapter: HomeAllProjectsAdapter? = null
    private var adapterProperty: HomePropertyAdapter? = null
    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: HomeVM

    @Inject
    lateinit var picasso: Picasso

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.ic_home_hamburger -> {
                toggleBottomSheet()
            }

            R.id.closeIV -> {
                toggleBottomSheet()
            }

            /*  R.id.feedbackCL -> {
                  FeedbackActivity.start(this, vendorId)
                  overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
              }
  */
            R.id.rewardsCL -> {
                RewardsActivity.start(this, vendorId)
                overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
            }


            /* R.id.leadsCL -> {
                 LeadListActivity.start(this, vendorId)
                 overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
             }
 */
            R.id.referCL -> {
                ReferListActivity.start(this, vendorId)
                overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
            }

            R.id.profileBg -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivityForResult(intent, PROFILE_START)
                overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
            }

            /*R.id.selectDeveloperTV -> {
                val intent = Intent(this, SelectDeveloperActivity::class.java).apply {
                    flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                    .putExtra("From", "Home")
                startActivity(intent)
            }*/

            R.id.logoutTV -> {
                LogoutDialog(this, preferenceStorage).pop()
            }

            /*  R.id.notiIV -> {
                  NotiLeadsActivity.start(this, vendorId)
                  overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
              }*/

            R.id.phnBtn -> {
                setBottomsheetDilog()

            }

        }
    }

    private fun setBottomsheetDilog() {
        val view: View =
            layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()
        dialog.callnowTv.setOnClickListener {
            dialog.dismiss()
            dialWatsapp(vendorPhone)
        }
        dialog.request_callback.setOnClickListener {
            dialog.dismiss()
            viewModel.requestCallBack(vendorPhone)
        }
    }

    fun animationMarque() {
        var displayMetrics = DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        var width = displayMetrics.widthPixels;
        var tanim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, +1f,
            Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        );
        tanim.setDuration(30000);
        tanim.setInterpolator(LinearInterpolator());
        tanim.setRepeatCount(Animation.INFINITE);
        tanim.setRepeatMode(Animation.RESTART);

        marqueText.startAnimation(tanim);


    }

    var shortText = "                 This program is managed by Loyalie"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move)
        setContentView(R.layout.activity_home)
        marqueText.text = shortText + "                 " + shortText;
        marqueText.isSelected = true
//        animationMarque()
//        marqueText.setText("This program is managed by Loyalie")
//        animationMarque()
        /*   marqueText.isSelected = true;
           marqueText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
           marqueText.setSingleLine(true);*/
//
//        marqueText.setSelected(true);
        if (intent != null) {
            val fromactiivty = intent.getStringExtra("From")
            if (fromactiivty == "splash")
                animationCall(savedInstanceState)
        }
        preferenceStorage.userId = "12533"
        preferenceStorage.userEmail = "ribina@focaloid.com"
        preferenceStorage.userName = "Ribina"
        preferenceStorage.userPhone = "854738418"
        preferenceStorage.userType = 1
        preferenceStorage.userAvatar ="https://s3-ap-southeast-1.amazonaws.com/benefique/avatar/default.jpg"




        viewModel = viewModelProvider(viewModelFactory)

        if (intent.getSerializableExtra(DEVELOPER_ITEM) != null) {
            val dev = intent.getSerializableExtra(DEVELOPER_ITEM) as DeveloperItem
            if (dev == null) {
                viewModel.getDevelopers()
            } else {
                vendorId = dev.vendor_id
                ConnectRePref.putString(this, "VendorID", dev.vendor_id)
                vendorPhone = dev.vendor_phone ?: ""
                initToolbar()
                viewModel.getDevelopersHome(vendorId)
                viewModel.getCurrentVersion()
                marqueText.setTextColor(ConnectReApp.themeColor)
            }

        } else
            viewModel.getDevelopers()



        initVP()

        setBottomSheet()
        initPropertyRV()
        initProjectRV()

        initEventsRV()

        initBannerRV()
        initClickListener()

        observeVM()

    }

    private fun animationCall(savedInstanceState: Bundle?) {


        if (savedInstanceState == null) {
            home_main.setVisibility(View.INVISIBLE)
            val viewTreeObserver: ViewTreeObserver = home_main.getViewTreeObserver()
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            home_main.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                        } else {
                            home_main.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        }
                    }
                })
            }
        }
    }

    private fun initBannerRV() {
//        bottomBannerRV.isNestedScrollingEnabled = false
        /* val layoutManager =object :LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
             override fun smoothScrollToPosition(
                 recyclerView: RecyclerView,
                 state: RecyclerView.State,
                 position: Int
             ) {
                 val smoothScroller: LinearSmoothScroller =
                     object : LinearSmoothScroller(this@HomeActivity) {
                         val speed = 4000f
                         override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                             return speed
                         }
                     };

                 smoothScroller.setTargetPosition(position);
                 startSmoothScroll(smoothScroller);
             }
         }
         autoScroll()*/
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bottomBannerRV.layoutManager = layoutManager
        bannerAdapter = BottomBannerAdapter(footerImages, picasso, this)
        bottomBannerRV.adapter = bannerAdapter


    }

    private fun circularRevealActivity() {
        val cx: Int = home_main.getWidth() / 2
        val cy: Int = home_main.getHeight() / 2
        val finalRadius: Float =
            Math.max(home_main.getWidth(), home_main.getHeight()).toFloat()
        // create the animator for this view (the start radius is zero)
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(home_main, cx, cy, 0f, finalRadius)
        circularReveal.duration = 1000
        // make the view visible and start the animation
        home_main.setVisibility(View.VISIBLE)
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


    override fun onResume() {
        super.onResume()
        viewModel.getUnreadNotificationCount()
    }

    private fun initClickListener() {
        swipeRL.setOnRefreshListener {
            all_projects.clear()
            footerImages.clear()
            bannerAdapter?.notifyDataSetChanged()

            count = 0
            adapter?.notifyDataSetChanged()
            projectScrollListener?.reset(0, false)
            viewModel.getDevelopersHome(vendorId)
        }
        ic_home_hamburger.setOnClickListener(this)
        closeIV.setOnClickListener(this)
        feedbackCL.setSafeOnClickListener {
            FeedbackActivity.start(this, vendorId)
            overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
        }
        leadsCL.setOnClickListener(this)
        referCL.setOnClickListener(this)
        profileBg.setOnClickListener(this)
        rewardsCL.setOnClickListener(this)
        selectDeveloperTV.setSafeOnClickListener {
            val intent = Intent(this, SelectDeveloperActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
                .putExtra("From", "Home")
            startActivity(intent)
        }
        logoutTV.setOnClickListener(this)
        notiIV.setSafeOnClickListener {
            NotiLeadsActivity.start(this, vendorId)
            overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
        }
        phnBtn.setOnClickListener(this)
    }

//    private fun initFeedsRV() {
//        feedsGridRV.isNestedScrollingEnabled = false
//        feedsGridRV.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
//        feedsGridRV.adapter = HomeFeedsAdapter()
//    }

    private fun initEventsRV() {
        eventsRV.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        eventsRV.layoutManager = layoutManager
        eventsAdapter = HomeEventsAdapter(events, picasso, this, this)
        eventsRV.adapter = eventsAdapter
        eventScrollListener =
            object : PaginatingScrollListener(layoutManager, direction = RecyclerView.HORIZONTAL) {
                override fun onLoadMore() {
                    viewModel.getEvents(vendorId)
                }
            }
        eventsRV.addOnScrollListener(eventScrollListener!!)
    }


    private fun initProjectRV() {
        allProjectsRV.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        allProjectsRV.layoutManager = layoutManager
        vendorId = ConnectRePref.getString(this!!, "VendorID", "0").toString()

        adapter = HomeAllProjectsAdapter(all_projects, picasso, vendorId, this, this)
        allProjectsRV.adapter = adapter

        /*      projectScrollListener =
                  object : PaginatingScrollListener(layoutManager, direction = RecyclerView.HORIZONTAL) {
                      override fun onLoadMore() {
                          viewModel.getProjects(vendorId)
                      }
                  }

              allProjectsRV.addOnScrollListener(projectScrollListener!!)

      *//**/
    }

    private fun initPropertyRV() {


        propertyRv.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        propertyRv.layoutManager = layoutManager
//        propertyList.add(propertiesList(0,11,45678.90,"Sample0","test","",0))

        adapterProperty = HomePropertyAdapter(propertyList, picasso, this, this)
        propertyRv.adapter = adapterProperty
        propertyScrollListener =
            object : PaginatingScrollListener(layoutManager, direction = RecyclerView.HORIZONTAL) {
                override fun onLoadMore() {
                    viewModel.getProperties(vendorId)
                }
            }
        propertyRv.addOnScrollListener(propertyScrollListener!!)
    }


    private fun toggleBottomSheet() {
        if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(nav_bottom_sheet)

        bottomSheetBehavior?.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }

        })

        navigationRV.layoutManager = LinearLayoutManager(this)
        navigationRV.adapter =
            BottomSheetAdapter(this, vendorId, object : BottomSheetAdapter.onDissmissCallback {
                override fun onHomeCallback() {
                    toggleBottomSheet()
                }

                override fun onLogout() {
                    LogoutDialog(this@HomeActivity, preferenceStorage).pop()

                }
            })
        setMenuHeader()

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onStop() {
        super.onStop()

        LogoutDialog(this, null).remove()

    }

    private fun setMenuHeader() {
        val avatar = preferenceStorage.userAvatar
        if (avatar.isNotBlank()) {
            picasso.load(avatar).fit().centerCrop()
                .placeholder(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.textColorGreyAlpha10
                        )
                    )
                )
                .error(ColorDrawable(ContextCompat.getColor(this, R.color.textColorGreyAlpha10)))
                .into(profileIV)
        } else {
            profilePlaceHolderBg.setColorFilter(ConnectReApp.themeColor)
            profileIV.setImageResource(R.drawable.ic_person)
        }
        profileNameTV.text = preferenceStorage.userName
        mobileNumTV.text = "+"+preferenceStorage.userCCode+" " +preferenceStorage.userPhone
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        homeToolbar.setBackgroundColor(extractedColor)
        window.statusBarColor = Utils.darkenColor(extractedColor, 0.8f)
        val bgShape = phnBtn.background as GradientDrawable
        bgShape.setColor(extractedColor)
        phnBtn.background = bgShape
    }

    private fun initVP() {
        devImageAdapter = DevImgPagerAdapter(bannerImages, picasso, this)
        devImgVP.adapter = devImageAdapter

    }

    private val developers = ArrayList<DeveloperItem>()

    private fun observeVM() {
        viewModel.developerHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    developers.clear()
                    home_main.visibility = View.VISIBLE
                    if (it.data != null) {
                        developers.addAll(it.data)
                        val dev = developers[0]

                        try {
                            val colorCode = dev.colorcode ?: ""
                            if (colorCode.contains("#"))
                                ConnectReApp.themeColor = Color.parseColor(colorCode)
                            else ConnectReApp.themeColor = Color.parseColor("#" + colorCode)
                        } catch (e: Exception) {
                            ConnectReApp.themeColor = ContextCompat.getColor(this, R.color.black)
                        }
                        initToolbar()
                        vendorId = dev.vendor_id
                        ConnectRePref.putString(this, "VendorID", dev.vendor_id)
                        vendorPhone = dev.vendor_phone ?: ""
                        viewModel.getDevelopersHome(vendorId)
                        viewModel.getCurrentVersion()
                        marqueText.setTextColor(ConnectReApp.themeColor)
//                        developerAdapter?.notifyDataSetChanged()
                    } else
                        "Oops !Not a part of any referral program".toast(this)


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
        viewModel.bannerHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    bannerImages.clear()
                    bannerImages.addAll(it.data)
                    devImageAdapter?.notifyDataSetChanged()
                    if (bannerImages.isEmpty()) {
                        devImgVP.setGone()
                        indicator.setGone()
                    } else {
                        indicator.setVisible()
                        indicator.setViewPager(devImgVP)

                        totalSlideCount = bannerImages.size

                        setAutoScrollVP()
                    }
//                    loadingDialog.remove()


                }
                is ViewState.Error -> {
//                    loadingDialog.remove()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
//                    loadingDialog.load()
                }
            }
        })
        viewModel.footerHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {

//                    loadingDialog.remove()
                    footerImages.clear()
                    footerImages.addAll(it.data)
                    bannerAdapter?.notifyDataSetChanged()
                    if (footerImages.isEmpty()) {
                        bottomBannerRV.setGone()
                        footerTV.visibility = View.GONE
                    }


//                    bannerAdapter?.showLoading(false)
                    autoScroll()

//                    footerImages.addAll(it.data.)

                    var count = footerImages.size


                }
                is ViewState.Error -> {
//                    loadingDialog.remove()

                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
//                    loadingDialog.load()
                }
            }
        })

        viewModel.projectsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    all_projects.clear()
                    all_projects.addAll(it.data)
//                    projects.clear()


                    if (all_projects.isEmpty()) {
//                        projectsTV.setGone()
                        allProjectsRV.setGone()

                    }

//                    loadingDialog.remove()
                    swipeRL.isRefreshing = false
                    adapter?.showLoading(false)

//                    adapter?.notifyItemChanged(it.position)
                    adapter?.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    adapter?.showLoading(false)
                    projectScrollListener?.setLoading(false)
                    swipeRL.isRefreshing = false
//                    loadingDialog.remove()

                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
//                    if (it.isInitial) loadingDialog.load()
//                    else adapter?.showLoading(true)
                }
            }
        })
        /*      viewModel.projectHolder.observe(this, Observer {
                  when (it) {
                      is ViewState.Success -> {
                          all_projects.set(it.position, projectCategorys(it.data[it.position].pcatId,it.data[it.position].categoryName,it.data[it.position].projects,it.data[it.position].count))
                          adapter?.showLoading(false)
                          adapter?.notifyItemChanged(it.position)
                      }
                      is ViewState.Error -> {
      //                    loadingDialog.remove()
                          projectScrollListener?.setLoading(false)
                          adapter?.showLoading(false)
                          onApiError(it.exception)
                      }
                      is ViewState.Loading -> {
      //                    if (it.isInitial) loadingDialog.load()
      //                    else adapter?.showLoading(true)
                      }
                  }
              })*/

        viewModel.eventsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    events.clear()
                    events.addAll(it.data)
                    if (events.isEmpty()) {
                        eventsTV.setGone()
                        eventsRV.setGone()
                    } else {

                    }

                    loadingDialog.remove()
                    eventsAdapter?.showLoading(false)
                    eventsAdapter?.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    eventScrollListener?.setLoading(false)
                    eventsAdapter?.showLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial) loadingDialog.load()
                    else eventsAdapter?.showLoading(true)
                }
            }
        })
        viewModel.propertiesHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    propertyList.clear()
                    propertyList.addAll(it.data)
//
//                    propertyList.add(propertiesList(0,11,45678.90,"Sample0","test","",0))
                    if (propertyList.isEmpty()) {
                        propertyTv.setGone()
                        propertyRv.setGone()
                    } else {
                        propertyTv.setVisible()
                        propertyRv.setVisible()
                    }

                    loadingDialog.remove()
                    adapterProperty?.showLoading(false)
                    adapterProperty?.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    propertyScrollListener?.setLoading(false)
                    adapterProperty?.showLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial) loadingDialog.load()
                    else adapterProperty?.showLoading(true)
                }
            }
        })

        viewModel.notificationCountHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    if (it.data > 0) unreadIndicator.setVisible()
                    else unreadIndicator.setGone()
                }
            }
        })
        viewModel.requestCallback.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {

                    loadingDialog.remove()
//                it.toString(this)
                    "Our team will contact you soon".toast(this)
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
        viewModel.versionHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    val info = packageManager.getPackageInfo(
                        this.packageName,
                        PackageManager.GET_ACTIVITIES
                    )

                    if (info.versionCode < it.data.MinAndroidVersion) {
                        UpdateDialog(this).pop(true)
                    } else if (info.versionCode < it.data.CurrentAndroidVersion) {
                        UpdateDialog(this).pop(false)
                    }

                }
            }
        })
    }

    val timer = Timer()
    private fun setAutoScrollVP() {
        if (totalSlideCount > 0) {
            val timerTask = object : TimerTask() {
                override fun run() {
                    devImgVP.post {
                        devImgVP.currentItem = (devImgVP.currentItem + 1) % totalSlideCount
                    }
                }
            }
            timer.schedule(timerTask, 3000, 3000)
        }

    }

    var count = 0
    fun autoScroll() {

        val speedScroll: Long = 2500
        val handler = Handler()


        val runnable = object : Runnable {

            override fun run() {
                if (count == bannerAdapter!!.itemCount)
                    count = 0
                if (count < bannerAdapter!!.itemCount) {
                    bottomBannerRV.smoothScrollToPosition(++count)
                    handler.postDelayed(this, speedScroll)
                }
            }
        }
        handler.postDelayed(runnable, speedScroll)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EventActivity.EVENT_REQUEST && resultCode == Activity.RESULT_OK) {
            viewModel.getDevelopersHome(vendorId)
        } else if (requestCode == PROFILE_START && resultCode == PROFILE_UPDATED) {
            setMenuHeader()
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onEventClick(event: EventItem, v: View) {
        EventActivity.start(this, event, v)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        } else super.onBackPressed()
    }

    companion object {
        const val DEVELOPER_ITEM = "developer_item"
        val PROFILE_START = 71
        val PROFILE_UPDATED = 72
        fun start(context: Context, dev: DeveloperItem) {
            context.startActivity(Intent(context, HomeActivity::class.java).apply {
                putExtra(DEVELOPER_ITEM, dev)
            })
        }
    }

    override fun onPropertyClick(properties: propertiesList, v: View) {
        val intent = Intent(this, DashboardAct::class.java)
        intent.putExtra("userId",properties.user_id)
        intent.putExtra("soId",properties.sales_order_id)
        startActivity(intent)
    }


}
