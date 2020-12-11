package com.loyalie.connectre.custom_views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.convertToHHMMA
import com.loyalie.connectre.util.convertToMMMDDYYY
import com.loyalie.connectre.util.setVisible
import kotlinx.android.synthetic.main.dialog_offer.*

class OfferDialog(
    contextIn: Context,
    desc: String,
    type: Int,
    date: String,
    code: String
) : Dialog(contextIn) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_offer)
        window?.setBackgroundDrawable(ColorDrawable(0))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        programTitleTV.setText(desc)
        if (type == 1) {
            offerImage.visibility = View.GONE
            offerCodeTV.visibility = View.GONE
            offerDescriptionTV.text = "Claimed on " + date.convertToMMMDDYYY()
            offerTimeTV.setVisible()
            offerTimeTV.text = date.convertToHHMMA()

        } else if (type == 0) {
            offerImage.visibility = View.VISIBLE
            offerImage.setImageResource(R.drawable.iv_offer_eligible)
            offerCodeTV.visibility = View.VISIBLE
            offerCodeTV.setText(code)
            offerDescriptionTV.visibility = View.VISIBLE
            offerTimeTV.visibility = View.GONE
            offerDescriptionTV.text = "Use this code to claim your reward."
            val extractedColor = ConnectReApp.themeColor
            offerCodeTV.setTextColor(extractedColor)
        } else if (type == 3) {
            offerImage.visibility = View.GONE
            offerCodeTV.visibility = View.GONE
            offerTimeTV.visibility = View.GONE
            offerDescriptionTV.text = "Expired"
        }

        setCanceledOnTouchOutside(true)
        show()

    }

    fun remove() {
        dismiss()
        cancel()
    }
}