package com.loyalie.connectre.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.propertiesList
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_property_item.view.*

class HomePropertyAdapter(
    private val properties: List<propertiesList>,
    private val picasso: Picasso,
    context: Context,
    private val propertiesClick: OnPropertyClickListner
) : PaginatingAdapter<propertiesList>(context, properties, RecyclerView.HORIZONTAL) {


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.home_property_item, parent, false)
        return EventVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventVH) holder.bind(properties[position])
    }


    inner class EventVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                propertiesClick.onPropertyClick(properties[adapterPosition], it.propertyIV)
            }
        }

        fun bind(properties: propertiesList) {

            itemView.run {
                propertyTitleTV.text = properties.title
                propertyDescriptionTV.text = properties.description
              if(  properties.amount!=null)
              {
                  properties.amount.apply {
                      amountTv.text="Rs "+properties.amount
                      amountTv.setVisible()
                  }
              }else{
                  amountTv.setGone()
              }

               if(properties.imageUrl!="") {
                   propertyIV.loadUrl(properties.imageUrl ?: "", picasso)
                   ViewCompat.setTransitionName(propertyIV, properties.imageUrl)   }


            }

        }
    }

    interface OnPropertyClickListner {
        fun onPropertyClick(properties: propertiesList, v: View)
    }
}