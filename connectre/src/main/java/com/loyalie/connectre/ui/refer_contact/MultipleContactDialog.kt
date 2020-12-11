package com.loyalie.connectre.ui.refer_contact

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loyalie.connectre.R
import kotlinx.android.synthetic.main.contact_listing_layout.*

class MultipleContactDialog(
    context: Context,
    filteredArray: Array<String>,
    pos: Int,
    presentSelection: String,
    name: String,
    val listener: OnContactClickedInformer
) :
    BottomSheetDialog(context),
    ReferMultipleContactAdapter.OnContactClickedParentInformer {

    override fun onContactParentClicked(number: String, dialog: Dialog, position: Int) {
        listener.onContactClicked(number, dialog, position)
    }

    init {
        setContentView(R.layout.contact_listing_layout)

        if (TextUtils.isEmpty(name))
            selContTitleTV.text = "Choose from available numbers"
        else
            selContTitleTV.text = "Choose from " + name + "'s available numbers"

        val multiAdapter =
            ReferMultipleContactAdapter(this, filteredArray, this, pos, presentSelection)
        contRecycler.setHasFixedSize(true)
        contRecycler.layoutManager = LinearLayoutManager(context)
        contRecycler.adapter = multiAdapter


    }

    interface OnContactClickedInformer {
        fun onContactClicked(number: String, dialog: Dialog, position: Int)
    }
}