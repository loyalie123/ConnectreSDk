package com.loyalie.connectre.ui.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.FaqItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class FAQVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {
    private val _faqHolder = MutableLiveData<ViewState<List<FaqItem>>>()
    val faqHolder: LiveData<ViewState<List<FaqItem>>> = _faqHolder

    fun getFaq() {
        compositeDisposable.add(repository.getFAQs()
            .doOnSubscribe {
                _faqHolder.postValue(ViewState.Loading())
            }
            .observeOn(schedulers.main)
            .subscribe({
                _faqHolder.postValue(ViewState.Success(it.faqList))
            },
                {
                    _faqHolder.postValue(ViewState.Error(it))
                })
        )
    }
}