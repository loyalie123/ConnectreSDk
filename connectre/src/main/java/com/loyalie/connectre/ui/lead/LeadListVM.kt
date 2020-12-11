package com.loyalie.connectre.ui.lead

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.AssociatedProgramItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class LeadListVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {
    private val _programHolder = MutableLiveData<ViewState<List<AssociatedProgramItem>>>()
    val programHolder: LiveData<ViewState<List<AssociatedProgramItem>>>
        get() = _programHolder
    private val programs = ArrayList<AssociatedProgramItem>()
    private var offset = 1
    private var totalCount = 0

    fun getAssociatedprograms(
        vendorId: String,
        isInitial: Boolean = true,
        isRefresh: Boolean = false
    ) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == programs.size) return
        }
        val disposable = repository.getAssociatedPrograms("4", offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _programHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) programs.clear()
                offset++
                programs.addAll(it.associatedPgmList)
                totalCount = it.associatedPgmListCount

                _programHolder.postValue(ViewState.Success(programs))

            }, {
                _programHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }


}