package com.loyalie.connectre.ui.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.BenefitItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.data.categoryList
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class BenefitsVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _benefitHolder = MutableLiveData<ViewState<List<BenefitItem>>>()
    val benefitHolder: LiveData<ViewState<List<BenefitItem>>>
        get() = _benefitHolder

    private val _catHolder = MutableLiveData<ViewState<List<categoryList>>>()
    val catHolder: LiveData<ViewState<List<categoryList>>>
        get() = _catHolder

    private val programs = ArrayList<BenefitItem>()
    private var offset = 1
    private var totalCount = 0
    fun getCategory(vendorId: String) {
        val disposable = repository.getCategories(vendorId)
            .observeOn(scheduler.main)

            .subscribe({


                _catHolder.postValue(ViewState.Success(it.categoryList))

            }, {
                _catHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun getBenefits(
        vendorId: String,
        benefitCategoryId: Int,
        isInitial: Boolean = true,
        isRefresh: Boolean = false
    ) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == programs.size) return
        }
        val disposable = repository.getBenefits(offset, vendorId, benefitCategoryId)
            .observeOn(scheduler.main)
            .doOnSubscribe { _benefitHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) programs.clear()
                offset++
                programs.addAll(it.Benefits)
                totalCount = 0

                _benefitHolder.postValue(ViewState.Success(programs))

            }, {
                _benefitHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }


}