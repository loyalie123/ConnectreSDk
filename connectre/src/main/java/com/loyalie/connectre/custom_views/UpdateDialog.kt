package com.loyalie.connectre.custom_views

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Window
import android.view.WindowManager
import com.loyalie.connectre.R
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import kotlinx.android.synthetic.main.update_dialog.*


class UpdateDialog(val contextIn: Context) : Dialog(contextIn) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.update_dialog)
        window?.setBackgroundDrawable(ColorDrawable(0))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    fun pop(isForceUpdate: Boolean) {
        setCanceledOnTouchOutside(!isForceUpdate)
        if (isForceUpdate) {
            titleTV.text = "Current Version Unsupported"
            descTV.text =
                " Current version of Club Benefique is no longer supported please update the app"
            cancelTV.setGone()
        } else {
            titleTV.text = "New Version Available"
            descTV.text = " New Version of Club Benefique is available, update now?"
            cancelTV.setOnClickListener { dismiss() }
            cancelTV.setVisible()
        }

        okTV.setOnClickListener {
            val appPackageName = contextIn.packageName
            try {
                contextIn.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                contextIn.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }

        }

        show()
    }

    fun remove() {
        dismiss()
        cancel()
    }


}