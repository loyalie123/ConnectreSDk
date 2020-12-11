package com.loyalie.connectre.ui.developers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.DeveloperItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class SelectDeveloperVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {

    private val _developerHolder = MutableLiveData<ViewState<List<DeveloperItem>>>()
    val developerHolder: LiveData<ViewState<List<DeveloperItem>>>
        get() = _developerHolder


    fun getDevelopers() {

        val disposable = repository.getDevelopers()
            .observeOn(schedulers.main)
            .doOnSubscribe { _developerHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _developerHolder.postValue(ViewState.Success(it.developerList))
            }, {
                _developerHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

}