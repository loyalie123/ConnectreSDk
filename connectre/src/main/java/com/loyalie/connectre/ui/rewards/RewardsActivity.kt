package com.loyalie.connectre.ui.rewards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_rewards.*
import kotlinx.android.synthetic.main.center_title_toolbar.*

class RewardsActivity : BaseActivity() {

    lateinit var pagerAdapter: RewardsPagerAdapter

    private var isRewardsLoading = false
    private var isbenefitsLoading = false

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    lateinit var vendorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)
        vendorId = intent.getStringExtra(VENDOR_ID)!!
        initToolbar()
        initViewClicks()
        setUpVP()

    }

    private fun setUpVP() {
        tab_layout.setBackgroundColor(ConnectReApp.themeColor)
        tab_layout.post {
            tab_layout.changeTextFonts(0, this@RewardsActivity.getUbunduBold()!!, 18f)
        }
        setTabSelection(tab_layout)
        pagerAdapter = RewardsPagerAdapter(
            this, supportFragmentManager,
            arrayListOf(
                BenefitsFragment.newInstance(vendorId),
                RewardsFragment.newInstance(vendorId)
            )
        )
        rewardsVP.adapter = pagerAdapter
        tab_layout.setupWithViewPager(rewardsVP)

    }

    fun load(isReward: Boolean) {
        if (!loadingDialog.isLoading()) loadingDialog.load()
        if (isReward) isRewardsLoading = true
        else isbenefitsLoading = true
    }

    fun stopLoading(isReward: Boolean) {
        if (isReward) isRewardsLoading = false
        else isbenefitsLoading = false

        if (!isRewardsLoading && !isbenefitsLoading) loadingDialog.remove()
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        rewardToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.setText("Rewards")

    }

    private fun setTabSelection(tab_layout: CustomTabLayout?) {
        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    tab_layout.changeTextFonts(it.position, this@RewardsActivity.getUbundu()!!, 14f)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    tab_layout.changeTextFonts(
                        it.position,
                        this@RewardsActivity.getUbunduBold()!!,
                        18f
                    )
                }
            }
        })
    }

    private fun initViewClicks() {
        backArrowIV.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val VENDOR_ID = "vendor_id"
        fun start(context: Context, vendorId: String) {
            context.startActivity(Intent(context, RewardsActivity::class.java).apply {
                putExtra(VENDOR_ID, vendorId)
            })
        }
    }
}
