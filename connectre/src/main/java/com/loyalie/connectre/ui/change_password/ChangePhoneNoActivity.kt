package com.loyalie.connectre.ui.change_password

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.otp.OtpActivity
import com.loyalie.connectre.util.*
import com.loyalie.connectre.widget.Toasty
import kotlinx.android.synthetic.main.activity_change_phone_no.*
import kotlinx.android.synthetic.main.activity_change_phone_no.countryCodeTV
import kotlinx.android.synthetic.main.activity_change_phone_no.emailET
import kotlinx.android.synthetic.main.activity_change_phone_no.mobileNumET
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class ChangePhoneNoActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ChangePhoneVM

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

private var email=""
private var code=""
private var mobile=""
    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
            R.id.submitBtn -> {
                 mobile = mobileNumET.getTrimText()
                code=countryCodeTV.selectedCountryCode
                 email=emailET.getTrimText()
                if (mobile.isBlank()) Toasty(this).showBelow(
                    mobileNumET,
                    "Please enter mobile number"
                )
                else if (code=="91" && mobile.length!=10) Toasty(this).showBelow(
                    mobileNumET,
                    "Please enter a valid mobile number"
                )

                else if (code != "91") {
                    if (email.isBlank())

                        "Please enter email ID".toast(this)
                    else if (!email.isEmail())
                        "Please enter a valid email ID".toast(this)
                    else {
                        hideKeyboard()
//                saveCountryCode(countryCodeTV.selectedCountryCodeAsInt)
                        viewModel.changePhone(mobile,code,email)
                    }
                }
                else viewModel.changePhone(mobile,code,email)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone_no)
        viewModel = viewModelProvider(viewModelFactory)
        initToolbar()

        ResourcesCompat.getFont(this, R.font.sinkinsans_400regular)?.let {
            countryCodeTV.setTypeFace(
                it
            )
        }
//     countryCodeTV.setde(+91)
        if (countryCodeTV.selectedCountryCodeAsInt == 91) {
            hideKeyboard()
            emailET.setGone()
            changeemailLabelTV.setGone()
        } else{
            hideKeyboard()
            emailET.setVisible()
            changeemailLabelTV.setVisible()
        }
        countryCodeTV.setOnCountryChangeListener {
            hideKeyboard()
            if (countryCodeTV.selectedCountryCodeAsInt == 91) {
                emailET.setGone()
                changeemailLabelTV.setGone()
            } else {
                emailET.setVisible()
                changeemailLabelTV.setVisible()
            }
        }
        initClickListener()

        viewModel.successHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.dismiss()
                    preferenceStorage.userPhone=mobile
                    if(code!="")
                    preferenceStorage.userCCode=code.toInt()
                    OtpActivity.start(this, false, it.data, email, code)
                }
                is ViewState.Error -> {
                    onApiError(it.exception)
                    loadingDialog.dismiss()
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == OtpActivity.RESULT_CODE) {

            Activity.RESULT_OK
            finish()
        } else super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
    }


    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        changePasswordToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.change_phone)
        submitBtn.setBackgroundColor(extractedColor)
    }
}
