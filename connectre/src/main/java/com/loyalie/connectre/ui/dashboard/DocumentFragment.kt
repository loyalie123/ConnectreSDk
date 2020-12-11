package com.loyalie.connectre.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.data.DocumentItem
import com.loyalie.connectre.ui.project.documentation.DocumentVM
import com.loyalie.connectre.util.PaginatingScrollListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_documentation.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DocumentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocumentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_document, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DocumentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DocumentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @Inject
    lateinit var picasso: Picasso


    private val documents = ArrayList<DocumentItem>()
    lateinit var docAdapter: DocumentsAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: DocumentVM

    lateinit var projectId: String


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel = viewModelProvider(viewModelFactory)
        documents.clear()
        documents.add(DocumentItem("New Document1", "Lorem ipsum is a dummy…"))
        documents.add(DocumentItem("New Document2", "Lorem ipsum is a dummy…"))
        documents.add(DocumentItem("New Document3", "Lorem ipsum is a dummy…"))
        documents.add(DocumentItem("New Document4", "Lorem ipsum is a dummy…"))
        documents.add(DocumentItem("New Document5", "Lorem ipsum is a dummy…"))
        initRV()


    }

    private fun initRV() {

        val layoutManager = LinearLayoutManager(activity)
        documentationRV.layoutManager = layoutManager
        docAdapter = DocumentsAdapter(requireContext(), documents)
        documentationRV.adapter = docAdapter
        /*  scrollListener = object : PaginatingScrollListener(layoutManager) {
              override fun onLoadMore() {
                  viewModel.getData(projectId, isInitial = false, isRefresh = false)
              }
          }
  */
//        documentationRV.addOnScrollListener(scrollListener)
    }


}