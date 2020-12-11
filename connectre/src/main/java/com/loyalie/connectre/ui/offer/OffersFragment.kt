package com.loyalie.connectre.ui.offer


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
import com.loyalie.connectre.data.ProgramItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.refer.ReferListAdapter
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_offers.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class OffersFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso

    private val programs = ArrayList<ProgramItem>()
    lateinit var programsAdapter: ReferListAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ProjectOfferVM

    lateinit var projectId: String
    lateinit var vendorId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId =
            arguments?.getString(PROJECT_ID) ?: throw IllegalStateException("Project id required")
        vendorId =
            arguments?.getString(VENDOR_ID) ?: throw IllegalStateException("Vendor id required")
        val prgrms = arguments?.getSerializable(PROGRAMS) as ArrayList<ProgramItem>?
        if (prgrms?.isEmpty() ?: true) defaultTV.setVisible()
        else defaultTV.setGone()
        if (prgrms == null) return
        viewModel = viewModelProvider(viewModelFactory)
        programs.clear()
        programs.addAll(prgrms)
        viewModel.setImages(prgrms)
        initRV()

        viewModel.programHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    programs.clear()
                    programs.addAll(it.data)
                    programsAdapter.showLoading(false)

                    programsAdapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    programsAdapter.showLoading(false)
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    programsAdapter.showLoading(true)
                }
            }
        })
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(activity)
        offersRV.layoutManager = layoutManager
        programsAdapter = ReferListAdapter(requireContext(), programs, picasso)
        offersRV.adapter = programsAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getPrograms(projectId, vendorId, isInitial = false, isRefresh = false)
            }
        }

        offersRV.addOnScrollListener(scrollListener)
    }

    companion object {
        const val PROGRAMS = "programs"
        const val PROJECT_ID = "project_id"
        const val VENDOR_ID = "vendor_id"
        fun newInstance(items: ArrayList<ProgramItem>, projectId: String, vendorId: String) =
            OffersFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROGRAMS, items)
                    putString(VENDOR_ID, vendorId)
                    putString(PROJECT_ID, projectId)
                }
            }

    }


}
