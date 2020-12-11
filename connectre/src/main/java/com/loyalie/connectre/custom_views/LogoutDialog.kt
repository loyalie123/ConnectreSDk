package com.loyalie.connectre.custom_views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.loyalie.connectre.R
import com.loyalie.connectre.data.PreferenceStorage
import com.loyalie.connectre.util.logout
import kotlinx.android.synthetic.main.logout_dialog.*

class LogoutDialog(val contextIn: Context, private val preferenceStorage: PreferenceStorage?) :
    Dialog(contextIn) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.logout_dialog)
        window?.setBackgroundDrawable(ColorDrawable(0))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    fun pop() {
        setCanceledOnTouchOutside(true)
        descTV.text = context.getString(R.string.are_you_sure_you_want)
        okTV.setOnClickListener {
            remove()
        }
        cancelTV.setOnClickListener {
            remove()
            contextIn.logout(preferenceStorage!!, false)
        }
        show()
    }

    fun remove() {
        dismiss()
        cancel()
    }


}