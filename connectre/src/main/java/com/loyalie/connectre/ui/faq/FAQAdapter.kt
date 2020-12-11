package com.loyalie.connectre.ui.faq

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.FaqItem
import com.loyalie.connectre.util.PaginatingAdapter
import kotlinx.android.synthetic.main.faq_item.view.*

class FAQAdapter(context: Context, val faqList: List<FaqItem>, private val isForProject: Boolean) :
    PaginatingAdapter<FaqItem>(context, faqList) {
    private var expPos = -1

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return FAQVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FAQVH) holder.bind(faqList[position])
    }


    inner class FAQVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.faqTitle.setOnClickListener {
                if (adapterPosition == expPos) {
                    expPos = -1
                    notifyItemChanged(adapterPosition)
                } else {
                    notifyItemChanged(expPos)
                    expPos = adapterPosition
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        fun bind(item: FaqItem) {
            if (adapterPosition == expPos) {
                itemView.arrowIV.animate().rotation(-90f).setDuration(200).start()
                itemView.answerTV.visibility = View.VISIBLE
            } else {
                itemView.arrowIV.animate().rotation(90f).setDuration(200).start()
                itemView.answerTV.visibility = View.GONE
            }
            if (isForProject) {
                itemView.qnTV.text = item.question
                itemView.answerTV.text = item.answer
            } else {
                itemView.qnTV.text = item.faqQues
                itemView.answerTV.text = item.faqAns
            }

        }
    }


}