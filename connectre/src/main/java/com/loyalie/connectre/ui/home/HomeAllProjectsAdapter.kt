package com.loyalie.connectre.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.projectCategorys
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.PaginatingScrollListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_all_project_item.view.*
import java.util.*


class HomeAllProjectsAdapter(
    private var projects: ArrayList<projectCategorys>,
    private val picasso: Picasso,
    private var vendorId: String,
    context: Context, val listner: onRequestCallBack
) : PaginatingAdapter<projectCategorys>(context, projects, RecyclerView.HORIZONTAL) {
    var adapter: HomeProjectsAdapter? = null
    var projectScrollListener: PaginatingScrollListener? = null
    /* override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
         if (viewHolder is ProjectVH) viewHolder.bind(projects[position])

     }*/
    var viewPool = RecyclerView.RecycledViewPool();

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_all_project_item, parent, false)

        return ProjectVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*val displayMetrics = DisplayMetrics()
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
       holder.itemView.setLayoutParams(
            RecyclerView.LayoutParams(
                width / 3,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        )
*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.itemView.projectsRV.layoutManager = layoutManager

        adapter = HomeProjectsAdapter(projects[position].projects, picasso, vendorId, context)
        holder.itemView.projectsRV.adapter = adapter

        projectScrollListener =
            object : PaginatingScrollListener(layoutManager, direction = RecyclerView.HORIZONTAL) {
                override fun onLoadMore() {
                    listner.onGetProjects(vendorId, projects[position].pcatId, position, adapter!!)
                }
            }

        holder.itemView.projectsRV.setRecycledViewPool(viewPool);
        holder.itemView.projectsRV.addOnScrollListener(projectScrollListener!!)


        if (holder is ProjectVH) holder.bind(projects[position], position)
    }

    inner class ProjectVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
            /* itemView.setOnClickListener {

                 ProjectActivity.start(it.context,project.projectId,vendorId,project.projectName?:"")
             }*/
//             }
        }

        fun bind(project: projectCategorys, pos: Int) {
            if (project.projects.size == 0) {
                itemView.projectsTV.visibility = View.GONE
                itemView.projectsRV.visibility = View.GONE
            } else {

                itemView.projectsTV.text = project.categoryName
                itemView.projectsTV.isAllCaps = true
            }

        }

        lateinit var viewModel: HomeVM
        /* private fun initRV(project: projectCategorys, pos: Int) {
             viewModel.projectHolder.observe(context, Observer {
                 when (it) {
                     is ViewState.Success -> {
 //                    all_projects.clear()
 //                    all_projects.addAll(it.data)
 //                    projects.clear()

                         projects.addAll(it.data)


 //                    loadingDialog.remove()
                     adapter!!.notifyDataSetChanged()


                     }
                     is ViewState.Error -> {
 //                    loadingDialog.remove()
 //                    projectScrollListener?.setLoading(false)
 //                    adapter?.showLoading(false)
                     it.exception.toast(this)
                     }
                     is ViewState.Loading -> {
 //                    if (it.isInitial) loadingDialog.load()
 //                    else adapter?.showLoading(true)
                     }
                 }
             })

         }*/
    }


    interface onRequestCallBack {
        fun onGetProjects(
            vendorId: String,
            pcatId: Int,
            position: Int,
            adapter: HomeProjectsAdapter
        )
        fun notifyAdapter(adapter: HomeProjectsAdapter)
    }

}