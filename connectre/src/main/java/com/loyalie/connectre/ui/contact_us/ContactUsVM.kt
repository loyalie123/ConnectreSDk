package com.loyalie.connectre.ui.contact_us

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.ContactUsResp
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class ContactUsVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {
    private val _contactUsHolder = MutableLiveData<ViewState<ContactUsResp>>()
    val contactUsHolder: LiveData<ViewState<ContactUsResp>> = _contactUsHolder

    fun getContactUs() {
        compositeDisposable.add(repository.getContactUs()
            .doOnSubscribe {
                _contactUsHolder.postValue(ViewState.Loading())
            }
            .observeOn(schedulers.main)
            .subscribe({
                _contactUsHolder.postValue(ViewState.Success(it))
            },
                {
                    _contactUsHolder.postValue(ViewState.Error(it))
                })
        )
    }
}