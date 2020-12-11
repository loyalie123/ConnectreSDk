package com.loyalie.connectre.ui.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.AppTutorialsResp
import com.loyalie.connectre.data.TutorialItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class TutorialsVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {
    private val _notificationHolder = MutableLiveData<ViewState<AppTutorialsResp>>()
    val tutorialsHolder: LiveData<ViewState<AppTutorialsResp>> = _notificationHolder
    private val tutorials = ArrayList<TutorialItem>()
    private var offset = 1
    private var totalCount = 0

    fun getTutorials(isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == tutorials.size) return
        }
        compositeDisposable.add(repository.getAppTutorial(offset)
            .doOnSubscribe {
                _notificationHolder.postValue(ViewState.Loading(isInitial, isRefresh))
            }
            .observeOn(schedulers.main)
            .subscribe({
                if (isInitial) tutorials.clear()
                offset++
                tutorials.addAll(it.AppTutorials)
                totalCount = it.AppTutorialCount
                _notificationHolder.postValue(ViewState.Success(it))
            },
                {
                    _notificationHolder.postValue(ViewState.Error(it))
                })
        )
    }
}