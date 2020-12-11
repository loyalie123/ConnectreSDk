package com.loyalie.connectre.ui.dashboard.payment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.DemandList
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.demand_item.view.*


class DemandsAdapter(
    context: Context,


    val demand: ArrayList<DemandList>

) : PaginatingAdapter<DemandList>(context, demand) {
//    private var animationUp: Animation? = null
//    var animationDown: Animation? = null
    private val COUNTDOWN_RUNNING_TIME: Long = 500
    var dummyarray: ArrayList<Int> = ArrayList<Int>()
    var params: ConstraintLayout.LayoutParams? = null
    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.demand_item, parent, false)
        return DocumentVH(v)
    }

    init {
//        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
//        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);


    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is DocumentVH) holder.bind(demand[position])
        params = holder.itemView.view1.layoutParams as ConstraintLayout.LayoutParams
        val demandList = demand[position]

        if (demandList.cgstValue == 0.0 && demandList.sgstValue == 0.0 && demandList.serviceTaxValue != 0.0) {
            holder.itemView.cgstTv.text = "Service Tax"
            holder.itemView.sgstTv.text = "SBC"
            holder.itemView.serviceTv.text = "KKC"
            holder.itemView.sbsTv.text = "VAT"
            holder.itemView.tdsTv.text = "TDS"
             holder.itemView.cgstValueTv.text = "Rs " + demandList.serviceTaxValue
            holder.itemView.sgstValueTv.text = "Rs " + demandList.sbcValue
            holder.itemView.tdsValueTv.text = "Rs " + demandList.tdsValue
            holder.itemView.serviceValueTv.text = "Rs " + demandList.kkcValue
            holder.itemView.sbsValueTv.text = "Rs " + demandList.vatValue
            holder.itemView.sbsTv.setVisible()
            holder.itemView.sbsValueTv.setVisible()
            holder.itemView.tdsTv.setVisible()
            holder.itemView.tdsValueTv.setVisible()
            holder.itemView.cgstTv.setVisible()
            holder.itemView.cgstValueTv.setVisible()
            holder.itemView.sgstTv.setVisible()
            holder.itemView.sgstValueTv.setVisible()
            holder.itemView.serviceTv.setVisible()
            holder.itemView.serviceValueTv.setVisible()

        } else if (demandList.sbcValue == 0.0 && demandList.kkcValue == 0.0 && demandList.serviceTaxValue == 0.0 && demandList.vatValue == 0.0 && demandList.cgstValue != 0.0) {
            holder.itemView.cgstTv.text = "CGST"
            holder.itemView.sgstTv.text = "SGST"
            holder.itemView.serviceTv.text = "TDS"
            holder.itemView.cgstValueTv.text = "Rs " + demandList.cgstValue
            holder.itemView.sgstValueTv.text = "Rs " + demandList.sgstValue
            holder.itemView.serviceValueTv.text = "Rs " + demandList.tdsValue
            holder.itemView.sbsTv.setGone()
            holder.itemView.sbsValueTv.setGone()
            holder.itemView.tdsTv.setGone()
            holder.itemView.tdsValueTv.setGone()

            holder.itemView.serviceTv.setVisible()
            holder.itemView.serviceValueTv.setVisible()
            holder.itemView.cgstTv.setVisible()
            holder.itemView.cgstValueTv.setVisible()
            holder.itemView.sgstTv.setVisible()
            holder.itemView.sgstValueTv.setVisible()
        } else {
            holder.itemView.sbsTv.setGone()
            holder.itemView.sbsValueTv.setGone()
            holder.itemView.tdsTv.setGone()
            holder.itemView.tdsValueTv.setGone()
            holder.itemView.cgstTv.setGone()
            holder.itemView.cgstValueTv.setGone()
            holder.itemView.sgstTv.setGone()
            holder.itemView.sgstValueTv.setGone()
            holder.itemView.serviceTv.setGone()
            holder.itemView.serviceValueTv.setGone()
        }



        holder.itemView.invoiceIdvalueTv.text = demandList.invoiceNumber
        holder.itemView.titleTv.text = demandList.allotmentDays

        holder.itemView.priceTv.text = "Rs " + demandList.billingTotalAmountDue

        if (dummyarray.contains(demandList.id)) {
//        if (item.isOpen) {
            holder.itemView.constraintInner.setVisible()
            holder.itemView.showMoreTv.text = "Show Less"

            holder.itemView.showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_point_to_up,
                0
            )
        } else {
            holder.itemView.constraintInner.setGone()

            holder.itemView.showMoreTv.text = "Show More"

            holder.itemView.showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_point_to_right,
                0
            )
        }

        if (demandList.billingTotalAmountDue == 0.0) {
            holder.itemView.showMoreconstraint.setGone()
        } else
            holder.itemView.showMoreconstraint.setVisible()
        holder.itemView.showMoreTv.setOnClickListener {
            if (dummyarray.contains(demandList.id)) {
            /*    holder.itemView.constraintInner.startAnimation(animationUp)
                val countDownTimerStatic: CountDownTimer = object : CountDownTimer(
                    COUNTDOWN_RUNNING_TIME,
                    16
                ) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {

                    }
                }
                countDownTimerStatic.start()*/
                holder.itemView.showMoreTv.text = "Show More"
                holder.itemView.constraintInner.setGone()

                holder.itemView.showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_point_to_right,
                    0
                )
//                        .isOpen = false
                val pos = dummyarray.indexOf(demandList.id)
                dummyarray.removeAt(pos)
            } else {
                holder.itemView.constraintInner.setVisible()
//                        constraintInner.startAnimation(animationDown)
                holder.itemView.showMoreTv.text = "Show Less"
                if (!dummyarray.contains(demandList.id))
                    dummyarray.add(demandList.id)
             /*   val top: Int = getPixelValue(
                    context,
                    12
                )
                params?.setMargins(
                    0,
                    top,
                    0,
                    0
                ) //substitute parameters for left, top, right, bottom

                holder.itemView.view1.layoutParams = params*/
                holder.itemView.showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_point_to_up,
                    0
                )
            }

            holder.itemView.shareIv.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"

                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
            holder.itemView.downloadTV.setOnClickListener {
                "Document Downloaded Successfully".toast(context)
            }
        }
    }


    inner class DocumentVH(v: View) : RecyclerView.ViewHolder(v) {

        init {

        }

    }
}