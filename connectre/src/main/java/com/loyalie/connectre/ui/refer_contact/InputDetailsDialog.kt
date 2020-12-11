package com.loyalie.connectre.ui.refer_contact

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.loyalie.connectre.R
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.input_contact_dilog.*


class InputDetailsDialog(
    context: Context,
    private val commentDialogInteractions: CommentDialogInteractions
) : Dialog(context) {
    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setContentView(R.layout.input_contact_dilog)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (window != null) window!!.attributes = lp
        val commentBtn = findViewById<View>(R.id.commentBtn) as TextView
        val nameET = findViewById<View>(R.id.nameET) as EditText
        val mobileET = findViewById<View>(R.id.mobET) as EditText
        val emailET = findViewById<View>(R.id.emailET) as EditText
        clickableSpan()

        commentBtn.setOnClickListener {


            if (nameET.getTrimText().isBlank()) {
                "Please enter your name".toast(context)
                return@setOnClickListener
            } else if (mobET.getTrimText().isBlank()) {
                "Please enter your mobile number".toast(context)
                return@setOnClickListener
            } else if (mobET.getTrimText().length < 10) {
                "Invalid mobile number".toast(context)
                return@setOnClickListener
            } else if (!emailET.getTrimText().isNullOrEmpty() && !emailET.getTrimText().isEmail()) {
                "Please enter a valid email ID".toast(context)
                return@setOnClickListener
            } else if (!tcCheckbox.isChecked) {
                "Please agree Terms & Conditions".toast(context)
                return@setOnClickListener
            }


            commentDialogInteractions.onCommentSubmit(
                nameET.getTrimText(),
                mobileET.getTrimText(),
                emailET.getTrimText()
            )
        }

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked)  // checked
            ),
            intArrayOf(Color.LTGRAY, ConnectReApp.themeColor)
        )
//        commentCB.setButtonTintList(colorStateList)
        tcCheckbox.buttonTintList = colorStateList
//        tcCheckbox.buttonTintList = ConnectReApp.themeColor
        commentBtn.setBackgroundColor(ConnectReApp.themeColor)
        titleTV.setBackgroundColor(ConnectReApp.themeColor)
    }

    fun clickableSpan() {
        var str = "I agree to the Terms & Conditions"
        val ss = SpannableString(str)
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ConnectReApp.themeColor
            }

            override fun onClick(p0: View) {

                context.openInBrowser("https://shapoorji.loyalie.in/terms_conditions_referral.html")
//                Toast.makeText(this@RegisterActivity, "link clicked", Toast.LENGTH_SHORT).show()

            }
        }
        ss.setSpan(clickableSpan, 15, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(UnderlineSpan(), 15, ss.length, 0);


        tcText.text = ss

        tcText.movementMethod = LinkMovementMethod.getInstance()
    }

    interface CommentDialogInteractions {
        fun onCommentSubmit(
            name: String,
            trimText: String,
            trimText1: String
        )
    }
}
