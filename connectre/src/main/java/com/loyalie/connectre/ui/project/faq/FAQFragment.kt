package com.loyalie.connectre.ui.project.faq


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
import com.loyalie.connectre.data.FaqItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.faq.FAQAdapter
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import kotlinx.android.synthetic.main.fragment_faq.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class FAQFragment : BaseFragment() {

    private val faqs = ArrayList<FaqItem>()
    lateinit var faqAdapter: FAQAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: FaqVM

    lateinit var projectId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId =
            arguments?.getString(PROJECT_ID) ?: throw IllegalStateException("Project id required")
        val faqList = arguments?.getSerializable(FAQ) as ArrayList<FaqItem>?
        if (faqList?.isEmpty() ?: true) defaultTV.setVisible()
        else defaultTV.setGone()
        if (faqList == null) return
        viewModel = viewModelProvider(viewModelFactory)
        faqs.clear()
        faqs.addAll(faqList)
        viewModel.setFaqs(faqList)
        initRV()

        viewModel.faqsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    faqs.clear()
                    faqs.addAll(it.data)

                    faqAdapter.showLoading(false)
                    faqAdapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    faqAdapter.showLoading(false)
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    faqAdapter.showLoading(true)
                }
            }
        })
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(activity)
        faqRV.layoutManager = layoutManager
        faqAdapter = FAQAdapter(requireContext(), faqs, true)
        faqRV.adapter = faqAdapter

        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getFaqs(projectId, isInitial = false, isRefresh = false)
            }
        }

        faqRV.addOnScrollListener(scrollListener)
    }

    companion object {
        const val FAQ = "faq"
        const val PROJECT_ID = "project_id"
        fun newInstance(items: ArrayList<FaqItem>, projectId: String) = FAQFragment().apply {
            arguments = Bundle().apply {
                putSerializable(FAQ, items)
                putString(PROJECT_ID, projectId)
            }
        }

    }


}
