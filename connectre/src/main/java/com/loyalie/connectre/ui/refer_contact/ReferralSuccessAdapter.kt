package com.loyalie.connectre.ui.refer_contact

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.ReferredUserStatusItem
import com.loyalie.connectre.util.ConnectReApp
import kotlinx.android.synthetic.main.refer_success_single.view.*

class ReferralSuccessAdapter(private val userStatusItems: List<ReferredUserStatusItem>) :
    RecyclerView.Adapter<ReferralSuccessAdapter.ReferSuccessVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferSuccessVH {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.refer_success_single, parent, false)
        return ReferSuccessVH(v)
    }

    override fun onBindViewHolder(holder: ReferSuccessVH, position: Int) {
        val userStatusItem = userStatusItems[position]
        holder.itemView.referNameTV.setText(userStatusItem.userName)
        holder.itemView.referNumberTV.setText(userStatusItem.user_phone)
        if (userStatusItem.status == 1) {
            holder.itemView.successImg.setVisibility(View.VISIBLE)
            holder.itemView.existingMemberTV!!.setVisibility(View.GONE)
        } else if (userStatusItem.status == 4) {
            holder.itemView.successImg.setVisibility(View.GONE)
            holder.itemView.existingMemberTV.setVisibility(View.VISIBLE)
            holder.itemView.existingMemberTV.setText("Invalid\n number")
        } else {
            holder.itemView.successImg.setVisibility(View.GONE)
            holder.itemView.existingMemberTV.setVisibility(View.VISIBLE)
            holder.itemView.existingMemberTV.setText("Existing\n member")
        }

    }

    override fun getItemCount(): Int {
        return userStatusItems.size
    }

    inner class ReferSuccessVH(itemView: View) : RecyclerView.ViewHolder(itemView) {


        init {
            itemView.successImg.setColorFilter(ConnectReApp.themeColor, PorterDuff.Mode.SRC_IN)
        }

    }
}