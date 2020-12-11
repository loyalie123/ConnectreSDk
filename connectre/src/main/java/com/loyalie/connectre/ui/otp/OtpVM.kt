package com.loyalie.connectre.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.iid.FirebaseInstanceId
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.*
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import io.reactivex.Single
import javax.inject.Inject


class OtpVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers,
    private val preferenceStorage: SharedPreferenceStorage,
    private val userIndependentPref: UserIndependantPreferenceStore
) : BaseVM() {

    private val _otpVerifyHolder = MutableLiveData<ViewState<UserReponse>>()
    val otpVerifyHolder: LiveData<ViewState<UserReponse>>
        get() = _otpVerifyHolder

    private val _resendHolder = MutableLiveData<ViewState<String>>()
    val resendHolder: LiveData<ViewState<String>>
        get() = _resendHolder
    private val _resendEmailHolder = MutableLiveData<ViewState<Any>>()
    val resendEmailHolder: LiveData<ViewState<Any>>
        get() = _resendEmailHolder

    private val _developerHolder = MutableLiveData<ViewState<List<DeveloperItem>>>()
    val developerHolder: LiveData<ViewState<List<DeveloperItem>>>
        get() = _developerHolder
    private val _emailHolder = MutableLiveData<ViewState<Any>>()
    val emailHolder: LiveData<ViewState<Any>>
        get() = _emailHolder


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

    fun verifyEmail(emailVerifyCode: String) {

        val disposable = repository.verifyEmail(emailVerifyCode)
            .observeOn(schedulers.main)
            .doOnSubscribe { _emailHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _emailHolder.postValue(ViewState.Success(it))
            }, {
                _emailHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)
    }


    fun verifyOtp(otp: String, mobile: String, isLogin: Boolean) {
        if (isLogin) {
            userIndependentPref.fcmToken.let {
                if (it.isBlank()) {
                    FirebaseInstanceId.getInstance().instanceId.continueWith { newToken ->
                        userIndependentPref.fcmToken = newToken.result?.token ?: ""
                        setOtpObservable(repository.verifyOtp(otp, userIndependentPref.fcmToken))
                    }
                } else {
                    setOtpObservable(repository.verifyOtp(otp, it))
                }
            }
        } else setOtpObservable(repository.verifyChangeNumber(otp, mobile))
    }

    private fun setOtpObservable(observable: Single<UserReponse>) {
        compositeDisposable.add(observable.observeOn(schedulers.main)
            .doOnSubscribe { _otpVerifyHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _otpVerifyHolder.postValue(ViewState.Success(it))
                preferenceStorage.userId = it.userDetails.user_id
                preferenceStorage.userName = it.userDetails.user_name
                preferenceStorage.userEmail = it.userDetails.user_email
                preferenceStorage.userAvatar = it.userDetails.user_avatar ?: ""
                preferenceStorage.userPhone = it.userDetails.user_phone
                preferenceStorage.dob = it.userDetails.dob ?: ""
                preferenceStorage.anniversary = it.userDetails.anniversary ?: ""
                preferenceStorage.app_status = it.userDetails.app_status ?: 0
                preferenceStorage.regDone = it.userDetails.regDone ?: 0
                preferenceStorage.verifyEmail = it.userDetails.emailVerify ?: 0
                preferenceStorage.userType = it.userDetails.userType ?: 1
            }, {
                _otpVerifyHolder.postValue(ViewState.Error(it))
            })
        )
    }

    fun resendOtp(mobile: String, isLogin: Boolean,ccode:String,email:String) {

        val disposable = repository.resendOtp(mobile, isLogin,ccode,email)
            .observeOn(schedulers.main)
            .doOnSubscribe { _resendHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _resendHolder.postValue(ViewState.Success(it))
            }, {
                _resendHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun resendEmailOtp() {

        val disposable = repository.resendEmail()
            .observeOn(schedulers.main)
            .doOnSubscribe { _resendEmailHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _resendEmailHolder.postValue(ViewState.Success(it))
            }, {
                _resendEmailHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

}