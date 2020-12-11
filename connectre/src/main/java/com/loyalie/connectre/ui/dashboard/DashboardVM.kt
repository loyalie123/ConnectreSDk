package com.loyalie.connectre.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.*
import com.loyalie.connectre.interceptors.API_ERROR
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import com.loyalie.connectre.util.ConnectReApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DashboardVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {

    private val _dashboardHolder = MutableLiveData<ViewState<List<MilestoneList>>>()
    val developerHolder: LiveData<ViewState<List<MilestoneList>>>
        get() = _dashboardHolder

    private val _demandHolder = MutableLiveData<ViewState<List<DemandList>>>()
    val demandHolder: LiveData<ViewState<List<DemandList>>>
        get() = _demandHolder

    private val _receiptHolder = MutableLiveData<ViewState<List<ReceiptList>>>()
    val receiptHolder: LiveData<ViewState<List<ReceiptList>>>
        get() = _receiptHolder

    private val _overviewHolder=MutableLiveData<ViewState<paymentResponse>>()
        val overviewHolder:LiveData<ViewState<paymentResponse>>
    get()=_overviewHolder
    private var offset = 0
    private var offset1 = 0
    private var offset2 = 0
    private var totalCount = 0
    private var totalCount1 = 0
    private var totalCount2 = 0
    private val milestoneList = ArrayList<MilestoneList>()
    private val demandList = ArrayList<DemandList>()
    private val receiptList = ArrayList<ReceiptList>()


    fun List(isInitial: Boolean, isRefresh: Boolean, soId: String, offset: Int,userId:Int) {

       /* if (isInitial) {
            offset = 0
            totalCount = 0
        } else {
            if (totalCount == milestoneList.size) return
        }*/
        _dashboardHolder.value = ViewState.Loading(isInitial, isRefresh)

        repository.getMilestone(soId, offset,userId).enqueue(object :
            Callback<milestoneRes> {
            override fun onFailure(call: Call<milestoneRes>?, t: Throwable?) {
                _dashboardHolder.value = ViewState.Error(t!!)
            }

            override fun onResponse(call: Call<milestoneRes>?, response: Response<milestoneRes>?) {
                if (response?.isSuccessful == true) {
//                    if (isInitial) milestoneList.clear()
//                    offset++
//                    milestoneList.addAll(response.body()?.MilestoneList!!)
//                    totalCount = /*response.body()?.totalRecords ?: 0*/response?.body()?.MilestoneList?.size!!

                    _dashboardHolder.value = ViewState.Success(response?.body()?.MilestoneList!!)


                } else
                    _dashboardHolder.value = ViewState.Error(
                        ApiException(
                            API_ERROR, ConnectReApp.instance.getString(
                                R.string.un_expected_error
                            )
                        )
                    )

            }
        })
    }

    fun DemandsList(isInitial: Boolean, isRefresh: Boolean, soId: String, offset: Int,userId: Int) {

       /* if (isInitial) {
            offset1 = 0
            totalCount1 = 0
        } else {
            if (totalCount1 == demandList.size) return
        }*/
        _demandHolder.value = ViewState.Loading(isInitial, isRefresh)

        repository.getDemands(soId, offset,userId).enqueue(object :
            Callback<demandsRes> {
            override fun onFailure(call: Call<demandsRes>?, t: Throwable?) {
                _demandHolder.value = ViewState.Error(t!!)
            }

            override fun onResponse(call: Call<demandsRes>?, response: Response<demandsRes>?) {
                if (response?.isSuccessful == true) {
//                    if (isInitial) demandList.clear()
//                    offset1++
//                    response.body()?.DemandList?.let { demandList.addAll(it) }
//                    totalCount1 = /*response?.body()?.totalRecords ?: 0*/demandList.size

                    _demandHolder.value = ViewState.Success(response?.body()?.DemandList!!)
                } else
                    _demandHolder.value = ViewState.Error(
                        ApiException(
                            API_ERROR, ConnectReApp.instance.getString(
                                R.string.un_expected_error
                            )
                        )
                    )

            }
        })
    }

    fun ReceiptList(
        isInitial: Boolean ,
        isRefresh: Boolean,
        soId: String,
        offset: Int,userId: Int
    ) {

        _receiptHolder.value = ViewState.Loading(isInitial, isRefresh)

        repository.getReceipts(soId, offset,userId).enqueue(object : Callback<receiptRes> {
            override fun onFailure(call: Call<receiptRes>?, t: Throwable?) {
                _receiptHolder.value = ViewState.Error(t!!)
            }

            override fun onResponse(call: Call<receiptRes>?, response: Response<receiptRes>?) {
                if (response?.isSuccessful == true) {

                    _receiptHolder.value = ViewState.Success(response?.body()?.ReceiptList!!)
                } else
                    _receiptHolder.value = ViewState.Error(
                        ApiException(
                            API_ERROR,
                            ConnectReApp.instance.getString(R.string.un_expected_error)
                        )
                    )

            }
        })
    }
    fun GetOverview(
        soId: Int,
        userId: Int
    ) {


        _overviewHolder.value = ViewState.Loading()

        repository.getOverview(soId,userId).enqueue(object : Callback<paymentResponse> {
            override fun onFailure(call: Call<paymentResponse>?, t: Throwable?) {
                _overviewHolder.value = ViewState.Error(t!!)
            }

            override fun onResponse(call: Call<paymentResponse>?, response: Response<paymentResponse>?) {
                if (response?.isSuccessful == true) {

                    _overviewHolder.value = ViewState.Success(response.body()!!)
                } else
                    _overviewHolder.value = ViewState.Error(
                        ApiException(
                            API_ERROR,
                            ConnectReApp.instance.getString(R.string.un_expected_error)
                        )
                    )

            }
        })
    }
}