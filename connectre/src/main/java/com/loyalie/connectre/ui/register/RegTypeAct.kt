package com.loyalie.connectre.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.util.Utils
import kotlinx.android.synthetic.main.activity_reg_type.*
import kotlinx.android.synthetic.main.center_title_toolbar.*

class RegTypeAct : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_type)
        initToolbar()
        customer_type.setOnClickListener {
            preferenceStorage.userType = 1

            startActivity(Intent(this, RegCustomerAct::class.java))
        }
        emp_type.setOnClickListener {
            preferenceStorage.userType = 2
            startActivity(Intent(this, RegisterAct::class.java))
        }
        backArrowIV.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initToolbar() {
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val extractedColor = resources.getColor(R.color.black)
        registerToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = "Register"

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


