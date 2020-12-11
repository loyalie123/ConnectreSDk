package com.loyalie.connectre.ui.refer_contact

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.ReferredUserStatusItem
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.setGone

class ReferSuccessDialog(
    context: Context,
    private val programName: String,
    private val newUsers: List<ReferredUserStatusItem>,
    private val oldUsers: List<ReferredUserStatusItem>,
    private val invalid: List<ReferredUserStatusItem>,
    private val listener: OnSuccessDismissListener,
    private val isAfterSuccess: Boolean = true
) : Dialog(context) {


    init {

        // window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.refer_success_popup)
        val countTV = findViewById(R.id.countTV) as TextView
        val programNameTV = findViewById(R.id.programNameTV) as TextView
        val addedLabelTV = findViewById(R.id.addedLabelTV) as TextView
        val totalUsers = newUsers.union(oldUsers).union(invalid)
        programNameTV.setText(programName)
        countTV.setText(newUsers.size.toString() + " of " + totalUsers.size.toString())
        if (newUsers.size == 0) {
            countTV.setGone()
            addedLabelTV.setText("Unable to refer selected contacts")
        } else if (newUsers.size == 1) addedLabelTV.setText("contact successfully referred")
        val referSuccessRecycler = findViewById(R.id.referSuccessRecycler) as RecyclerView
        val referalSuccessAdapter = ReferralSuccessAdapter(totalUsers.toList())
        referSuccessRecycler.setHasFixedSize(true)
        referSuccessRecycler.setLayoutManager(
            LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
        )
        referSuccessRecycler.setAdapter(referalSuccessAdapter)
        show()
        setOnDismissListener { if (isAfterSuccess) listener.onDismiss() }
        programNameTV.setBackgroundColor(ConnectReApp.themeColor)
        addedLabelTV.setBackgroundColor(ConnectReApp.themeColor)
        countTV.setBackgroundColor(ConnectReApp.themeColor)
    }

    interface OnSuccessDismissListener {
        fun onDismiss()
    }
}