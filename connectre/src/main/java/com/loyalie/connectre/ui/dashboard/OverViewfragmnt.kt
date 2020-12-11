package com.loyalie.connectre.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.BillingDummyList
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.data.paymentOverView
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.fragment_over_viewfragmnt.*
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OverViewfragmnt.newInstance] factory method to
 * create an instance of this fragment.
 */

class OverViewfragmnt : BaseFragment() {
    // TODO: Rename and change types of parameters

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: DashboardVM
    private var param1: Int = 0
    private var param2: Int = 0
    var billingDummyList = ArrayList<BillingDummyList>()
    var paymentDummyList = ArrayList<BillingDummyList>()

    //    var paymentOverviewData :paymentOverView?=null
    var isOpen: Boolean = false
    private val COUNTDOWN_RUNNING_TIME: Long = 500
    private var animationUp: Animation? = null
    var animationDown: Animation? = null
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
        return inflater.inflate(R.layout.fragment_over_viewfragmnt, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.GetOverview(param1, param2)
        observeVM()
        arc.setMaxProgress(100)
        arc.setProgressColor(Color.parseColor("#0088cf"))
        setAdapter1()
        setAdapter2()
        listners()
    }

    private fun listners() {
        constDownload.setOnClickListener {
            "Document Downloaded Successfully".toast(context!!)
        }
        greivncTv.setOnClickListener {
            GreifsActivity.start(context!!)
            activity?.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
        }

        showMoreTv.setOnClickListener {
            if (!isOpen) {
                rvPayment.startAnimation(animationUp)
                paymentDetailsTv.startAnimation(animationUp)
                val countDownTimerStatic: CountDownTimer = object : CountDownTimer(
                    COUNTDOWN_RUNNING_TIME,
                    16
                ) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        rvPayment.setGone()
                        paymentDetailsTv.setGone()
                    }
                }
                countDownTimerStatic.start()
                showMoreTv.text = "Show More"


                showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_point_to_right,
                    0
                )

                adapter1 = BillingAdapter(context!!, billingDummyList)
                rvBilling.setAdapter(adapter1)
            } else {
                rvPayment.setVisible()
                paymentDetailsTv.setVisible()
                rvPayment.startAnimation(animationDown);
                paymentDetailsTv.startAnimation(animationDown)
                showMoreTv.text = "Show Less"


                showMoreTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_point_to_up,
                    0
                )

                adapter1 = BillingAdapter(context!!, billingDummyList)
                rvBilling.setAdapter(adapter1)
//notifyItemChanged(position)
            }


        }
    }

    private val loadingDialog by lazy {
        LoadingDialog(context!!)
    }

    private fun observeVM() {
        viewModel.overviewHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.dismiss()


                    setArrayList1(it.data.paymentOverView)
                    setArrayList2(it.data.paymentOverView)
//                    adapter1.showLoading(false)
                    titleUpcoming.text = it.data.paymentOverView.allotmentDays
                    arctv3.text =
                        "As on " + it.data.paymentOverView.enteredDate.convertToddthMMyyy()
                    valueUpcoming.text = "Rs " + it.data.paymentOverView.upcomingAmount
                    paymdueValueTv.text = it.data.paymentOverView.dueDate.convertToddMMyyy()
                    arctv1.text =
                        ""/*+String.format("%.2f",*/ + it.data.paymentOverView.amountPaid + "%"
                    arc.setProgress(it.data.paymentOverView.amountPaid.toFloat())
                    adapter1?.notifyDataSetChanged()
                    adapter2?.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    loadingDialog.dismiss()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.show()
                }
            }
        })
    }

    var adapter1: BillingAdapter? = null
    var adapter2: BillingAdapter? = null
    fun setAdapter1() {
        rvPayment.setNestedScrollingEnabled(false)
        rvBilling.setNestedScrollingEnabled(false)

        val lm = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
//        lm.setScrollEnabled(false)
        rvBilling.setLayoutManager(lm)
        adapter1 = BillingAdapter(context!!, billingDummyList)
        rvBilling.setAdapter(adapter1)
        rvBilling.stopScroll()
    }

    fun setAdapter2() {
//        setArrayList2(paymentOverviewData[0])
        val lm = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
//        lm.setScrollEnabled(false)
        rvPayment.setLayoutManager(lm)
        adapter2 = BillingAdapter(context!!, paymentDummyList)
        rvPayment.setAdapter(adapter2)
        rvPayment.stopScroll()
    }

    fun setArrayList1(paymentOverView: paymentOverView) {
        billingDummyList.clear()
        billingDummyList.add(
            BillingDummyList(
                "Agreement Value",
                paymentOverView.agreementValue,
                0,
                "#000000",
                11,
                0.0,
                0.0,
                0.0,
                0.0, 0.0
            )
        )
        billingDummyList.add(
            BillingDummyList(
                "Total Applicable Taxes",
                paymentOverView.totalApplicableTaxes,
                0,
                "#000000",
                22,
                0.0,
                0.0,
                0.0,
                0.0, 0.0
            )
        )
        billingDummyList.add(
            BillingDummyList(
                "Total Billed Value",
                paymentOverView.totalBilledValue,
                1,
                "#000000",
                33,
                paymentOverView.towardsBilledPrincipal,
                paymentOverView.towardsBilledTds,
                paymentOverView.towardsBilledTaxes,
                paymentOverView.towardsBilledInterest, 0.0
            )
        )
         billingDummyList.add(
             BillingDummyList(
                 "Total Unbilled Value", paymentOverView.totalUnBilledValue,
                 0,
                 "#000000",
                 44, paymentOverView.towardsUnBilledPrincipal,
                 paymentOverView.towardsUnBilledTds,
                 paymentOverView.towardsUnBilledTaxes,
                 paymentOverView.towardsUnBilledInterest,0.0
             )
         )
    }

    fun setArrayList2(paymentOverView: paymentOverView) {
        paymentDummyList.clear()
        paymentDummyList.add(
            BillingDummyList(
                "Total Paid",
                paymentOverView.totalPaid,
                1,
                "#0088cf",
                55,
                paymentOverView.towardsPaymentPrincipal,
                paymentOverView.towardsPaymentTds,
                paymentOverView.towardsPaymentTaxes,
                paymentOverView.towardsPaymentInterest, paymentOverView.towardsPaymentOtherCharges
            )
        )

        paymentDummyList.add(
            BillingDummyList(
                "Total Outstanding",
                paymentOverView.towardsOutstandingAmount,
                1,
                "#ea2c00",
                66,
                paymentOverView.towardsOutstandingPrincipal,
                paymentOverView.towardsOutstandingTds,
                paymentOverView.towardsOutstandingTaxes,
                paymentOverView.towardsOutstandingInterest, 0.0
            )
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OverViewfragmnt.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            OverViewfragmnt().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}