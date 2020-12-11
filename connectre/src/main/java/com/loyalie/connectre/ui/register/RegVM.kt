package com.loyalie.connectre.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.ProjectItem
import com.loyalie.connectre.data.RegResponse
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.data.domainList
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class RegVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {

    private val _userHolder = MutableLiveData<ViewState<Pair<Boolean, RegResponse>>>()
    val userHolder: LiveData<ViewState<Pair<Boolean, RegResponse>>>
        get() = _userHolder

    private val _projectHolder = MutableLiveData<ViewState<List<ProjectItem>>>()
    val projectHolder: LiveData<ViewState<List<ProjectItem>>> = _projectHolder


    private val _domainHolder = MutableLiveData<ViewState<List<domainList>>>()
    val domainHolder: LiveData<ViewState<List<domainList>>> = _domainHolder

    fun getDomainList() {
        val disposable = repository.getDomainLIst()
            .observeOn(schedulers.main)
            .doOnSubscribe { _domainHolder.postValue(ViewState.Loading(isInitial = false)) }
            .subscribe({

                _domainHolder.postValue(ViewState.Success(it.domainList))

            }, {
                _domainHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)


    }

    fun registerEmp(
        username: String,

        address: String,
        flatNo: String,
        designation: String,
        companyName: String,
        empId: String,
        projectId: String
    ) {
        val disposible = repository.registerEmployee(
            username,

            address,
            flatNo,
            empId,
            projectId,
            designation,
            companyName
        )
            .observeOn(schedulers.main)
            .subscribe({
                _userHolder.value = ViewState.Success(false to it)
            }, {
                _userHolder.value = ViewState.Error(it)
            })

        compositeDisposable.add(disposible)
    }

    fun getProjectList() {

        val disposable = repository.getProjectList()
            .observeOn(schedulers.main)
            .doOnSubscribe { _projectHolder.postValue(ViewState.Loading(isInitial = false)) }
            .subscribe({

                _projectHolder.postValue(ViewState.Success(it.projectList))

            }, {
                _projectHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }
}