package com.loyalie.connectre.ui.project.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.FaqItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class FaqVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _faqsHolder = MutableLiveData<ViewState<List<FaqItem>>>()
    val faqsHolder: LiveData<ViewState<List<FaqItem>>>
        get() = _faqsHolder
    private val faqs = ArrayList<FaqItem>()
    private var offset = 2
    private var totalCount = 0

    fun getFaqs(projectId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == faqs.size) return
        }
        val disposable = repository.getFaqs(projectId, offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _faqsHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) faqs.clear()
                offset++
                faqs.addAll(it.faqList)
                totalCount = it.faqListCount

                _faqsHolder.postValue(ViewState.Success(faqs))

            }, {
                _faqsHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun setFaqs(images: List<FaqItem>) {
        this.faqs.addAll(images)
    }
}