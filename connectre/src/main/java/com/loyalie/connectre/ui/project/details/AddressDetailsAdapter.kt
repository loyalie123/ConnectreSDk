package com.loyalie.connectre.ui.project.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import kotlinx.android.synthetic.main.address_detail_item.view.*

class AddressDetailsAdapter(private val fieldPairs: List<Pair<String, String>>) :
    RecyclerView.Adapter<AddressDetailsAdapter.AddressVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.address_detail_item, parent, false)
        return AddressVH(v)
    }

    override fun getItemCount(): Int {
        return fieldPairs.size
    }

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        holder.bind(fieldPairs[position])
    }

    inner class AddressVH(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: Pair<String, String>) {
            itemView.headingHolderTV.text = item.first
            itemView.headingMainTV.text = item.second
        }
    }
}