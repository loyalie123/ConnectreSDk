package com.loyalie.connectre.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.UserReponse
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {

    private val _userHolder = MutableLiveData<ViewState<Pair<Boolean, UserReponse>>>()
    val userHolder: LiveData<ViewState<Pair<Boolean, UserReponse>>>
        get() = _userHolder


    fun uploadImage(file: File) {

        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val disposible = repository.uploadImage(body)
            .observeOn(schedulers.main)
            .doOnSubscribe { _userHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _userHolder.value = ViewState.Success(true to it)
            }, {
                _userHolder.value = ViewState.Error(it)
            })

        compositeDisposable.add(disposible)

    }

    fun getUserData() {
        val disposible = repository.getUserData()
            .observeOn(schedulers.main)
            .subscribe({
                _userHolder.value = ViewState.Success(false to it)
            }, {
                _userHolder.value = ViewState.Error(it)
            })

        compositeDisposable.add(disposible)
    }

    fun updateUser(name: String, email: String, dob: String, anniversary: String) {
        val disposible = repository.updateUser(name, email, dob, anniversary)
            .observeOn(schedulers.main)
            .doOnSubscribe { _userHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _userHolder.value = ViewState.Success(true to it)
            }, {
                _userHolder.value = ViewState.Error(it)
            })

        compositeDisposable.add(disposible)
    }


}