package com.loyalie.connectre.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.*
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import javax.inject.Inject

class HomeVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {

    private val _bannerHolder = MutableLiveData<ViewState<List<BannerImageItem>>>()
    val bannerHolder: LiveData<ViewState<List<BannerImageItem>>>
        get() = _bannerHolder

    private val _footerHolder = MutableLiveData<ViewState<List<BannerImageItem>>>()
    val footerHolder: LiveData<ViewState<List<BannerImageItem>>>
        get() = _footerHolder

    private val _projectsHolder = MutableLiveData<ViewState<List<projectCategorys>>>()
    val projectsHolder: LiveData<ViewState<List<projectCategorys>>>
        get() = _projectsHolder

    private val _projectHolder = MutableLiveData<ViewState<List<ProjectItem>>>()
    val projectHolder: LiveData<ViewState<List<ProjectItem>>>
        get() = _projectHolder

    private val _eventsHolder = MutableLiveData<ViewState<List<EventItem>>>()
    val eventsHolder: LiveData<ViewState<List<EventItem>>>
        get() = _eventsHolder

    private val _notificationCountHolder = MutableLiveData<ViewState<Int>>()
    val notificationCountHolder: LiveData<ViewState<Int>>
        get() = _notificationCountHolder

    private val _versionHolder = MutableLiveData<ViewState<VersionReponse>>()
    val versionHolder: LiveData<ViewState<VersionReponse>>
        get() = _versionHolder

    private val _requestCall = MutableLiveData<ViewState<Any>>()
    val requestCallback: LiveData<ViewState<Any>>
        get() = _requestCall


    private val _developerHolder = MutableLiveData<ViewState<List<DeveloperItem>>>()
    val developerHolder: LiveData<ViewState<List<DeveloperItem>>>
        get() = _developerHolder
  private val _propertiesHolder = MutableLiveData<ViewState<List<propertiesList>>>()
    val propertiesHolder: LiveData<ViewState<List<propertiesList>>>
        get() = _propertiesHolder


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

        private var currentPropertyPage = 2
    private var currentEventPage = 2
    var oldItems: IntArray? = null
    private val projectsList = ArrayList<projectCategorys>()
    private val projectList = ArrayList<ProjectItem>()
    private val eventsList = ArrayList<EventItem>()
    private val footerList = ArrayList<BannerImageItem>()
    private val propertiesList = ArrayList<propertiesList>()
    private var dynamicList = ArrayList<ArrayList<ProjectItem>>()
//    val dynamicList = HashMap<Any, Any>()

    fun getDevelopersHome(vendorId: String) {

        val disposable = repository.getDeveloperHomeData(vendorId)
            .observeOn(schedulers.main)
            .doOnSubscribe { _bannerHolder.postValue(ViewState.Loading()) }
            .subscribe({

                eventsList.clear()
                footerList.clear()
projectList.clear()
                projectsList.clear()
                propertiesList.clear()
                _bannerHolder.postValue(ViewState.Success(it.bannerImageList))

                footerList.addAll(it.footerImageList)
                _footerHolder.postValue(ViewState.Success(footerList))


                oldItems = IntArray(it.projectCategorys.size)
                for (i in oldItems!!.indices) {
                    oldItems!![i] = 2

                    /*for(j in it.projectCategorys.get(i).projects.indices)
                        projectList.add(j,it.projectCategorys.get(i).projects)*/



//                    dynamicList[i] = ArrayList<ProjectItem>()

                }

                projectsList.addAll(it.projectCategorys)
                _projectsHolder.postValue(ViewState.Success(projectsList))
                eventsList.addAll(it.eventList)
                _eventsHolder.postValue(ViewState.Success(eventsList))
                propertiesList.addAll(it.propertiesList)
                _propertiesHolder.postValue(ViewState.Success(propertiesList))
            }, {
                _bannerHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun getProjects(vendorId: String, proCatId: Int, position: Int) {

        val disposable = repository.getProjects(vendorId, oldItems!![position], proCatId)
            .observeOn(schedulers.main)
            .doOnSubscribe { _projectHolder.postValue(ViewState.Loading(isInitial = false)) }
            .subscribe({

                projectList.addAll(it.projectList)
//                dynamicList[position].addAll(projectList)
                _projectHolder.postValue(ViewState.Success(projectList))
                oldItems!![position]++


            }, {
                _projectHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun getEvents(vendorId: String) {

        val disposable = repository.getEvents(vendorId, currentEventPage)
            .observeOn(schedulers.main)
            .doOnSubscribe { _eventsHolder.postValue(ViewState.Loading(isInitial = false)) }
            .subscribe({
                eventsList.addAll(it.eventList)
                _eventsHolder.postValue(ViewState.Success(eventsList))
                currentEventPage++
            }, {
                _eventsHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }
    fun getProperties(vendorId: String) {

        val disposable = repository.getProperties(vendorId, currentPropertyPage)
            .observeOn(schedulers.main)
            .doOnSubscribe { _propertiesHolder.postValue(ViewState.Loading(isInitial = false)) }
            .subscribe({
             propertiesList.addAll(it.propertiesList)
                _propertiesHolder.postValue(ViewState.Success(propertiesList))
                currentPropertyPage++
            }, {
                _propertiesHolder.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)

    }

    fun getUnreadNotificationCount() {
        val disposable = repository.getUnreadCount()
            .observeOn(schedulers.main)
            .subscribe({
                _notificationCountHolder.postValue(ViewState.Success(it.notificationCount))
            }, {
            })

        compositeDisposable.add(disposable)
    }

    fun requestCallBack(vendorPhone: String) {
        val disposable = repository.requestCallback(vendorPhone)
            .observeOn(schedulers.main)
            .doOnSubscribe { _requestCall.postValue(ViewState.Loading()) }
            .subscribe({
                _requestCall.postValue(ViewState.Success(it))
            }, {
                _requestCall.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)
    }

    fun getCurrentVersion() {
        val disposable = repository.getVersions()
            .observeOn(schedulers.main)
            .subscribe({
                _versionHolder.postValue(ViewState.Success(it))
            }, {
            })

        compositeDisposable.add(disposable)
    }
}