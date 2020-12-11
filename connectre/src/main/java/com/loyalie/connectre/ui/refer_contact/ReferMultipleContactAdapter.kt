package com.loyalie.connectre.ui.refer_contact

import android.app.Dialog
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import kotlinx.android.synthetic.main.refer_multi_contact_item.view.*

class ReferMultipleContactAdapter(
    val listener: OnContactClickedParentInformer,
    val filteredArray: Array<String>,
    val dialog: Dialog,
    val pos: Int,
    var presentSelection: String
) : RecyclerView.Adapter<ReferMultipleContactAdapter.CntactVH>() {

    override fun getItemCount(): Int {
        return filteredArray.size
    }

    private var lastSelection = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CntactVH {
        val v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.refer_multi_contact_item, parent, false)
        return CntactVH(v)
    }

    override fun onBindViewHolder(holder: CntactVH, position: Int) {
        holder.itemView.multipleContactTV.setText(filteredArray[position])
        if (filteredArray[position].equals(presentSelection, ignoreCase = true)) {
            lastSelection = position
            holder.itemView.tickImg.visibility = View.VISIBLE
        } else
            holder.itemView.tickImg.visibility = View.INVISIBLE
        val extractedColor = ConnectReApp.themeColor
        holder.itemView.tickImg.setColorFilter(extractedColor, PorterDuff.Mode.SRC_IN)


        holder.itemView.setOnClickListener {
            notifyItemChanged(lastSelection)
            notifyItemChanged(position)
            presentSelection = filteredArray[position]
            listener.onContactParentClicked(filteredArray[position], dialog, pos)

        }
    }

    inner class CntactVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnContactClickedParentInformer {
        fun onContactParentClicked(number: String, dialog: Dialog, position: Int)
    }
}