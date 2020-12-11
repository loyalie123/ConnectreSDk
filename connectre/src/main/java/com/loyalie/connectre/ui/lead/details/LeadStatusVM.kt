package com.loyalie.connectre.ui.lead.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.LeadItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class LeadStatusVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {
    private val _leadsHolder =
        MutableLiveData<ViewState<Triple<List<LeadItem>, Pair<String, String>, String>>>()
    val leadsHolder: LiveData<ViewState<Triple<List<LeadItem>, Pair<String, String>, String>>>
        get() = _leadsHolder
    private val leads = ArrayList<LeadItem>()
    private var offset = 1
    private var totalCount = 0

    fun getLeads(programId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == leads.size) return
        }
        val disposable = repository.getLeadStatuses(programId, offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _leadsHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) leads.clear()
                offset++
                leads.addAll(it.leadStatus)
                totalCount = it.leadStatusCount

                _leadsHolder.postValue(
                    ViewState.Success(
                        Triple(
                            leads,
                            (it.PgmTitle) to (it.PgmDescription),
                            it.PgmImage
                        )
                    )
                )

            }, {
                _leadsHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }


}