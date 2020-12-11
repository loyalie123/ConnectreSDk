package com.loyalie.connectre.ui.refer_contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.ContactItem
import com.loyalie.connectre.data.ReferalHolder
import com.loyalie.connectre.data.ReferredUserStatusItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import ir.mirrajabi.rxcontacts.Contact
import java.util.*
import javax.inject.Inject

class ReferContactVM @Inject constructor(
    private val repository: RemoteRepository,
    private val scheduler: AppRxSchedulers
) : BaseVM() {

    private val _checkUserExistHolder =
        MutableLiveData<ViewState<Triple<List<ReferredUserStatusItem>, List<ReferredUserStatusItem>, List<ReferredUserStatusItem>>>>()
    val checkUserExistHolder: LiveData<ViewState<Triple<List<ReferredUserStatusItem>, List<ReferredUserStatusItem>, List<ReferredUserStatusItem>>>>
        get() = _checkUserExistHolder

    private val _referSuccessHolder =
        MutableLiveData<ViewState<Triple<List<ReferredUserStatusItem>, List<ReferredUserStatusItem>, List<ReferredUserStatusItem>>>>()
    val referSuccessHolder: LiveData<ViewState<Triple<List<ReferredUserStatusItem>, List<ReferredUserStatusItem>, List<ReferredUserStatusItem>>>>
        get() = _referSuccessHolder

    private var referalHolder: ReferalHolder? = null


    fun checkIfAnyUsersAreNew(
        numberContactMap: HashMap<Contact, String>,
        userId: String,
        programID: String
    ) {

        val contacts = ArrayList<ContactItem>()
        for ((rxContact, number) in numberContactMap) {

            val email: String = if (rxContact.emails.isEmpty()) ""
            else rxContact.emails.iterator().next()

            contacts.add(
                ContactItem(
                    userId,
                    "",
                    rxContact.displayName ?: "",
                    number.replace("[^0-9]".toRegex(), ""),
                    email,
                    programID,
                    "0"

                )
            )
        }

        referalHolder = ReferalHolder(contacts)
        val disposable = repository.checkIfNewUserExists(referalHolder!!)
            .map {
                val newUsers = it.userStatus.filter { it.status == 1 }
                val existingUsers = it.userStatus.filter { it.status == 2 || it.status == 3 }
                val invalidUsers =
                    it.userStatus.filter { it.status != 1 && it.status != 2 && it.status != 3 }
                return@map Triple(newUsers, existingUsers, invalidUsers)
            }
            .observeOn(scheduler.main)
            .doOnSubscribe { _checkUserExistHolder.postValue(ViewState.Loading()) }
            .subscribe({
                _checkUserExistHolder.postValue(ViewState.Success(it))
            }, {
                _checkUserExistHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun referContacts(comment: String) {
        val disposable = repository.referUsers(referalHolder!!, comment)
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