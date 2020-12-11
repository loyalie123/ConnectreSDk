package com.loyalie.connectre.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class FeedbackVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {


    private val _successHolder = MutableLiveData<ViewState<String>>()
    val successHolder: LiveData<ViewState<String>>
        get() = _successHolder


    fun sendFeedback(vendorId: String, comment: String, rating: String) {

        val disposible = repository.addFeedback(vendorId, comment, rating)
            .observeOn(schedulers.main)
            .doOnSubscribe { _successHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _successHolder.value = ViewState.Success(it)
            }, {
                _successHolder.value = ViewState.Error(it)
            })

        compositeDisposable.add(disposible)

    }

}