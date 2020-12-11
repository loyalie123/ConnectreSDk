package com.loyalie.connectre.ui.project.testimonial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.TestimonialItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class TestimonialVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _testimonyHolder = MutableLiveData<ViewState<List<TestimonialItem>>>()
    val testimonyHolder: LiveData<ViewState<List<TestimonialItem>>>
        get() = _testimonyHolder
    private val testimonies = ArrayList<TestimonialItem>()
    private var offset = 2
    private var totalCount = 0

    fun getImages(projectId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == testimonies.size) return
        }
        val disposable = repository.getTestimonials(projectId, offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _testimonyHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) testimonies.clear()
                offset++
                testimonies.addAll(it.testmonialsList)
                totalCount = it.testmonialsListCount

                _testimonyHolder.postValue(ViewState.Success(testimonies))

            }, {
                _testimonyHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun setData(items: List<TestimonialItem>) {
        testimonies.addAll(items)
    }
}