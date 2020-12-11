package com.loyalie.connectre.ui.lead.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.LeadItem
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.PaginatingAdapter
import kotlinx.android.synthetic.main.program_status_item.view.*

class ProgramStatusAdapter(
    context: Context,
    private val items: List<LeadItem>
) : PaginatingAdapter<LeadItem>(context, items) {

    private val textColorGrey70 by lazy {
        ContextCompat.getColor(ConnectReApp.instance, R.color.textColorGreyAlpha70)
    }

    private val textColorGrey30 by lazy {
        ContextCompat.getColor(ConnectReApp.instance, R.color.textColorGreyAlpha30)
    }

    private val textColorGrey by lazy {
        ContextCompat.getColor(ConnectReApp.instance, R.color.textColorGrey)
    }


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.program_status_item, parent, false)
        return StatusVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StatusVH) holder.bind(items[position])

    }


    inner class StatusVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener { }
        }

        fun bind(lead: LeadItem) {
            itemView.apply {
                nameTV.setText(lead.userName)
                if (lead.referralStampStatus == 1) {
                    programStatusIV.visibility = View.VISIBLE
                    programStatusTV.text = context.getString(R.string.accepted)
                    programStatusTV.setTextColor(textColorGrey)
                    nameTV.setTextColor(textColorGrey)
                } else if (lead.referralStampStatus == 0 && lead.sfdcLeadStatus != null) {
                    programStatusIV.visibility = View.GONE
//                    programStatusTV.text =context.getString(R.string.pending)
                    programStatusTV.text = lead.sfdcLeadStatus.toString()
                    programStatusTV.setTextColor(textColorGrey70)
                    nameTV.setTextColor(textColorGrey)

                } else if (lead.referralStampStatus == 0 && lead.sfdcLeadStatus == null) {
                    programStatusIV.visibility = View.GONE
                    programStatusTV.text = "Open"
                    programStatusTV.setTextColor(textColorGrey70)
                    nameTV.setTextColor(textColorGrey)
                } else {
                    programStatusIV.visibility = View.GONE
                    programStatusTV.text = context.getString(R.string.declined)
                    programStatusTV.setTextColor(textColorGrey30)
                    nameTV.setTextColor(textColorGrey30)
                }
            }

        }
    }
}