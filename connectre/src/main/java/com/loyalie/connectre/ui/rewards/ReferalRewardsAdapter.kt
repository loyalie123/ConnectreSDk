package com.loyalie.connectre.ui.rewards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.RewardItem
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reward_item.view.*

class ReferalRewardsAdapter(
    private val ctxt: Context,
    private val items: List<RewardItem>,
    private val isReward: Boolean,
    private val picasso: Picasso
) : PaginatingAdapter<RewardItem>(ctxt, items) {


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.referal_reward_item, parent, false)
        return RewardsVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RewardsVH) holder.bind(items[position])
    }


    inner class RewardsVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.rewardCodeTV.setTextColor(ConnectReApp.themeColor)

            /* itemView.setOnClickListener {
                 val url = items[adapterPosition].url()
                 if (!isReward && !url.isNullOrBlank()) {
                     it.context.openInBrowser(url)
                 }
             }*/
        }

        fun bind(item: RewardItem) {
            itemView.run {
                if (item.vendor_logo.isNullOrBlank()) rewardIV.setImageResource(R.drawable.placeholder)
                else rewardIV.loadUrl(item.vendor_logo!!, picasso)
                rewardNameTV.setText(item.reward_name)
                programNameTV.setText(item.reward_description)
                //val expiryTxt = if (isReward) "Offer available till " + item.expiry()
                //else "Available till " + item.expiry()
                // expiryDateTV.setText(expiryTxt)
                if (item.claim_direct.isNullOrBlank()) rewardCodeTV.setGone()
                else {
                    rewardCodeTV.setVisible()
                    rewardCodeTV.setText(item.claim_direct)
                }

                if (isReward) {
                    expiryDateTV.setVisible()
                    benfitExpiryDateTV.setGone()
                    expiryDateTV.text = "Offer available till " + item.expiry_date
                } else {
                    expiryDateTV.setGone()
                    benfitExpiryDateTV.setVisible()
                    benfitExpiryDateTV.text = "Available till " + item.expiry_date
                }
            }

        }

    }


}

