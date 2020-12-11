package com.loyalie.connectre.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.ProjectItem
import com.loyalie.connectre.ui.project.ProjectActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_project_item.view.*

class HomeProjectsAdapter(
    private val projects: List<ProjectItem>,
    private val picasso: Picasso,
    private val vendorId: String,
    context: Context
) : PaginatingAdapter<ProjectItem>(context, projects, RecyclerView.HORIZONTAL) {


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ProjectVH) viewHolder.bind(projects[position])

    }

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.home_project_item, parent, false)

        return ProjectVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProjectVH) holder.bind(projects[position])
    }

    inner class ProjectVH(v: View) : RecyclerView.ViewHolder(v) {


        fun bind(project: ProjectItem) {
            itemView.projectIV.loadUrl(project.projectLogo ?: "", picasso)
            itemView.projectNameTV.text = project.projectName
            val bhk = project.apartment?.split(",")?.toList()?.filter { it.isNotBlank() }
            val bhkTxt = StringBuilder()
            bhk?.forEachIndexed { index: Int, bh ->
                if (index == 0) bhkTxt.append(bh)
                else if (index == bhk.count() - 1) {
                    bhkTxt.append("&")
                    bhkTxt.append(bh)
                } else {
                    bhkTxt.append(",")
                    bhkTxt.append(bh)
                }

            }
            itemView.projectTypeTV.text = bhkTxt.append(" Bed Residences")
            itemView.setOnClickListener {

                ProjectActivity.start(
                    it.context,
                    project.projectId,
                    vendorId,
                    project.projectName ?: ""
                )
            }
//            try {
            /* val price = project.rate?.toFloat()?:0f
             val inCr = price/10000000
             if (inCr >= 1){
                 val roundedRate  = fmt(Math.round((inCr * 10)) / 10.0)
                 itemView.newTV.text = "₹ "+roundedRate+" Cr+"
             }
             else{
                 val inLakh = price/100000
                 val roundedRate  = fmt(Math.round((inLakh * 10)) / 10.0)
                 if (inLakh >= 1) itemView.newTV.text = "₹ "+roundedRate+" Lc+"
                 else itemView.newTV.text = "₹ "+project.rate+"+"
             }
         }catch (e : Exception){*/
            itemView.newTV.text =/* fmt((*/context.getString(R.string.Rs)+" "+ project.rate/*+"+").toDouble())*/
//            }


            project.projectBgImage?.let {
                itemView.projectIV.loadUrl(it, picasso)
            } ?: run {
                itemView.projectIV.loadUrl("", picasso)
            }
        }
    }

    fun fmt(d: Double): String? {
        if (d == d.toLong().toDouble())
            return String.format("%d", d.toLong())
        else
            return String.format("%s", d)
    }
}