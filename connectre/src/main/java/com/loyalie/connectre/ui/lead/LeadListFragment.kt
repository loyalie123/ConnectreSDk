package com.loyalie.connectre.ui.lead

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.AssociatedProgramItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_lead_list.*
import javax.inject.Inject


class LeadListFragment : BaseFragment()/*, View.OnClickListener */ {

    @Inject
    lateinit var picasso: Picasso
    lateinit var vendorId: String


    private val loadingDialog by lazy {
        LoadingDialog(context!!)
    }
    private val programs = ArrayList<AssociatedProgramItem>()
    lateinit var leadListAdapter: LeadListAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: LeadListVM

    /*override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_lead_list, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
//        vendorId = intent.getStringExtra(VENDOR_ID)
        vendorId =
            arguments?.getString(VENDOR_ID) ?: throw IllegalStateException("Vendor ID required")
//        initToolbar()
//        viewModel.getAssociatedprograms(vendorId)
        initRV()
        observeVM()
//        initClickListener()

        swipeRL.setOnRefreshListener {
            programs.clear()
            leadListAdapter.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getAssociatedprograms(vendorId, isInitial = true, isRefresh = true)
        }

    }


    /*   private fun initToolbar() {
           val extractedColor = ConnectReApp.themeColor
           leadToolbar.setBackgroundColor(extractedColor)
           window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
           titleTV.text = getString(R.string.lead_status)
       }
   */
    /* private fun initClickListener() {
         backArrowIV.setOnClickListener(context!!)
     }
 */
    private fun initRV() {
        val layoutManager = LinearLayoutManager(context!!)
        leadsRV.layoutManager = layoutManager
        leadListAdapter = LeadListAdapter(context!!, programs, picasso)
        leadsRV.adapter = leadListAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getAssociatedprograms(vendorId, false, false)
            }
        }
        leadsRV.addOnScrollListener(scrollListener)
    }

    private fun observeVM() {
        viewModel.programHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    programs.clear()
                    programs.addAll(it.data)

                    if (programs.isEmpty()) {
                        errorView.setVisible()
                    } else {
                        errorView.setGone()
                    }

                    loadingDialog.remove()
                    leadListAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    leadListAdapter.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    leadListAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) leadListAdapter.showLoading(true)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAssociatedprograms(vendorId, isInitial = true, isRefresh = true)
    }

    /* companion object {
         const val VENDOR_ID = "vendor_id"
         fun start(context: Context, vendorId: String) {
             context.startActivity(Intent(context, LeadListFragment::class.java).apply {
                 putExtra(VENDOR_ID, vendorId)
             })
         }
     }*/
    override fun setUserVisibleHint(isVisibleToUser: Boolean): Unit {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            viewModel.getAssociatedprograms(vendorId, isInitial = true, isRefresh = true)
        }
    }

    companion object {
        const val VENDOR_ID = "vendor_id"
        fun newInstance(vendorId: String) = LeadListFragment().apply {
            arguments = Bundle().apply { putString(VENDOR_ID, vendorId) }
        }
    }

}
