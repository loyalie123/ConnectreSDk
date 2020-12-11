package com.loyalie.connectre.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.greifList
import com.loyalie.connectre.util.PaginatingAdapter
import kotlinx.android.synthetic.main.greif_item.view.*


class GreifAdapter(
    context: Context,
    private val docs: List<greifList>
) : PaginatingAdapter<greifList>(context, docs) {
    private var animationUp: Animation? = null
    var animationDown: Animation? = null
    private val COUNTDOWN_RUNNING_TIME: Long = 500
    var dummyarray: ArrayList<Int> = ArrayList<Int>()
    var params: ConstraintLayout.LayoutParams? = null
    var params1: ConstraintLayout.LayoutParams? = null
    var params2: ConstraintLayout.LayoutParams? = null
    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.greif_item, parent, false)
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
            params = itemView.profileIV.layoutParams as ConstraintLayout.LayoutParams
            params1 = itemView.statusGreifTv.layoutParams as ConstraintLayout.LayoutParams
            params2 = itemView.nameTv.layoutParams as ConstraintLayout.LayoutParams
        }

        fun bind(document: greifList) {
            itemView.apply {
                statusGreifTv.text = document.status

                if (document.status == "Pending")
                    statusGreifTv.setTextColor(Color.parseColor("#ea2c00"))
                else
                    statusGreifTv.setTextColor(Color.parseColor("#005e9a"))

                if (dummyarray.contains(document.id)) {
//        if (item.isOpen) {
                    constraintMain.setBackgroundResource(R.drawable.round_corner_withborder)
                    greifView.visibility = View.GONE

                    constraitInner.visibility = View.VISIBLE

                    downArrowIv.setImageResource(
                        R.drawable.ic_arrow_point_to_up
                    )
                } else {
                    constraintMain.setBackgroundResource(0)
                    greifView.visibility = View.VISIBLE
                    constraitInner.visibility = View.GONE
                    downArrowIv.setImageResource(
                        R.drawable.ic_arrow_point_to_right

                    )
                }
                constraintMain.setOnClickListener{
                    if (dummyarray.contains(document.id)) {
                        constraitInner.setVisibility(View.GONE);
                        constraitInner.startAnimation(animationUp);

                        constraintMain.setBackgroundResource(0)
                        greifView.visibility = View.VISIBLE
                        val countDownTimerStatic: CountDownTimer = object : CountDownTimer(
                            COUNTDOWN_RUNNING_TIME,
                            16
                        ) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                constraitInner.setVisibility(View.GONE)
                            }
                        }
                        countDownTimerStatic.start()


                        downArrowIv.setImageResource(
                            R.drawable.ic_arrow_point_to_right

                        )
//                        .isOpen = false
                        val pos = dummyarray.indexOf(document.id)
                        dummyarray.removeAt(pos)
                      /*  val top: Int = getPixelValue(
                            context,
                            16
                        )
                        val top1: Int = getPixelValue(
                            context,
                            27
                        )
                        params?.setMargins(
                            0,
                            top,
                            0,
                            0
                        )
                        params1?.setMargins(
                            0,
                            top1,
                            0,
                            0
                        )
                        params2?.setMargins(
                            0,
                            top1,
                            0,
                            0
                        )
                       //substitute parameters for left, top, right, bottom

                        profileIV.layoutParams = params
                        statusGreifTv.layoutParams=params1
                        nameTv.layoutParams=params2*/

                    } else {
                /*        val top: Int = getPixelValue(
                            context,
                            23
                        )
                        val top1: Int = getPixelValue(
                            context,
                            29
                        )
                        params?.setMargins(
                            0,
                            top,
                            0,
                            0
                        )
                        params1?.setMargins(
                            0,
                            top1,
                            0,
                            0
                        )
                        params2?.setMargins(
                            0,
                            top1,
                            0,
                            0
                        )
                         //substitute parameters for left, top, right, bottom

                        profileIV.layoutParams = params
                        statusGreifTv.layoutParams=params1
                        nameTv.layoutParams=params2*/
                        constraitInner.visibility = View.VISIBLE
                        constraitInner.startAnimation(animationDown)
                        constraintMain.setBackgroundResource(R.drawable.round_corner_withborder)
                        greifView.visibility = View.GONE


                        if (!dummyarray.contains(document.id))
                            dummyarray.add(document.id)


                        downArrowIv.setImageResource(R.drawable.ic_arrow_point_to_up)
//notifyItemChanged(position)
                    }
                }
            }
        }
        fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
            referencedIds.forEach { id ->
                rootView.findViewById<View>(id).setOnClickListener(listener)
            }
        }
    }
}
