package com.loyalie.connectre.ui.rewards


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.data.BenefitItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.data.categoryList
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_benefits.*
import javax.inject.Inject


class BenefitsFragment : BaseFragment() {
    @Inject
    lateinit var picasso: Picasso

    private val benefits = ArrayList<BenefitItem>()
    lateinit var benefitsAdapter: RewardsAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: BenefitsVM
    lateinit var vendorId: String
    private val catTotalList = ArrayList<categoryList>()
    private var catList = arrayOf<CharSequence?>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_benefits, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vendorId =
            arguments?.getString(VENDOR_ID) ?: throw IllegalStateException("Vendor ID required")
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getCategory(vendorId)
        viewModel.getBenefits(vendorId, catID)
        initRV()
        observeVM()
        filterIV.setOnClickListener {
            alertDilog()
        }
        benefitsSWL.setOnRefreshListener {
            benefits.clear()
            benefitsAdapter.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getBenefits(vendorId, catID, isInitial = true, isRefresh = true)
        }

    }


    private fun initRV() {
        val layoutManager = LinearLayoutManager(requireContext())
        benefitsRV.layoutManager = layoutManager
        benefitsAdapter = RewardsAdapter(requireContext(), benefits, false, picasso)
        benefitsRV.adapter = benefitsAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getBenefits(vendorId, catID, false, false)
            }
        }
        benefitsRV.addOnScrollListener(scrollListener)
    }

    private fun observeVM() {
        viewModel.benefitHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    benefits.clear()
                    benefits.addAll(it.data)

                    if (benefits.isEmpty()) {
                        errorView.setVisible()
                        filterIV.setGone()
                    } else {
                        errorView.setGone()
                        filterIV.setVisible()

                    }
                    toggleLoading(false)
                    benefitsAdapter.showLoading(false)
                    benefitsSWL.isRefreshing = false
                    benefitsAdapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    toggleLoading(false)
                    benefitsAdapter.showLoading(false)
                    benefitsSWL.isRefreshing = false
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) toggleLoading(true)
                    else if (!it.isReload) benefitsAdapter.showLoading(true)
                }
            }
        })

        viewModel.catHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    catTotalList.clear()
                    catTotalList.add(0, categoryList(0, "All", 0, "", 0))

                    catTotalList.addAll(it.data)
                    if (it.data.isNotEmpty()) {
                        catList = arrayOfNulls<CharSequence>(it.data.size)
                        val listItems: ArrayList<String> =
                            ArrayList()






                        for (i in it.data.indices) {

                            listItems.add(i, it.data[i].benefitCategoryName)
                        }
                        listItems.add(0, "All")
                        catList = listItems.toArray(arrayOfNulls<CharSequence>(listItems.size))
                    }


                }
                is ViewState.Error -> {

                    onApiError(it.exception)
                }
                is ViewState.Loading -> {

                }
            }
        })
    }

    var checkedItemPos = 0
    var catID = 0
    fun alertDilog() {
        var alertDialog1: AlertDialog? = null

        var builder = AlertDialog.Builder(context, R.style.AlertDialogTheme);

        builder.setTitle("Select Category");

        builder.setSingleChoiceItems(
            catList, checkedItemPos
        ) { p0, p1 ->
            alertDialog1!!.dismiss()
            checkedItemPos = p1
            catID = catTotalList[p1].benefitCategoryId
            viewModel.getBenefits(vendorId, catID, true, false)
        }
        alertDialog1 = builder.create();
        alertDialog1.show();

    }


    private fun toggleLoading(load: Boolean) {
        if (load) {
            if (activity is RewardsActivity) (activity as RewardsActivity).load(true)
        } else {
            if (activity is RewardsActivity) (activity as RewardsActivity).stopLoading(true)
        }
    }

    companion object {
        const val VENDOR_ID = "vendor_id"
        fun newInstance(vendorId: String) = BenefitsFragment().apply {
            arguments = Bundle().apply { putString(VENDOR_ID, vendorId) }
        }
    }


}
