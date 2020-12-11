package com.loyalie.connectre.ui.rewards


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.data.RewardItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.PaginatingScrollListener
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import com.loyalie.connectre.util.viewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_rewards.*
import javax.inject.Inject


class RewardsFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso

    private val rewards = ArrayList<RewardItem>()
    lateinit var rewardsAdapter: ReferalRewardsAdapter
    lateinit var scrollListener: PaginatingScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: RewardsVM
    lateinit var vendorId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rewards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        vendorId =
            arguments?.getString(VENDOR_ID) ?: throw IllegalStateException("Vendor ID required")
        viewModel.getRewards(vendorId)
        initRV()
        observeVM()

        rewardSWL.setOnRefreshListener {
            rewards.clear()
            rewardsAdapter.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getRewards(vendorId, isInitial = true, isRefresh = true)
        }

    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(requireContext())
        rewardRV.layoutManager = layoutManager
        rewardsAdapter = ReferalRewardsAdapter(requireContext(), rewards, true, picasso)
        rewardRV.adapter = rewardsAdapter
        scrollListener = object : PaginatingScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.getRewards(vendorId, false, false)
            }
        }
        rewardRV.addOnScrollListener(scrollListener)
    }

    private fun observeVM() {
        viewModel.rewardHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    rewards.clear()
                    rewards.addAll(it.data)
                    if (rewards.isEmpty()) {
                        errorView.setVisible()
                    } else {
                        errorView.setGone()
                    }
                    rewardsAdapter.showLoading(false)
                    rewardSWL.isRefreshing = false
                    rewardsAdapter.notifyDataSetChanged()
                }
                is ViewState.Error -> {
                    toggleLoading(false)
                    rewardsAdapter.showLoading(false)
                    rewardSWL.isRefreshing = false
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) toggleLoading(true)
                    else if (!it.isReload) rewardsAdapter.showLoading(true)
                }
            }
        })
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
        fun newInstance(vendorId: String) = RewardsFragment().apply {
            arguments = Bundle().apply { putString(VENDOR_ID, vendorId) }
        }
    }


}
