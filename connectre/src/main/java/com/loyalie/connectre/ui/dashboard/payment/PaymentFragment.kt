package com.loyalie.connectre.ui.dashboard.payment

import android.graphics.Color
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
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.*
import com.loyalie.connectre.ui.dashboard.DashboardVM
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.fragment_payment.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int = 0
    private var param2: Int = 0
    var key1 = ArrayList<String>()
    var value1 = ArrayList<String>()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DashboardVM
    lateinit var scrollListener: PaginatingScrollListener1
    private val list = ArrayList<MilestoneList>()
    var param: String = "M"
    private val demand = ArrayList<DemandList>()
    private val receipt = ArrayList<ReceiptList>()
    private val aggregatedReceipts = ArrayList<aggregatedReceipts>()
    lateinit var adapter: MilestoneAdapter
    lateinit var adapter1: DemandsAdapter
    lateinit var adapter2: ReceiptAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    /*  override fun onResume() {
          super.onResume()
  //        getApi(param, true, true, isPaginating)
          fetchList(0,param,isPaginating,true,true)
      }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        milestonesTv.setOnClickListener {
            param = "M"
            milestonesTv.setBackgroundResource(R.drawable.black_rounded_corner)
            dropdown.visibility = View.VISIBLE
            dropdown2.visibility = View.GONE
            dropdown3.visibility = View.GONE
            milestonesTv.setTextColor(Color.WHITE)
            demandsTv.let {
                it.setBackgroundResource(R.drawable.grey_rounded_corner)
                it.setTextColor(Color.BLACK)
            }
            receiptsTv.let {
                it.setBackgroundResource(R.drawable.grey_rounded_corner)
                it.setTextColor(Color.BLACK)
            }
            fetchList(0,param,isPaginating,true,true)
            initRV(param)
        }



        swipeRL.setOnRefreshListener {

            when (param) {

                "M" -> {
                    list.clear()
                    adapter.notifyDataSetChanged()
                }
                "D" -> {
                    demand.clear()
                    adapter1.notifyDataSetChanged()
                }
                "R" -> {
                    receipt.clear()
//                    aggregatedReceipts.clear()
                    adapter2.notifyDataSetChanged()
                }
            }
            swipeRL.isRefreshing = true
            scrollListener?.reset(0, false)
            fetchList(0, param, isPaginating, true, true)
//            getApi(param, true, true,isPaginating)
        }
        demandsTv.setOnClickListener {
            param = "D"
            milestonesTv.setBackgroundResource(R.drawable.grey_rounded_corner)
            dropdown.visibility = View.GONE
            dropdown2.visibility = View.VISIBLE
            dropdown3.visibility = View.GONE
            milestonesTv.setTextColor(Color.BLACK)
            demandsTv.let {
                it.setBackgroundResource(R.drawable.black_rounded_corner)
                it.setTextColor(Color.WHITE)
            }
            receiptsTv.let {
                it.setBackgroundResource(R.drawable.grey_rounded_corner)
                it.setTextColor(Color.BLACK)
            }


//            list.add(MileStoneList("Within 55 days of allotment","Rs 1,56,250.0","HT0034563","","D",10))
//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","HT1234563","","D",20))
//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","HT1234563","","D",30))
//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","HT1234564","","D",40))
            initRV(param)
            fetchList(0, param, isPaginating, true, true)
        }
        receiptsTv.setOnClickListener {
            param = "R"
            milestonesTv.setBackgroundResource(R.drawable.grey_rounded_corner)
            milestonesTv.setTextColor(Color.BLACK)
            dropdown.visibility = View.GONE
            dropdown2.visibility = View.GONE
            dropdown3.visibility = View.VISIBLE
            demandsTv.let {
                it.setBackgroundResource(R.drawable.grey_rounded_corner)
                it.setTextColor(Color.BLACK)
            }
            receiptsTv.let {
                it.setBackgroundResource(R.drawable.black_rounded_corner)
                it.setTextColor(Color.WHITE)
            }

//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","YTRP312524","","R",100))
//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","YTRP312530","","R",200))
//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","HT1234563","","R",300))
//            list.add(milestoneList("Within 55 days of allotment","Rs 1,56,250.0","HT1234564","","R",400))
            initRV(param)
            fetchList(0, param, isPaginating, true, true)
        }
        initRV(param)
        fetchList(0, param, isPaginating, true, true)

        observeVM()
    }

    private var isPaginating = false
    private var isLoad = false
    private val loadingDialog by lazy {
        LoadingDialog(context!!)
    }

    private fun observeVM() {
        viewModel.developerHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    if (swipeRL.isRefreshing) {
                        swipeRL.isRefreshing = false
                        list.clear()
                        isLoad = false
                        list.addAll(it.data ?: emptyList())

                    } else {
                        loadingDialog?.dismiss()
                        if (list.isEmpty()) {
                            list.clear()
                            isLoad = false
                            list.addAll(it.data ?: emptyList())

                        } else {
//                            if(isPaginating){
//                                isLoad=true


                            list.addAll(it.data ?: emptyList())


//                                isPaginating=false
//                            }

                        }
                    }
//                    list.clear()
//                    list.addAll(it.data)
                    if (list.isEmpty()) {
                        errorTV.setVisible()
                    } else {
                        errorTV.setGone()
                    }
                    loadingDialog.remove()
                    adapter.showLoading(false)
//                    swipeRL.isRefreshing = false
                    adapter.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    if (swipeRL.isRefreshing) swipeRL.isRefreshing = false
                    adapter.showLoading(false)
//                    swipeRL.isRefreshing = false
                    scrollListener?.resetLoading()


                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) adapter.showLoading(true)
                }
            }
        })
        viewModel.demandHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    if (swipeRL.isRefreshing) {
                        swipeRL.isRefreshing = false
                        demand.clear()
                        isLoad = false
                        demand.addAll(it.data ?: emptyList())
                    } else {
                        loadingDialog?.dismiss()
                        if (demand.isEmpty()) {
                            demand.clear()
                            isLoad = false
                            demand.addAll(it.data ?: emptyList())
                        } else {
//                            if(isPaginating){
//                                isLoad=true
                            demand.addAll(it.data ?: emptyList())
//                                isPaginating=false
//                            }
                        }
                    }
//                    list.clear()
//                    list.addAll(it.data)
                    if (demand.isEmpty()) {
                        errorTV.setVisible()
                    } else {
                        errorTV.setGone()
                    }
                    loadingDialog.remove()
                    adapter1.showLoading(false)
//                    swipeRL.isRefreshing = false
                    adapter1.notifyDataSetChanged()
                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    adapter1.showLoading(false)
                    swipeRL.isRefreshing = false
                    scrollListener.resetLoading()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) adapter1.showLoading(true)
                }
            }
        })
        viewModel.receiptHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    if (swipeRL.isRefreshing) {
                        swipeRL.isRefreshing = false
                        receipt.clear()
                        isLoad = false
                        receipt.addAll(it.data ?: emptyList())
                    } else {
                        loadingDialog?.dismiss()
                        if (receipt.isEmpty()) {
                            receipt.clear()
                            isLoad = false
                            receipt.addAll(it.data ?: emptyList())
                        } else {
                            receipt.addAll(it.data ?: emptyList())
                        }
                    }
                   if (receipt.isEmpty()) {
                        errorTV.setVisible()
                    } else {
                        errorTV.setGone()
                    }
                    loadingDialog.remove()
                    adapter2.showLoading(false)
                    adapter2.notifyDataSetChanged()
                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    adapter2.showLoading(false)
                    swipeRL.isRefreshing = false
                    scrollListener.resetLoading()
                    onApiError(it.exception)

                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) adapter2.showLoading(true)
                }
            }
        })
    }

/*
    override fun setUserVisibleHint(isVisibleToUser: Boolean): Unit {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
//            getApi(param, true, true, isPaginating)

        fetchList(0,param,isPaginating,true,true)
        }
    }
*/

    fun fetchList(
        offset: Int,
        p: String,
        paginating: Boolean,
        isInitial: Boolean,
        isRefresh: Boolean
    ) {
        isPaginating = paginating
        getApi(p, isInitial, isRefresh, offset)
    }

    fun getApi(p: String, isInitial: Boolean, isRefresh: Boolean, offset: Int) {
        when (p) {
            "M" -> {
                viewModel.List(isInitial, isRefresh, param2.toString(), offset,param1)
            }
            "D" -> viewModel.DemandsList(isInitial, isRefresh, param2.toString(), offset,param1)
            "R" -> viewModel.ReceiptList(isInitial, isRefresh, param2.toString(), offset,param1)
        }

    }

    private fun initRV(param: String) {
        val layoutManager = LinearLayoutManager(activity)
        paymentRcv.layoutManager = layoutManager
        when (param) {
            "M" -> {
                adapter = MilestoneAdapter(requireContext(), list)
//                paymentRcv.setHasFixedSize(true);
                paymentRcv.adapter = adapter

            }
            "D" -> {
                adapter1 = DemandsAdapter(requireContext(), demand)
//                paymentRcv.setHasFixedSize(true);
                paymentRcv.adapter = adapter1

            }
            "R" -> {
                adapter2 = ReceiptAdapter(requireContext(), receipt)
//                paymentRcv.setHasFixedSize(true);
                paymentRcv.adapter = adapter2
            }
        }

        scrollListener =
            object : PaginatingScrollListener1(layoutManager, PAGINATOR_ITEMS_PER_PAGE) {
                override fun onLoadMore(offset: Int) {
                    fetchList(offset, param, true, false, false)
                }
                /*  override fun onLoadMore() {
                      getApi(param, false, false, isPaginating)
      //                viewModel.List(isInitial = false, isRefresh = false, soId = "0")
                  }*/
            }
        paymentRcv.addOnScrollListener(scrollListener)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            PaymentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}