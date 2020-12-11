package com.loyalie.connectre.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.EventItem
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.convertToMMMDDYYY
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_event_item.view.*

class HomeEventsAdapter(
    private val events: List<EventItem>,
    private val picasso: Picasso,
    context: Context,
    private val eventClick: OnEventClickListener
) : PaginatingAdapter<EventItem>(context, events, RecyclerView.HORIZONTAL) {


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_event_item, parent, false)
        return EventVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventVH) holder.bind(events[position])
    }


    inner class EventVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                eventClick.onEventClick(events[adapterPosition], it.eventIV)
            }
        }

        fun bind(event: EventItem) {

            itemView.run {
                eventTitleTV.text = event.eventName
                eventDescriptionTV.text = event.eventDescription
                eventDateTV.text = event.eventStartDate.convertToMMMDDYYY()
                eventIV.loadUrl(event.eventImage ?: "", picasso)
                ViewCompat.setTransitionName(eventIV, event.eventImage)
            }

        }
    }

    interface OnEventClickListener {
        fun onEventClick(event: EventItem, v: View)
    }
}