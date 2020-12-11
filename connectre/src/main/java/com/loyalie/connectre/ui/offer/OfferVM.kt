package com.loyalie.connectre.ui.offer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.*
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class OfferVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _programHolder =
        MutableLiveData<ViewState<Triple<List<OfferItem>, Pair<String, String>, String>>>()
    val programHolder: LiveData<ViewState<Triple<List<OfferItem>, Pair<String, String>, String>>>
        get() = _programHolder
    private val programs = ArrayList<OfferItem>()
    private var offset = 1
    private var totalCount = 0
    private val _referSuccessHolder =
        MutableLiveData<ViewState<Triple<List<ReferredUserStatusItem>, List<ReferredUserStatusItem>, List<ReferredUserStatusItem>>>>()
    val referSuccessHolder: LiveData<ViewState<Triple<List<ReferredUserStatusItem>, List<ReferredUserStatusItem>, List<ReferredUserStatusItem>>>>
        get() = _referSuccessHolder

    private var referalHolder: ReferalHolder? = null

    fun getOffers(programId: String, isInitial: Boolean = true, isRefresh: Boolean = false) {
        if (isInitial) {
            offset = 1
            totalCount = 0
        } else {
            if (totalCount == programs.size) return
        }
        val disposable = repository.getOffers(programId, offset)
            .observeOn(scheduler.main)
            .doOnSubscribe { _programHolder.postValue(ViewState.Loading(isInitial, isRefresh)) }
            .subscribe({

                if (isInitial) programs.clear()
                offset++
                programs.addAll(it.OfferDetails)
                totalCount = it.OfferDetailsCount

                _programHolder.postValue(
                    ViewState.Success(
                        Triple(
                            programs,
                            (it.PgmTitle ?: "") to (it.PgmDescription ?: ""),
                            it.ShareLink ?: ""
                        )
                    )
                )

            }, {
                _programHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun referContacts(
        name: String,
        mobileNum: String,
        emailStr: String,
        userID: String,
        programID: String
    ) {
        val contacts = java.util.ArrayList<ContactItem>()


        val email: String = if (emailStr.isEmpty()) ""
        else emailStr

        contacts.add(
            ContactItem(
                userID,
                "",
                name ?: "",
                mobileNum.replace("[^0-9]".toRegex(), ""),
                email,
                programID,
                "0"

            )
        )


        referalHolder = ReferalHolder(contacts)
        val disposable = repository.referUsers(referalHolder!!, "")
            .map {
                val newUsers = it.userStatus.filter { it.status == 1 }
                val existingUsers = it.userStatus.filter { it.status == 2 || it.status == 3 }
                val invalidUsers =
                    it.userStatus.filter { it.status != 1 && it.status != 2 && it.status != 3 }
                return@map Triple(newUsers, existingUsers, invalidUsers)
            }
            .observeOn(scheduler.main)
            .doOnSubscribe { _referSuccessHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _referSuccessHolder.postValue(ViewState.Success(it))
            }, {
                _referSuccessHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)
    }
}