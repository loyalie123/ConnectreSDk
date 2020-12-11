package com.loyalie.connectre.ui.otp

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ConnectRePref
import com.loyalie.connectre.data.DeveloperItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.home.HomeActivity
import com.loyalie.connectre.util.*
import com.loyalie.connectre.widget.Toasty
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.*
import javax.inject.Inject

class OtpEmailActivity : BaseActivity() {

    private val smsBroadcastReceiver by lazy { SMSBroadcastReciever() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: OtpVM

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    private var isLogin = true
    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_email)
        isLogin = intent.getBooleanExtra(IS_LOGIN, false)
        viewModel = viewModelProvider(viewModelFactory)
        phoneNumber = intent.getStringExtra(EMAIL)!!
        enterOtpLabelTV.setText("${getString(R.string.enter_the_mobileotp_send_to)} $phoneNumber")
        setupOTPFields()

        continueBtn.setOnClickListener {
            val otp =
                otpOneET.getTrimText() + otpTwoET.getTrimText() + otpThreeET.getTrimText() + otpFourET.getTrimText()
            if (otp.trim().length < 4) {
                Toasty(this).showBelow(otpOneET, getString(R.string.invalid_otp_entered))
            } else {
                hideKeyboard()
                viewModel.verifyEmail(otp)
            }


        }

        registerSMSListener()
        observeVM()

        resendOTPTV.setOnClickListener {
            viewModel.resendEmailOtp()
        }
    }

    private fun observeVM() {
        viewModel.emailHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    preferenceStorage.loginStatus = true
                    viewModel.getDevelopers()
                    /*  if (isLogin) {

                          if (preferenceStorage.regDone == 1) {
                              preferenceStorage.loginStatus =false
                              startActivity(Intent(this, RegTypeAct::class.java).apply {
                                  flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                                          or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                          or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                          or Intent.FLAG_ACTIVITY_NEW_TASK)
                              })
                          } else {
                              preferenceStorage.loginStatus = true
                              viewModel.getDevelopers()

                          } } else {
                          setResult(RESULT_CODE)
                          finish()
                      }*/

                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })
        viewModel.resendEmailHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    "Otp sent".toast(this)

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

        viewModel.developerHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    val developers = ArrayList<DeveloperItem>()

                    developers.clear()
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
                        ConnectRePref.putString(this, "VendorID", dev.vendor_id)

                        startActivity(
                            Intent(this, HomeActivity::class.java).apply {
                                flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }.putExtra("From", "Login")
                                .putExtra("developer_item", dev)
                        )
                        finish()

//                        developerAdapter?.notifyDataSetChanged()
                    } else
                        "Oops !Not a part of any referral program".toast(this)
                    loadingDialog.remove()


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


    }


    private fun setupOTPFields() {
        otpOneET.setOnEditorActionListener { p0, p1, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (p1 == EditorInfo.IME_ACTION_DONE)) {

            }
            false;
        }
        otpTwoET.setOnEditorActionListener { p0, p1, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (p1 == EditorInfo.IME_ACTION_DONE)) {

            }
            false;
        }
        otpThreeET.setOnEditorActionListener { p0, p1, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (p1 == EditorInfo.IME_ACTION_DONE)) {

            }
            false;
        }
        otpFourET.setOnEditorActionListener { p0, p1, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (p1 == EditorInfo.IME_ACTION_DONE)) {

            }
            false;
        }

        otpOneET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (s.length > 1) {
                        otpOneET.setText(s[0].toString())
                        otpTwoET.setText(s[1].toString())
                        otpThreeET.requestFocus()
                    } else {
                        otpTwoET.requestFocus()
                    }

                } else if (s.isEmpty()) {
                    otpOneET.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        otpTwoET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (s.length > 1) {
                        otpTwoET.setText(s[0].toString())
                        otpThreeET.setText(s[1].toString())
                        otpFourET.requestFocus()
                    } else
                        otpThreeET.requestFocus()
                } else if (s.isEmpty()) {
                    otpOneET.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        otpThreeET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (s.length > 1) {
                        otpThreeET.setText(s[0].toString())
                        otpFourET.setText(s[1].toString())
                    }
                    otpFourET.requestFocus()
                } else if (s.isEmpty()) {
                    otpTwoET.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        otpFourET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    otpFourET.setSelection(1)
                } else if (s.isEmpty()) {
                    otpThreeET.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun registerSMSListener() {
        val client = SmsRetriever.getClient(this)
        val retriever = client.startSmsRetriever()
        retriever.addOnSuccessListener {
            val listener = object : SMSBroadcastReciever.Listener {
                override fun onSMSReceived(otp: String) {
                    otpOneET.setText(otp[0].toString())
                    otpTwoET.setText(otp[1].toString())
                    otpThreeET.setText(otp[2].toString())
                    otpFourET.setText(otp[3].toString())
                    viewModel.verifyOtp(otp, phoneNumber, isLogin)
                }

                override fun onTimeOut() {

                }
            }
            smsBroadcastReceiver.injectListener(listener)
            registerReceiver(smsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }
        retriever.addOnFailureListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsBroadcastReceiver)
    }


    companion object {
        const val IS_LOGIN = "is_login"
        const val EMAIL = "email"
        const val REQUEST_CODE = 11
        const val RESULT_CODE = 12
        fun start(context: Activity, isLogin: Boolean, email: String) {
            context.startActivityForResult(Intent(context, OtpEmailActivity::class.java).apply {
                putExtra(IS_LOGIN, isLogin)
                putExtra(EMAIL, email)
            }, REQUEST_CODE)
        }
    }
}
