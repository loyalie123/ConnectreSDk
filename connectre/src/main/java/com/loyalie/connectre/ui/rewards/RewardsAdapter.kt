package com.loyalie.connectre.ui.rewards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reward_item.view.*

class RewardsAdapter(
    private val ctxt: Context,
    private val items: List<RewardWrapper>,
    private val isReward: Boolean,
    private val picasso: Picasso
) : PaginatingAdapter<RewardWrapper>(ctxt, items) {


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reward_item, parent, false)
        return RewardsVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RewardsVH) holder.bind(items[position])
    }


    inner class RewardsVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.rewardCodeTV.setTextColor(ConnectReApp.themeColor)

            itemView.setOnClickListener {
                if (!isReward)
                    BenefitsDetailAct.start(context, items[adapterPosition])
                /*   val url = items[adapterPosition].url()
                   if (!isReward && !url.isNullOrBlank()) {
                       it.context.openInBrowser(url)
                   }*/
            }
        }

        fun bind(item: RewardWrapper) {
            itemView.run {
                if (item.img().isNullOrBlank()) rewardIV.setImageResource(R.drawable.placeholder)
                else rewardIV.loadUrl(item.img()!!, picasso)
                rewardNameTV.setText(item.title())
                programNameTV.setText(item.catName())
                benfitExpiryDateTV.setText(item.expiry())
                //val expiryTxt = if (isReward) "Offer available till " + item.expiry()
                //else "Available till " + item.expiry()
                // expiryDateTV.setText(expiryTxt)
                /* if (item.code().isNullOrBlank()) rewardCodeTV.setGone()
                 else {
                     rewardCodeTV.setVisible()
                     rewardCodeTV.setText(item.code())
                 }*/
                benfitExpiryDateTV.text = /*"Offer available till " +*/ item.expiry()
                /* if(isReward){
                     expiryDateTV.setVisible()
                     benfitExpiryDateTV.setGone()
                     expiryDateTV.text = "Offer available till " + item.expiry()
                 }else{
                     expiryDateTV.setGone()
                     benfitExpiryDateTV.setVisible()
                     benfitExpiryDateTV.text = "Available till " + item.expiry()
                 }*/
            }

        }

    }


}

interface RewardWrapper {
    fun title(): String
    fun subTitle(): String
    fun expiry(): String
    fun code(): String?
    fun img(): String?
    fun url(): String?
    fun catID(): Int?
    fun catName(): String?
    fun termsCondition(): String?

}