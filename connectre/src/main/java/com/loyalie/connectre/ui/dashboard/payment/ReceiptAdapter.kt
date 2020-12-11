package com.loyalie.connectre.ui.dashboard.payment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.ReceiptList
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.receipt_item.view.*


class ReceiptAdapter(
    context: Context,


    val receipt: ArrayList<ReceiptList>

) : PaginatingAdapter<ReceiptList>(context, receipt) {
//    private var animationUp: Animation? = null
//    var animationDown: Animation? = null
//    private val COUNTDOWN_RUNNING_TIME: Long = 500
    var dummyarray: ArrayList<Int> = ArrayList<Int>()
    private var  values =ArrayList<String>()
    private var keysets =ArrayList<String>()

    var params: ConstraintLayout.LayoutParams? = null

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.receipt_item, parent, false)
        return DocumentVH(v)
    }

    init {
//        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
//        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is DocumentVH) holder.bind(receipt[position], position)
        val document=receipt[position]
//      holder.itemView.  constraintInner.setGone()
//        holder.itemView.   showMoreconstraint.setVisible()
        holder.itemView.  invoiceIdvalueTv.text = document.receiptNo
        holder.itemView.  titleTv.text = document.allotmentDays
        holder.itemView.   priceTv.text = "Rs " + document.totalAmount

        holder.itemView. innerRv.setNestedScrollingEnabled(false)
        val lm = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        holder.itemView.   innerRv.setLayoutManager(lm)
        val adapter1 =
            document.aggregatedReceipts?.let {
                InnerReceiptAdapter(
                    context!!,
                    it
                )
            }
        holder.itemView. innerRv.setAdapter(adapter1)
        holder.itemView.  innerRv.stopScroll()
        if (dummyarray.contains(document.receiptNo.toInt())) {
//        if (item.isOpen) {
            holder.itemView.      constraintInner.setVisible()

            holder.itemView.     showMoreTv.text = "Show Less"

            holder.itemView.    showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_point_to_up,
                0
            )
        } else {
            holder.itemView.    constraintInner.setGone()

            holder.itemView.      showMoreTv.text = "Show More"

            holder.itemView.      showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_point_to_right,
                0
            )
        }
        holder.itemView.    shareIv.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"

            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
        holder.itemView.    showMoreTv.setOnClickListener {
            if (dummyarray.contains(document.receiptNo.toInt())) {
           /*     holder.itemView.     constraintInner.startAnimation(animationUp)
                val countDownTimerStatic: CountDownTimer = object : CountDownTimer(
                    COUNTDOWN_RUNNING_TIME,
                    16
                ) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {*/
                        holder.itemView.constraintInner.setGone()
                   /* }
                }
                countDownTimerStatic.start()*/
                holder.itemView.    showMoreTv.text = "Show More"

            /*    val top: Int = getPixelValue(context, 26)
                params?.setMargins(
                    0,
                    top,
                    0,
                    0
                ) //substitute parameters for left, top, right, bottom

                holder.itemView.      view1.layoutParams = params
                holder.itemView.  showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_point_to_right,
                    0
                )*/
//                        .isOpen = false
                val pos = dummyarray.indexOf(document.receiptNo.toInt())
                dummyarray.removeAt(pos)
            } else {
                holder.itemView.    constraintInner.setVisible()
//                holder.itemView.   constraintInner.startAnimation(animationDown)
                holder.itemView.    showMoreTv.text = "Show Less"
                if (!dummyarray.contains(document.receiptNo.toInt()))
                    dummyarray.add(document.receiptNo.toInt())
           /*     val top: Int = getPixelValue(
                    context,
                    12
                )
                params?.setMargins(
                    0,
                    top,
                    0,
                    0
                ) //substitute parameters for left, top, right, bottom

                holder.itemView.    view1.layoutParams = params*/
                holder.itemView.    showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_point_to_up,
                    0
                )
            }
        }
        holder.itemView.   downloadTV.setOnClickListener {
            "Document Downloaded Successfully".toast(context)
        }
    }



    inner class DocumentVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
//            params = itemView.view1.layoutParams as ConstraintLayout.LayoutParams
        }


        fun bind(document: ReceiptList, position: Int) {
            itemView.apply {


            }
        }
    }

}