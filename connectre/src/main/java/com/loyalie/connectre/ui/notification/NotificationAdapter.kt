package com.loyalie.connectre.ui.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.NotificationItem
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.Utils
import com.loyalie.connectre.util.gethhmmaFromApiDateString
import kotlinx.android.synthetic.main.notification_row_item.view.*
import kotlin.reflect.KFunction2

class NotificationAdapter(
    contextIn: Context,
    private val notificationList: List<NotificationItem>,
    val listener: KFunction2<@ParameterName(name = "notification") NotificationItem, @ParameterName(
        name = "position"
    ) Int, Unit>
) : PaginatingAdapter<NotificationItem>(contextIn, notificationList) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_row_item, parent, false)
        return NotificationVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationVH) {
            holder.bind(notificationList[position])
        }
    }

    inner class NotificationVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.setOnClickListener {
                listener(notificationList[adapterPosition],adapterPosition)
            }

        }

        fun bind(item: NotificationItem) {
            val currentTimeInSeconds = System.currentTimeMillis() / 1
            itemView.run {
                if (item.read_status == 0) {
                    val extractedColor = ConnectReApp.themeColor
                    notificationTitleTV.setTextColor(extractedColor)
                } else {
                    notificationTitleTV.setTextColor(
                        ContextCompat.getColor(
                            ConnectReApp.instance,
                            R.color.textColorGrey
                        )
                    )
                }
                notificationTitleTV.setText(item.title)
                notificationDescriptionTV.setText(item.message)
                (item.timestamp?.gethhmmaFromApiDateString())?.let {
                    notificationDateTV.text =
                        Utils.formatAsTimeAgo(it, currentTimeInSeconds - it.time)
                }
            }
        }
    }
}