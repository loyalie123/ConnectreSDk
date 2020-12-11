package com.loyalie.connectre.ui.developers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.DeveloperItem
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.loadUrlWithPhVendor
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.developer_item.view.*

class DeveloperAdapter(
    private val listener: OnSelectionCallBack,
    private val developers: List<DeveloperItem>,
    private val picasso: Picasso
) : RecyclerView.Adapter<DeveloperAdapter.DeveloperVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.developer_item, parent, false)
        return DeveloperVH(v)
    }

    override fun getItemCount() = developers.size

    override fun onBindViewHolder(holder: DeveloperVH, position: Int) {
        val developer = developers[position]
        holder.itemView.run {
            //            devBgIV.loadUrl(developer.vendor_image?:"",picasso)
            devLogoIV.loadUrlWithPhVendor(developer.vendor_logo ?: "", picasso)
            nameTv.setText(developer.vendor_name)
        }

    }

    inner class DeveloperVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.setOnClickListener {
                val dev = developers[adapterPosition]
                try {
                    val colorCode = dev.colorcode ?: ""
                    if (colorCode.contains("#"))
                        ConnectReApp.themeColor = Color.parseColor(colorCode)
                    else ConnectReApp.themeColor = Color.parseColor("#" + colorCode)
                } catch (e: Exception) {
                    ConnectReApp.themeColor = ContextCompat.getColor(it.context, R.color.black)
                }
                listener.onSelection(dev)

            }
        }
    }

    interface OnSelectionCallBack {
        fun onSelection(dev: DeveloperItem)
    }
}