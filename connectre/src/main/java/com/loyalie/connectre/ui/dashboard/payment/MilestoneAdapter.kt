package com.loyalie.connectre.ui.dashboard.payment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.MilestoneList
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.milestone_item.view.*


class MilestoneAdapter(
    context: Context,
    private val docs: List<MilestoneList>
) : PaginatingAdapter<MilestoneList>(context, docs) {
    private var animationUp: Animation? = null
    var animationDown: Animation? = null
    private val COUNTDOWN_RUNNING_TIME: Long = 500
    var dummyarray: ArrayList<Int> = ArrayList<Int>()
    var params: ConstraintLayout.LayoutParams? = null
    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.milestone_item, parent, false)
        return DocumentVH(v)
    }

    init {
        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);


    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DocumentVH) holder.bind(docs[position])
    }


    inner class DocumentVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            params = itemView.view1.layoutParams as ConstraintLayout.LayoutParams
        }

        fun bind(document: MilestoneList) {
            itemView.apply {


                    paymentDateTv.setVisible()
                    paymntDateValue.setVisible()
                    statusValueTv.setVisible()
                    statusTv.setVisible()
                    constraintInner.setGone()
                    showMoreconstraint.setGone()
                    invoiceIdTv.setGone()
                    invoiceIdvalueTv.setGone()

                    paymntDateValue.text = document.milestoneDate.convertToddMMyyy()
                    titleTv.text=document.allotmentDays

                    priceTv.text = "Rs "+document.billingAmount
                    paymntDateValue.text = document.milestoneDate.convertToddMMyyy()
                    var str=document.billingStatus.toLowerCase()
                    if (str=="billed") {
                        statusValueTv.setTextColor(Color.parseColor("#0088cf"))

                    } else {

                        statusValueTv.setTextColor(Color.parseColor("#ea2c00"))

                    }

                    str=  str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                str=str.replace("_"," ")
                str=str.capitalizeWords()
                  statusValueTv.text = str
                paymentDateTv.text = if (str == "Billed") "Payment Date" else "Tentative Payment Date"
     /*           if (dummyarray.contains(document.milestoneId)) {
//        if (item.isOpen) {
                    constraintInner.setVisible()

                    showMoreTv.text = "Show Less"

                    showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_point_to_up,
                        0
                    )
                } else {
                    constraintInner.setGone()

                    showMoreTv.text = "Show More"

                    showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_point_to_right,
                        0
                    )
                }*/
                shareIv.setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }

                downloadTV.setOnClickListener {
                    "Document Downloaded Successfully".toast(context)
                }
            }
        }
    }
}