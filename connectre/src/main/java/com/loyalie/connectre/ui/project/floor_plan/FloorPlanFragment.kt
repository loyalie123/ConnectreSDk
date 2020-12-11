package com.loyalie.connectre.ui.project.floor_plan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_floor_plan.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class FloorPlanFragment : BaseFragment() {
    @Inject
    lateinit var picasso: Picasso


    private val images = ArrayList<GalleryItem>()
    lateinit var imagesAdapter: FloorPlanAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: FloorPlanVM

    lateinit var projectId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floor_plan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId =
            arguments?.getString(PROJECT_ID) ?: throw IllegalStateException("Project id required")
        val gallery = arguments?.getSerializable(FLOOR_PLANS) as ArrayList<GalleryItem>?
        if (gallery?.isEmpty() ?: true) defaultTV.setVisible()
        else defaultTV.setGone()
        if (gallery == null) return
        viewModel = viewModelProvider(viewModelFactory)
        images.clear()
        images.addAll(gallery)
        viewModel.setImages(gallery)
        initRV()

        viewModel.imagesHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    images.clear()
                    images.addAll(it.data)
                    imagesAdapter.showLoading(false)

                    imagesAdapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    imagesAdapter.showLoading(false)
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    imagesAdapter.showLoading(true)
                }
            }
        })
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(activity)
        floorPlanRV.layoutManager = layoutManager
        imagesAdapter = FloorPlanAdapter(requireContext(), images, picasso)
        floorPlanRV.adapter = imagesAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getImages(projectId, isInitial = false, isRefresh = false)
            }
        }

        floorPlanRV.addOnScrollListener(scrollListener)
    }

    companion object {
        const val FLOOR_PLANS = "floor_plans"
        const val PROJECT_ID = "project_id"
        fun newInstance(items: ArrayList<GalleryItem>, projectId: String) =
            FloorPlanFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(FLOOR_PLANS, items)
                    putString(PROJECT_ID, projectId)
                }
            }

    }


}
