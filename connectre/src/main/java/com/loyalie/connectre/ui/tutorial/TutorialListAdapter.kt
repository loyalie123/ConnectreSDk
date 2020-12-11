package com.loyalie.connectre.ui.tutorial

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.TutorialItem
import com.loyalie.connectre.ui.tutorial.details.TutorialDetailsActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tutorial_list_item.view.*

class TutorialListAdapter(
    contextIn: Context,
    private val picasso: Picasso,
    private val tutorialList: List<TutorialItem>
) : PaginatingAdapter<TutorialItem>(contextIn, tutorialList) {
    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.tutorial_list_item, parent, false)
        return TutorialVH(v, parent.context)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TutorialVH).bind(tutorialList[position])
    }

    inner class TutorialVH(v: View, val context: Context) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                with(
                    Intent(context, TutorialDetailsActivity::class.java).putExtra(
                        "tutorial",
                        tutorialList[adapterPosition]
                    )
                ) {
                    context.startActivity(this)
                }
            }
        }

        fun bind(item: TutorialItem) {
            itemView.run {
                tutorialTitleTV.setText(item.appTutorialTitle)
                tutorialDescriptionTV.setText(item.appTutorialDesc)
                item.appTutorialLink?.let {
                    val url = "https://img.youtube.com/vi/" + it + "/hqdefault.jpg"
                    tutorialMainIV.loadUrl(url, picasso)
                }
            }
        }
    }
}