package com.loyalie.connectre.ui.offer

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.custom_views.OfferDialog
import com.loyalie.connectre.data.OfferItem
import com.loyalie.connectre.util.ConnectReApp
import kotlinx.android.synthetic.main.loader_item.view.*
import kotlinx.android.synthetic.main.offer_title_item.view.*
import kotlinx.android.synthetic.main.offer_type_claimed_item.view.*
import kotlinx.android.synthetic.main.offer_type_eligible_tem.view.*
import kotlinx.android.synthetic.main.offer_type_refer.view.*

class OfferDetailsAdapter(
    val activity: Activity,
    val offerList: List<OfferItem>,
    val programId: String,
    var programName: String,
    var programDesc: String,
    val listner: onSharecallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val intArray = SparseIntArray(offerList.size)

    protected var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2
    private val VIEWTYPE_TITLE = 3
    val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEWTYPE_LOADER) {
            val view = mInflater.inflate(R.layout.loader_item, parent, false)
            return LoaderViewHolder(view, activity)
        } else if (viewType == VIEWTYPE_ITEM) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.offer_type_claimed_item, parent, false)
            return OfferVH(v)
        } else if (viewType == VIEWTYPE_TITLE) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.offer_title_item, parent, false)
            return TitleVH(v)
        }

        throw IllegalArgumentException("Invalid ViewType: " + viewType)
    }

    override fun getItemCount(): Int {
        return if (offerList.isEmpty()) 1
        else offerList.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEWTYPE_TITLE
        else if (position == itemCount - 1) VIEWTYPE_LOADER
        else VIEWTYPE_ITEM

    }

    fun showLoading(status: Boolean) {
        showLoader = status
        notifyItemChanged(offerList.size + 1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoaderViewHolder) {
            if (showLoader) {
                holder.itemView.loader?.visibility = View.VISIBLE
            } else {
                holder.itemView.loader?.visibility = View.GONE
            }
            return
        } else if (holder is TitleVH) {
            holder.itemView.run {
                programNameTV.text = programName
                programDescTV.text = programDesc
            }
        } else {
            val extractedColor = ConnectReApp.themeColor
            val offer = offerList[position - 1]
            when {
                offerList[position - 1].rewardStatus == 1 -> {
                    holder.itemView.offerClaimedCL.visibility = View.VISIBLE
                    holder.itemView.offerEligibleCL.visibility = View.GONE
                    holder.itemView.offerReferCL.visibility = View.GONE
                    holder.itemView.setOnClickListener {
                        OfferDialog(
                            activity,
                            offer.rewardDescription,
                            offer.rewardStatus,
                            offer.claimDate,
                            offer.rewardCode ?: ""
                        )
                    }
                    if (position == 1)
                        holder.itemView.topView.visibility = View.GONE
                    else
                        holder.itemView.topView.visibility = View.VISIBLE

                    holder.itemView.titleTV.setText(offerList[position - 1].rewardName)
                }
                offerList[position - 1].rewardStatus == 0 -> {
                    holder.itemView.offerClaimedCL.visibility = View.GONE
                    holder.itemView.offerEligibleCL.visibility = View.VISIBLE
                    holder.itemView.offerReferCL.visibility = View.GONE

                    holder.itemView.offerEligibleView.setColorFilter(
                        extractedColor,
                        PorterDuff.Mode.SRC_IN
                    );
                    holder.itemView.offerEligibleTV.setTextColor(extractedColor)
                    holder.itemView.setOnClickListener {
                        OfferDialog(
                            activity,
                            offer.rewardDescription ?: "",
                            offer.rewardStatus,
                            offer.claimDate ?: "",
                            offer.rewardCode ?: ""
                        )
                    }
                    if (position == 1)
                        holder.itemView.eligibleTopView.visibility = View.GONE
                    else
                        holder.itemView.eligibleTopView.visibility = View.VISIBLE

                    holder.itemView.titleElgblTV.setText(offerList[position - 1].rewardName)

                }

                offerList[position - 1].rewardStatus == 3 -> {
                    holder.itemView.offerClaimedCL.visibility = View.GONE
                    holder.itemView.offerEligibleCL.visibility = View.GONE
                    holder.itemView.offerReferCL.visibility = View.VISIBLE
//                    holder.itemView.setOnClickListener {
//                        ReferContactListActivity.start(activity, programId, programName)
//                    }
                    if (position == 1)
                        holder.itemView.referTopView.visibility = View.GONE
                    else
                        holder.itemView.referTopView.visibility = View.VISIBLE
                }

                offerList[position - 1].rewardStatus == 4 -> {
                    holder.itemView.offerClaimedCL.visibility = View.GONE
                    holder.itemView.offerEligibleCL.visibility = View.GONE
                    holder.itemView.offerReferCL.visibility = View.VISIBLE
                    holder.itemView.setOnClickListener {
                        listner.onShareCallback()
                    }
                    if (position == 1)
                        holder.itemView.referTopView.visibility = View.GONE
                    else
                        holder.itemView.referTopView.visibility = View.VISIBLE

                    holder.itemView.referTitleTV.setText(offerList[position - 1].rewardName)
                    holder.itemView.offerReferTV.setText("On Referring " + offerList[position - 1].visitToGo.toString())
                }
            }
        }
    }


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        intArray.put(holder.adapterPosition, 0)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
        intArray.delete(holder.adapterPosition)
    }


    inner class OfferVH(view: View) : RecyclerView.ViewHolder(view) {

        fun rotateOfferImageBackground(dy: Int) {
            val currentRotn = intArray.get(adapterPosition, 0)
            itemView.offerClaimedIV.rotation = currentRotn + (dy).toFloat()
            itemView.offerEligibleView.rotation = currentRotn + (dy).toFloat()
            itemView.offerReferView.rotation = currentRotn + (dy).toFloat()
            intArray.put(adapterPosition, currentRotn + (dy))
        }


    }

    inner class TitleVH(view: View) : RecyclerView.ViewHolder(view) {


    }

    class LoaderViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {


        init {
            itemView.loader?.indeterminateDrawable?.setColorFilter(
                ConnectReApp.themeColor, PorterDuff.Mode.MULTIPLY
            )
        }


    }

    public interface onSharecallback {
        fun onShareCallback()
    }

}