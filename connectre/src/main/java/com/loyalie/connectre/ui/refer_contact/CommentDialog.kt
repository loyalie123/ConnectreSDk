package com.loyalie.connectre.ui.refer_contact

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.toast
import kotlinx.android.synthetic.main.comment_dialog.*


class CommentDialog(
    context: Context,
    private val commentDialogInteractions: CommentDialogInteractions
) : Dialog(context) {
    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setContentView(R.layout.comment_dialog)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (window != null) window!!.attributes = lp
        val commentBtn = findViewById<View>(R.id.commentBtn) as TextView
        val commentCB = findViewById<View>(R.id.commentCB) as CheckBox
        val commentET = findViewById<View>(R.id.commentET) as EditText
        val commentCbParent = findViewById<View>(R.id.commentCbParent) as LinearLayout

        commentCbParent.setOnClickListener { commentCB.isChecked = !commentCB.isChecked }

        commentBtn.setOnClickListener {
            var comment = ""
            if (!commentCB.isChecked) {
                comment = commentET.getText().toString()
                if (comment.isBlank()) {
                    "Please enter your comment".toast(context)
                    return@setOnClickListener
                }
            }

            commentDialogInteractions.onCommentSubmit(comment)
        }

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked)  // checked
            ),
            intArrayOf(Color.LTGRAY, ConnectReApp.themeColor)
        )
        commentCB.setButtonTintList(colorStateList)
        commentBtn.setBackgroundColor(ConnectReApp.themeColor)
        titleTV.setBackgroundColor(ConnectReApp.themeColor)
    }

    interface CommentDialogInteractions {
        fun onCommentSubmit(
            comment: String
        )
    }
}
