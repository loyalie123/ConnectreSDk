package com.loyalie.connectre.ui.enter_phn

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.loyalie.connectre.AppSignatureHelper
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.data.saveApiToken
import com.loyalie.connectre.ui.otp.OtpActivity
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_enter_phone_number.*
import javax.inject.Inject

class EnterPhoneNumberActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var mobileNumber = ""
    private var email = ""
    private var ccode = "91"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var viewModel: EnterPhoneVM

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    lateinit var apiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move)

        /*   getWindow().setFlags(
               WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_enter_phone_number)

        if (intent.getBooleanExtra("IS_FROM_SPLASH", false)) {
            animationCall(savedInstanceState)
        }

        ConnectReApp.themeColor = ContextCompat.getColor(this, R.color.black)
        var appSignatureHelper = AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures()
        if (intent.getBooleanExtra(IS_AUTH_FAIL, false)) {
            getString(R.string.current_session_expired).toast(this)
        }

        viewModel = viewModelProvider(viewModelFactory)
        ResourcesCompat.getFont(this, R.font.sinkinsans_400regular)?.let {
            countryCodeTV.setTypeFace(
                it
            )
        }
//     countryCodeTV.setde(+91)
        if (countryCodeTV.selectedCountryCodeAsInt == 91) {
            hideKeyboard()
            emailET.setGone()
        } else {
            hideKeyboard()
            emailET.setVisible()
        }
        countryCodeTV.setOnCountryChangeListener {
            hideKeyboard()
            if (countryCodeTV.selectedCountryCodeAsInt == 91) {
                emailET.setGone()
            } else
                emailET.setVisible()
        }
        continueBtn.setOnClickListener {
            mobileNumber = mobileNumET.getTrimText()
            ccode = "" + countryCodeTV.selectedCountryCodeAsInt
            email = emailET.getTrimText()
            if (mobileNumber.isBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.please_enter_your_mobile_number),
                    Toast.LENGTH_LONG
                ).show()
                /* Toasty(this).showBelow(
                     mobileNumET,getString(R.string.please_enter_your_mobile_number)

                 )*/
            } else if (ccode == "91" && mobileNumber.length != 10) {
//                Toasty(this).showBelow(mobileNumET, getString(R.string.invalid_mobile))

                Toast.makeText(this, getString(R.string.invalid_mobile), Toast.LENGTH_LONG).show()
            } else if (ccode != "91") {
                if (email.isBlank())
                    "Please enter email ID".toast(this)
                else if (!email.isEmail())
                    "Please enter a valid email ID".toast(this)
                else {
                    hideKeyboard()
//                saveCountryCode(countryCodeTV.selectedCountryCodeAsInt)
                    viewModel.login(mobileNumber, ccode, email)
                }
            } else {
                hideKeyboard()
                ccode = "91"
                email = ""
//                saveCountryCode(countryCodeTV.selectedCountryCodeAsInt)
                viewModel.login(mobileNumber, "91", "")
            }
        }

        styleTermsTV()
        observeVM()

        apiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .enableAutoManage(this, this)
            .addApi(Auth.CREDENTIALS_API)
            .build()

        getNumbers()


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (getCurrentFocus() != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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

    private fun styleTermsTV() {
        val ss = SpannableString(getString(R.string.by_signing_in_you_agree_to_the_terms))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openInBrowser(TERMS_CONDITION_URL)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val clickableSpanPrivacy = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openInBrowser(PRIVACY_POLICY_URL)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        ss.setSpan(clickableSpan, 34, 46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), 34, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), 34,
            46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ss.setSpan(clickableSpanPrivacy, 49, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), 49, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), 49,
            ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        termsPrivacyTV.setText(ss)
        termsPrivacyTV.movementMethod = LinkMovementMethod.getInstance()
        termsPrivacyTV.highlightColor = Color.TRANSPARENT
    }

    private fun observeVM() {
        viewModel.enterPhnHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.dismiss()

                    preferenceStorage.userCCode = ccode.toInt()
                    preferenceStorage.userPhone = mobileNumber
                    this.saveApiToken(it.data.sec_key)
                    OtpActivity.start(this, true, mobileNumber, email, ccode)
                }
                is ViewState.Error -> {
                    loadingDialog.dismiss()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading(R.color.goldenColor)
                }
            }
        })


    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun getNumbers() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val hintRequest = HintRequest.Builder()
                    .setPhoneNumberIdentifierSupported(true)
                    .build()
                val intent = Auth.CredentialsApi.getHintPickerIntent(apiClient, hintRequest)
                try {
                    startIntentSenderForResult(
                        intent.getIntentSender(),
                        RESOLVE_HINT,
                        null,
                        0,
                        0,
                        0
                    );
                } catch (e: IntentSender.SendIntentException) {

                }
            }
        }, 1000) // }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
                val phn = credential?.getId() ?: ""
                if (phn.startsWith("+91")) mobileNumET.setText(phn.removePrefix("+91"))
                else mobileNumET.setText(phn)
            } else super.onActivityResult(requestCode, resultCode, data)
        }


    }

    companion object {
        const val RESOLVE_HINT = 12
        const val IS_AUTH_FAIL = "is_auth_fail"
        fun start(
            context: Context,
            isAuthFail: Boolean,
            isLogout: Boolean,
            from_splash: Boolean
        ) {
            context.startActivity(Intent(context, EnterPhoneNumberActivity::class.java).apply {
                if (isLogout) {
                    flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                putExtra(IS_AUTH_FAIL, isAuthFail)
                putExtra("IS_FROM_SPLASH", from_splash)

            }
            )

        }
    }

}
