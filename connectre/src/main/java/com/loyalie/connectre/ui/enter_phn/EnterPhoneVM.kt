package com.loyalie.connectre.ui.enter_phn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.LoginResponse
import com.loyalie.connectre.data.SharedPreferenceStorage
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class EnterPhoneVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers,
    private val preferenceStorage: SharedPreferenceStorage
) : BaseVM() {

    private val _enterPhnHolder = MutableLiveData<ViewState<LoginResponse>>()
    val enterPhnHolder: LiveData<ViewState<LoginResponse>>
        get() = _enterPhnHolder


    fun login(phn: String, ccode: String, email: String) {

        val disposable = repository.login(phn,ccode,email)
            .doOnSuccess {
                preferenceStorage.userId = it.userId
                preferenceStorage.authToken = it.sec_key
            }
            .observeOn(schedulers.main)
            .doOnSubscribe { _enterPhnHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _enterPhnHolder.postValue(ViewState.Success(it))
            }, {
                _enterPhnHolder.postValue(ViewState.Error(it))
            })
        compositeDisposable.add(disposable)
    }
}