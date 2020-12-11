package com.loyalie.connectre.ui.privacy_policy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.PrivacyPolicy
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class PrivacyVM @Inject constructor(
    val repository: RemoteRepository,
    val schedulers: AppRxSchedulers
) : BaseVM() {
    private val _privacyHolder = MutableLiveData<ViewState<PrivacyPolicy>>()
    val privacyHolder: LiveData<ViewState<PrivacyPolicy>> = _privacyHolder
    fun getPrivacyPolicy() {
        compositeDisposable.add(repository.getPrivacyPolicy()
            .doOnSubscribe {
                _privacyHolder.postValue(ViewState.Loading())
            }
            .subscribeOn(schedulers.main)
            .subscribe({
                _privacyHolder.postValue(ViewState.Success(it))
            }, {
                _privacyHolder.postValue(ViewState.Error(it))
            })
        )
    }
}