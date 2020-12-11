package com.loyalie.connectre.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.BillingDummyList
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import kotlinx.android.synthetic.main.billing_item.view.*

class BillingAdapter(
    val context: Context,
    val list: List<BillingDummyList>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var animationUp: Animation? = null
    var animationDown: Animation? = null
    private val COUNTDOWN_RUNNING_TIME: Long = 500
    var dummyarray: ArrayList<Int> = ArrayList<Int>()
    override fun getItemCount(): Int {

        return list.size
    }

    init {
        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val arrylist = list[position]
        if(arrylist.name=="Total Paid"){
            holder.itemView.otherchargeTv.text="Towards Other Charges:"
            holder.itemView.inetresteTv.text="Towards Interest:"
            holder.itemView.const5.visibility=View.VISIBLE
            holder.itemView.interestValTv.text = "Rs " + arrylist.intereset
            holder.itemView.otherchargeValTv.text="Rs "+arrylist.towardsPaymentOtherCharges
        }
        else{
            holder.itemView.const5.visibility=View.GONE
            holder.itemView.otherchargeTv.text="Towards Interest:"
            holder.itemView.otherchargeValTv.text="Rs "+arrylist.intereset
        }

        holder.itemView.const1.setBackgroundColor(Color.WHITE)

        holder.itemView.agreementvalueTV.text = arrylist.name
        val nm = arrylist.value ?: ""
        holder.itemView.agreementValue.text = "Rs $nm"

        holder.itemView.agreementValue.setTextColor(Color.parseColor(arrylist.color))
        if (arrylist.name == "Total Outstanding")
            holder.itemView.view1.setGone()
        else
            holder.itemView.view1.setVisible()
        if (arrylist.status == 0)
            holder.itemView.downArrowIv.setGone()
        else
            holder.itemView.downArrowIv.setVisible()
        holder.itemView.constraintInner.setGone()
        holder.itemView.princupalValTv.text = "Rs " + arrylist.principal
        holder.itemView.tdsvaTv.text = "Rs " + arrylist.tds
        holder.itemView.gstValTv.text = "Rs " + arrylist.gst

        if (dummyarray.contains(arrylist.id)) {
//        if (item.isOpen) {
            holder.itemView.constraintInner.setVisible()



            holder.itemView.downArrowIv.setImageResource(R.drawable.ic_down_arrow)
        } else {
            holder.itemView.constraintInner.setGone()



            holder.itemView.downArrowIv.setImageResource(R.drawable.ic_right_arrow)
        }
        if (arrylist.name == "Total Unbilled Value") {
            holder.itemView.const4.setGone()
        } else {
            holder.itemView.const4.setVisible()
        }
        holder.itemView.const1.setOnClickListener {
            if (arrylist.status != 0) {
                if (dummyarray.contains(arrylist.id)) {
                    /*  holder.itemView.constraintInner.startAnimation(animationUp)
                      val countDownTimerStatic: CountDownTimer = object : CountDownTimer(
                          COUNTDOWN_RUNNING_TIME,
                          16
                      ) {
                          override fun onTick(millisUntilFinished: Long) {}
                          override fun onFinish() {*/
                    holder.itemView.constraintInner.setGone()
//                    }
//                }
//                countDownTimerStatic.start()
                    holder.itemView.downArrowIv.setImageResource(R.drawable.ic_right_arrow)


//                        .isOpen = false
                    val pos = dummyarray.indexOf(arrylist.id)
                    dummyarray.removeAt(pos)
                } else {
                    holder.itemView.constraintInner.setVisible()
//                holder.itemView.constraintInner.startAnimation(animationDown)

                    if (!dummyarray.contains(arrylist.id))
                        dummyarray.add(arrylist.id)
                    holder.itemView.downArrowIv.setImageResource(R.drawable.ic_down_arrow)

                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.billing_item, parent, false)
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to

    }
    /*  override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
          val v =
              LayoutInflater.from(parent.context).inflate(R.layout.billing_item, parent, false)
          return AdapterVH(v)
      }


      override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
  //        if (holder is AdapterVH) holder.bind(list[position])

      }

      override fun getItemCount(): Int {
          return list.size
      }

      inner class AdapterVH(v: View) : RecyclerView.ViewHolder(v) {


      }*/
}
