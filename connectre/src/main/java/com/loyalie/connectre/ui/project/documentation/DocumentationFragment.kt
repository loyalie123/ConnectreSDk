package com.loyalie.connectre.ui.project.documentation


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
import com.loyalie.connectre.data.DocumentItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_documentation.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class DocumentationFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso


    private val documents = ArrayList<DocumentItem>()
    lateinit var docAdapter: DocumentationAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: DocumentVM

    lateinit var projectId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_documentation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId =
            arguments?.getString(PROJECT_ID) ?: throw IllegalStateException("Project id required")
        val docs = arguments?.getSerializable(DOCUMENTS) as ArrayList<DocumentItem>?
        if (docs?.isEmpty() ?: true) defaultTV.setVisible()
        else defaultTV.setGone()
        if (docs == null) return
        viewModel = viewModelProvider(viewModelFactory)
        documents.clear()
        documents.addAll(docs)
        viewModel.setdata(docs)
        initRV()

        viewModel.docsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    docs.clear()
                    docs.addAll(it.data)

                    docAdapter.showLoading(false)
                    docAdapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    docAdapter.showLoading(false)
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    docAdapter.showLoading(true)
                }
            }
        })
    }

    private fun initRV() {

        val layoutManager = LinearLayoutManager(activity)
        documentationRV.layoutManager = layoutManager
        docAdapter = DocumentationAdapter(requireContext(), documents)
        documentationRV.adapter = docAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getData(projectId, isInitial = false, isRefresh = false)
            }
        }

        documentationRV.addOnScrollListener(scrollListener)
    }

    companion object {
        const val DOCUMENTS = "docs"
        const val PROJECT_ID = "project_id"
        fun newInstance(items: ArrayList<DocumentItem>, projectId: String) =
            DocumentationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DOCUMENTS, items)
                    putString(PROJECT_ID, projectId)
                }
            }

    }


}
