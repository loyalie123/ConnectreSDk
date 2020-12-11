package com.loyalie.connectre.ui.project.testimonial


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
import com.loyalie.connectre.data.TestimonialItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_testimonials.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class TestimonialsFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso

    private val items = ArrayList<TestimonialItem>()
    lateinit var testimonyAdapter: TestimonialAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: TestimonialVM

    lateinit var projectId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testimonials, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId =
            arguments?.getString(PROJECT_ID) ?: throw IllegalStateException("Project id required")
        val tests = arguments?.getSerializable(TESTIMONIALS) as ArrayList<TestimonialItem>?
        if (tests?.isEmpty() ?: true) defaultTV.setVisible()
        else defaultTV.setGone()
        if (tests == null) return
        viewModel = viewModelProvider(viewModelFactory)
        items.clear()
        items.addAll(tests)
        viewModel.setData(tests)
        initRV()

        viewModel.testimonyHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    items.clear()
                    items.addAll(it.data)
                    testimonyAdapter.showLoading(false)

                    testimonyAdapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    testimonyAdapter.showLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    testimonyAdapter.showLoading(true)
                }
            }
        })
    }

    private fun initRV() {


        val layoutManager = LinearLayoutManager(activity)
        testimonialsRV.layoutManager = layoutManager
        testimonyAdapter = TestimonialAdapter(requireContext(), items, picasso)
        testimonialsRV.adapter = testimonyAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getImages(projectId, isInitial = false, isRefresh = false)
            }
        }

        testimonialsRV.addOnScrollListener(scrollListener)
    }

    companion object {
        const val TESTIMONIALS = "testimonials"
        const val PROJECT_ID = "project_id"
        fun newInstance(items: ArrayList<TestimonialItem>, projectId: String) =
            TestimonialsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TESTIMONIALS, items)
                    putString(PROJECT_ID, projectId)
                }
            }

    }


}
