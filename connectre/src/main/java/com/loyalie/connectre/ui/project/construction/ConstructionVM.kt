package com.loyalie.connectre.ui.project.construction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class ConstructionVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _imagesHolder = MutableLiveData<ViewState<List<GalleryItem>>>()
    val imagesHolder: LiveData<ViewState<List<GalleryItem>>>
        get() = _imagesHolder
    private val images = ArrayList<GalleryItem>()
    private var offset = 2
    private var totalCount = 0

    fun getImages(projectId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == images.size) return
        }
        val disposable = repository.getConstruction(projectId, offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _imagesHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({
                if (isInitial) images.clear()
                offset++
                images.addAll(it.ConstructionProgress)
                totalCount = it.ConstructionCount

                _imagesHolder.postValue(ViewState.Success(images))

            }, {
                _imagesHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun setImages(images: List<GalleryItem>) {
        this.images.addAll(images)
    }
}