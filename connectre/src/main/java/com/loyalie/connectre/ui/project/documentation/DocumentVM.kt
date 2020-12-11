package com.loyalie.connectre.ui.project.documentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.DocumentItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class DocumentVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _docsHolder = MutableLiveData<ViewState<List<DocumentItem>>>()
    val docsHolder: LiveData<ViewState<List<DocumentItem>>>
        get() = _docsHolder
    private val docs = ArrayList<DocumentItem>()
    private var offset = 2
    private var totalCount = 0

    fun getData(projectId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == docs.size) return
        }
        val disposable = repository.getDocuments(projectId, offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _docsHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) docs.clear()
                offset++
                docs.addAll(it.documentationList)
                totalCount = it.documentationListCount

                _docsHolder.postValue(ViewState.Success(docs))

            }, {
                _docsHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun setdata(items: List<DocumentItem>) {
        docs.addAll(items)
    }
}