package com.loyalie.connectre.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.ApplyEventResponse
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class EventVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {
    private val _statusHolder = MutableLiveData<ViewState<ApplyEventResponse>>()
    val statusHolder: LiveData<ViewState<ApplyEventResponse>>
        get() = _statusHolder

    fun applyEvent(eventId: String) {

        val disposible = repository.applyEvent(eventId)
            .doOnSubscribe { _statusHolder.postValue(ViewState.Loading()) }
            .observeOn(schedulers.main)
            .subscribe({
                _statusHolder.postValue(ViewState.Success(it))
            }, {
                _statusHolder.postValue(ViewState.Error(it))
            })
        compositeDisposable.add(disposible)

    }
}