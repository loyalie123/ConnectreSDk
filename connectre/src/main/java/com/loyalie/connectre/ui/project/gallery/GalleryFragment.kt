package com.loyalie.connectre.ui.project.gallery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_gallery.*
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso

    private val images = ArrayList<GalleryItem>()
    lateinit var imagesAdapter: GalleyAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: GalleryVM

    lateinit var projectId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId =
            arguments?.getString(PROJECT_ID) ?: throw IllegalStateException("Project id required")
        val gallery = arguments?.getSerializable(GELLERY) as ArrayList<GalleryItem>?
        if (gallery?.isEmpty() ?: true) defaultTV.setVisible()
        else defaultTV.setGone()
        if (gallery == null) return
        viewModel = viewModelProvider(viewModelFactory)
        images.clear()
        images.addAll(gallery)
        viewModel.setImages(gallery)
        initGalleryRV()

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

    private fun initGalleryRV() {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        imagesAdapter = GalleyAdapter(requireContext(), images, picasso)
        galleryRV.layoutManager = staggeredGridLayoutManager
        galleryRV.adapter = imagesAdapter
        scrollListener = object :
            PaginatingScrollListener(staggeredGridLayoutManager = staggeredGridLayoutManager) {
            override fun onLoadMore() {
                viewModel.getImages(projectId, isInitial = false, isRefresh = false)
            }
        }

        galleryRV.addOnScrollListener(scrollListener)
    }

    companion object {
        const val GELLERY = "gallery"
        const val PROJECT_ID = "project_id"
        fun newInstance(items: ArrayList<GalleryItem>, projectId: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(GELLERY, items)
                    putString(PROJECT_ID, projectId)
                }
            }
    }


}
