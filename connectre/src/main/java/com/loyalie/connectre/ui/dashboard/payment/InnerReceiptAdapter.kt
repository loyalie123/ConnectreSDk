package com.loyalie.connectre.ui.dashboard.payment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.aggregatedReceipts
import kotlinx.android.synthetic.main.inner_item.view.*


class InnerReceiptAdapter(
    val context: Context,
    val key: List<aggregatedReceipts>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.inner_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {

//            keyTv.text = key.key[position]
            keyTv.text =key[position].paymentCategory
            valueTv.text = "Rs "+key[position].amount
//            valueTv.text = key.value[position]
        }
    }
    override fun getItemCount(): Int {
        return key.size
    }
}