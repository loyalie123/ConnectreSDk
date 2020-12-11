package com.loyalie.connectre.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.DocumentItem
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.toast
import kotlinx.android.synthetic.main.document_item.view.*


class DocumentsAdapter(
    context: Context,
    private val docs: List<DocumentItem>
) : PaginatingAdapter<DocumentItem>(context, docs) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.document_item, parent, false)
        return DocumentVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DocumentVH) holder.bind(docs[position])
    }


    inner class DocumentVH(v: View) : RecyclerView.ViewHolder(v) {

        init {

        }

        fun bind(document: DocumentItem) {
            itemView.apply {
                downloadIv.setOnClickListener {
                    "Document downloaded successfully".toast(context)
                }
                shareIv.setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"

                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            }
        }
    }
}