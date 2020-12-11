package com.loyalie.connectre.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyalie.connectre.base.BaseVM
import com.loyalie.connectre.data.*
import com.loyalie.connectre.network.RemoteRepository
import com.loyalie.connectre.util.AppRxSchedulers
import com.loyalie.connectre.util.convertToMMMDDYYY
import javax.inject.Inject

class ProjectVM @Inject constructor(
    private val repository: RemoteRepository,
    private val schedulers: AppRxSchedulers
) : BaseVM() {

    private val _projectUIModel = MutableLiveData<ViewState<ProjectUiModel>>()
    val projectUIModel: LiveData<ViewState<ProjectUiModel>>
        get() = _projectUIModel

    fun getProjectData(projectId: String, vendorId: String) {
        val disposable = repository.getProjectDetail(projectId, vendorId)
            .doOnSubscribe { _projectUIModel.postValue(ViewState.Loading()) }
            .map { transformProjectDetails(it) }
            .observeOn(schedulers.main)
            .subscribe({
                _projectUIModel.postValue(ViewState.Success(it))
            }, {
                _projectUIModel.postValue(ViewState.Error(it))
            })

        compositeDisposable.add(disposable)
    }


    private fun transformProjectDetails(project: ProjectDetailItem): ProjectUiModel {
        val bhk = project.project?.apartment?.split(",")?.toList()?.filter { it.isNotBlank() }
        val bhkTxt = StringBuilder()
        bhk?.forEachIndexed { index: Int, bh ->
            if (index == 0) bhkTxt.append(bh)
            else if (index == bhk.count() - 1) {
                bhkTxt.append("&")
                bhkTxt.append(bh)
            } else {
                bhkTxt.append(",")
                bhkTxt.append(bh)
            }

        }
        bhkTxt.append(" Bed Residences")
//        val price = project.project?.rate?.toFloat() ?: 0f
//        val inCr = price / 10000000
        val roundedPrice: String = "₹ " + project.project?.rate ?: ""
        /*  if (inCr >= 1) {
              roundedPrice = "₹ " + Math.round((inCr * 10)) / 10.0 + " Cr"
          } else {
              val inLakh = price / 100000
              roundedPrice = if (inLakh >= 1)  "₹ " + Math.round((inLakh * 10)) / 10.0 + " lakhs"
              else "₹ " +*/
//        }


        val bulletPair = arrayListOf(
            Pair("Address", project.project?.address ?: ""),
            Pair("Starting From", roundedPrice),
            Pair("Possession", project.project?.possessionDate?.convertToMMMDDYYY() ?: ""),
            Pair("Apartments", bhkTxt.toString())
        )
        val detailItem = ProjectDetailFragItem(
            bulletPair,
            project.project?.reraNumber ?: "",
            project.project?.reraWebsite ?: "",
            project.project?.projectTitle ?: "",
            project.project?.projectDesc ?: "",
            project.project?.projectName ?: "",
            "",
            project.DetailsPageImage ?: ArrayList(),
            project.project?.websiteUrl ?: ""
        )

        val locationItem = LocationFragItem(
            project.Location ?: LocationItem("", "", "", ""),
            project.Highlights ?: ArrayList(),
            project.AdditionalHighlights ?: ArrayList(),
            project.project?.projectName ?: ""
        )

        return ProjectUiModel(
            detailItem,
            locationItem,
            project.ConstructionProgress ?: ArrayList(),
            project.FloorPlan ?: ArrayList(),
            project.documentationList ?: ArrayList(),
            project.faqList ?: ArrayList(),
            project.Gallery ?: ArrayList(),
            project.testmonialsList ?: ArrayList(),
            project.ReferralList ?: ArrayList()
        )

//        TODO mock data below remove
//        val galleryList = ArrayList<GalleryItem>().also {
//            it.add(GalleryItem("https://homepages.cae.wisc.edu/~ece533/images/airplane.png", 1, "Test"))
//            it.add(GalleryItem("https://homepages.cae.wisc.edu/~ece533/images/airplane.png", 1, "Test"))
//        }
//
//        val bulletPair = arrayListOf(Pair("Address", "Abcd"), Pair("Starting From", "Abcd"),
//                Pair("Possession", "Abcd"), Pair("Apartments", "Abcd"))
//
//        val detail = ProjectDetailFragItem(galleryList,bulletPair,
//                "https://homepages.cae.wisc.edu/~ece533/images/airplane.png", "123","abcd")
//
//        val locationItem = LocationFragItem(LocationItem("",""), ArrayList(), project.additionalHighlightList, project.projectName)
//
//        val download = ArrayList<DownloadItem>().also {
//            it.add(DownloadItem("Doc1","url"))
//            it.add(DownloadItem("Doc1","url"))}
//
//        val faqs  = ArrayList<FaqItem>().also {
//            it.add(FaqItem("Answer","Qn"))
//            it.add(FaqItem("Answer","Qn"))
//        }
//        return ProjectUiModel(detail,galleryList,locationItem,download,GalleryFragItem(galleryList,galleryList),
//                faqs,"")

    }
}