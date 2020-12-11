package com.loyalie.connectre.ui.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.RewardItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class RewardsVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _rewardHolder = MutableLiveData<ViewState<List<RewardItem>>>()
    val rewardHolder: LiveData<ViewState<List<RewardItem>>>
        get() = _rewardHolder
    private val rewards = ArrayList<RewardItem>()
    private var offset = 1
    private var totalCount = 0

    fun getRewards(vendorId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == rewards.size) return
        }
        val disposable = repository.getRewards(offset, vendorId)
            .observeOn(scheduler.main)
            .doOnSubscribe { _rewardHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) rewards.clear()
                offset++
                rewards.addAll(it.RewardDetails)
                totalCount = 0

                _rewardHolder.postValue(ViewState.Success(rewards))

            }, {
                _rewardHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }


}