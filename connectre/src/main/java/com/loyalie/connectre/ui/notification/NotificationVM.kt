package com.loyalie.connectre.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.NotificationItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class NotificationVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {
    private val _notificationHolder = MutableLiveData<ViewState<List<NotificationItem>>>()
    val notificationHolder: LiveData<ViewState<List<NotificationItem>>> = _notificationHolder
    private val _readHolder = MutableLiveData<ViewState<Unit>>()
    val readHolder: LiveData<ViewState<Unit>> = _readHolder


    private val notifications = ArrayList<NotificationItem>()
    private var offset = 1
    private var totalCount = 0

    fun getNotification(isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == notifications.size) return
        }


        compositeDisposable.add(repository.getNotificationList(offset)
            .doOnSubscribe {
                _notificationHolder.postValue(ViewState.Loading(isInitial, isRefresh))
            }
            .observeOn(schedulers.main)
            .subscribe({
                if (isInitial) notifications.clear()
                offset++
                notifications.addAll(it.notificationList)
                totalCount = it.notificationListCount
                _notificationHolder.postValue(ViewState.Success(notifications))
            },
                {
                    _notificationHolder.postValue(ViewState.Error(it))
                })
        )
    }

    fun changeStatus(push_id: String, position: Int) {
        val disposable = repository.getNotificationStatus(push_id)
            .observeOn(schedulers.main)
            .subscribe({
                _readHolder.postValue(ViewState.Success(it,position))
            }, {
                _readHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)
    }
}