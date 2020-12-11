package com.loyalie.connectre.ui.offer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.ProgramItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class ProjectOfferVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _programHolder = MutableLiveData<ViewState<List<ProgramItem>>>()
    val programHolder: LiveData<ViewState<List<ProgramItem>>>
        get() = _programHolder
    private val programs = ArrayList<ProgramItem>()
    private var offset = 2
    private var totalCount = 0

    fun getPrograms(
        projectId: String,
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
        val disposable = repository.getProgramsForProject(projectId, offset, vendorId)
            .observeOn(scheduler.main)
            .doOnSubscribe { _programHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) programs.clear()
                offset++
                programs.addAll(it.ReferralList)
                totalCount = it.ReferralListCount

                _programHolder.postValue(ViewState.Success(programs))

            }, {
                _programHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun setImages(items: List<ProgramItem>) {
        programs.addAll(items)
    }
}