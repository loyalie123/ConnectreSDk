package com.loyalie.connectre.ui.rewards

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_benefits_detail.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject


class BenefitsDetailAct : BaseActivity(), View.OnClickListener {
    @Inject
    lateinit var picasso: Picasso
    var clipboard:ClipboardManager ?=null
    var clip :ClipData ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benefits_detail)
        initToolbar()
        setViews()
        listners()
    }

    private fun listners() {
        rewardCodeTV.setOnClickListener {
             clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
             clip = ClipData.newPlainText(label.toString(), rewardCode)
            clipboard?.setPrimaryClip(clip!!)

            "Copied to ClipBoard".toast(this)
            if (rewardCode != "" && !url.isNullOrBlank()) {
                it.context.openInBrowser(url)
            }
        }
        backArrowIV.setOnClickListener(this)

    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        rewardToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.benefits)
    }

    private fun setViews() {
        if (logo.isNullOrBlank()) rewardIV.setImageResource(R.drawable.placeholder)
        else rewardIV.loadUrl(logo, picasso)
        rewardNameTV.text = Title
        programNameTV.text = catname
        benfitExpiryDateTV.text = expirydate
        rewardCodeTV.text = rewardCode
        redemptionDetialTV.text = des
        if (rewardCode != "")
            rewardCodeTV.visibility = View.VISIBLE
        else
            rewardCodeTV.visibility = View.GONE
        if (tandc == "null") {
            termsDetailTV.visibility = View.GONE
            termsTV.visibility = View.GONE
        } else {
            termsDetailTV.visibility = View.VISIBLE
            termsTV.visibility = View.VISIBLE
            termsDetailTV.text = tandc
        }
    }

    /* override fun onOptionsItemSelected(item: MenuItem?): Boolean {
         if (item?.itemId == android.R.id.home) {
             onBackPressed()
             return true
         }
         return super.onOptionsItemSelected(item)
     }

     override fun onBackPressed() {

         finish()
         overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
     }*/

    companion object {
        const val REWARDS = "rewards"
        var Title = ""
        var des = ""
        var expirydate = ""
        var rewardCode = ""
        var catname = ""
        var tandc = ""
        var logo = ""
        var url = ""
        fun start(
            context: Context,
            rewardWrapper: RewardWrapper
        ) {
            context.startActivity(Intent(context, BenefitsDetailAct::class.java).apply {
                Title = rewardWrapper.title()
                des = rewardWrapper.subTitle()
                expirydate = rewardWrapper.expiry()
                rewardCode = rewardWrapper.code().toString()
                tandc = rewardWrapper.termsCondition().toString()
                catname = rewardWrapper.catName()!!
                logo = rewardWrapper.img()!!
                url = rewardWrapper.url()!!

            })
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.backArrowIV -> finish()
        }
    }
}
