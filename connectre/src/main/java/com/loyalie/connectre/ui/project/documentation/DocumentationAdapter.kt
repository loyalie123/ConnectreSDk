package com.loyalie.connectre.ui.project.documentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.DocumentItem
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.openInBrowser
import kotlinx.android.synthetic.main.documentation_item.view.*

class DocumentationAdapter(
    context: Context,
    private val docs: List<DocumentItem>
) : PaginatingAdapter<DocumentItem>(context, docs) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.documentation_item, parent, false)
        return DocumentVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DocumentVH) holder.bind(docs[position])
    }


    inner class DocumentVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.setOnClickListener {
                it.context.openInBrowser(docs[adapterPosition].docUrl)
            }
        }

        fun bind(document: DocumentItem) {
            itemView.pdfHeadingTV.setText(document.docName)
        }
    }
}