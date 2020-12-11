package com.loyalie.connectre.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.Utils
import com.loyalie.connectre.util.dpToPx

class Toasty(context: Context, isError: Boolean = false) : Toast(context) {

    init {
        duration = Toast.LENGTH_LONG

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflater.inflate(R.layout.toast_view, null)
        if (isError) {
            val red = ContextCompat.getColor(context, R.color.red)
            val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    red,
                    Utils.darkenColor(red, 0.8f)
                )
            )
            gd.cornerRadius = context.dpToPx(10).toFloat()
            toastView.setBackground(gd)
        } else {
            val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    ConnectReApp.themeColor,
                    Utils.darkenColor(ConnectReApp.themeColor, 0.8f)
                )
            )
            gd.cornerRadius = context.dpToPx(10).toFloat()
            toastView.setBackground(gd)
        }
        this.view = toastView
    }


    fun toast(msg: String) {
        view?.findViewById<TextView>(R.id.toast)?.setText(msg)
        show()
    }


    fun showAtTop(msg: String) {
        view?.findViewById<TextView>(R.id.toast)?.setText(msg)
        setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        show()
    }

    fun showBelow(v: View, msg: String) {
        view?.findViewById<TextView>(R.id.toast)?.setText(msg)
        setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, (v.y + v.height).toInt())
        show()
    }
}