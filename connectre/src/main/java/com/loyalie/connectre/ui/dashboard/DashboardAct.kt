package com.loyalie.connectre.ui.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.ui.dashboard.payment.PaymentFragment
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_project.projectVP
import kotlinx.android.synthetic.main.activity_project.tab_layout
import kotlinx.android.synthetic.main.close_title_toolbar.backArrowIV
import kotlinx.android.synthetic.main.close_title_toolbar.titleTV
import kotlinx.android.synthetic.main.close_title_toolbar.toolbar
import kotlinx.android.synthetic.main.notifi_title_toolbar.*
import java.util.ArrayList

class DashboardAct : BaseActivity() {
    private val mFragments = ArrayList<Fragment>()
    lateinit var projectPagerAdapter: DashboardPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initToolbar()
        extractIntent()
        setUpVP()
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        toolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = "Property 1"
        backArrowIV.setOnClickListener {
            onBackPressed()
        }
        notificationIv.setOnClickListener {
//            finish()
            DashNotificationAct.start(this)
            overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
        }
    }

    var userId: Int = 0
    var soId: Int = 0
    private fun extractIntent() {
        if (intent != null) {
            userId = intent.getIntExtra("userId", 0)
            soId = intent.getIntExtra("soId", 0)
        }
    }

    private fun setUpVP() {
        mFragments.clear()
        mFragments.add(OverViewfragmnt.newInstance(userId, soId))
        mFragments.add(PaymentFragment.newInstance(userId, soId))

        mFragments.add(DocumentFragment.newInstance("", ""))

//        mFragments.add(FAQFragment.newInstance(project.faqList,projectId))
//        mFragments.add(TestimonialsFragment.newInstance(project.testimonials,projectId))


        tab_layout.post {
//            tab_layout.changeTextFonts(0, this.getUbunduBold()!!, 18f)
        }
        setTabSelection(tab_layout)
        projectPagerAdapter = DashboardPagerAdapter(this, supportFragmentManager, mFragments)
        projectVP.adapter = projectPagerAdapter
        tab_layout.setupWithViewPager(projectVP)

    }

    private fun setTabSelection(tab_layout: CustomTabLayout?) {
        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
//                    tab_layout.changeTextFonts(it.position, this@DashboardAct.getUbundu()!!, 14f)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    /*       tab_layout.changeTextFonts(
                               it.position,
                               this@DashboardAct.getUbunduBold()!!,
                               18f
                           )*/
                }
            }
        })
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
}