package com.loyalie.connectre.network

import com.loyalie.connectre.data.LoginResponse
import com.loyalie.connectre.data.ReferalHolder
import com.loyalie.connectre.data.SharedPreferenceStorage
import com.loyalie.connectre.data.UserReponse
import com.loyalie.connectre.util.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(
    private val service: ConnectreService,
    private val schedulers: AppRxSchedulers,
    private val preferenceStorage: SharedPreferenceStorage
) {

    fun login(phone: String,code:String,email:String): Single<LoginResponse> =
        service.login(phone, 1,code,email).applySchedulers(schedulers)


    fun verifyOtp(otp: String, fcmToekn: String) =
        service.verifyOtp(preferenceStorage.userId, otp, fcmToekn, "android").applySchedulers(
            schedulers
        )

    fun getDevelopers() = service.getDevelopers(
        preferenceStorage.userId,
        PAGINATOR_ITEMS_PER_PAGE,
        1
    ).applySchedulers(schedulers)

    fun getDeveloperHomeData(vendorId: String) = service.getDeveloperHomeData(
        preferenceStorage.userId, vendorId,
        1,
        PAGINATOR_ITEMS_PER_PAGE,
        1,
        10000,1, 100000
    ).applySchedulers(schedulers)

    fun getProjects(vendorId: String, page: Int, procatID: Int) = service.getProjects(
        preferenceStorage.userId,
        vendorId,
        page,
        10000, procatID
    ).applySchedulers(schedulers)

    fun getEvents(vendorId: String, page: Int) = service.getEvents(
        preferenceStorage.userId,
        vendorId,
        page,
        PAGINATOR_ITEMS_PER_PAGE
    ).applySchedulers(schedulers)

    fun getProperties(vendorId: String,page: Int) =service.getProperties(preferenceStorage.userId.toInt(),vendorId.toInt(),page,
        PAGINATOR_ITEMS_PER_PAGE).applySchedulers(schedulers)
    fun getProjectDetail(projectId: String, vendorId: String) = service.getProjectDetails(
        preferenceStorage.userId,
        vendorId,
        projectId,
        PAGINATOR_ITEMS_PER_PAGE
    ).applySchedulers(schedulers)


    fun applyEvent(eventId: String) =
        service.applyEvent(preferenceStorage.userId, eventId).applySchedulers(schedulers)


    fun getPrograms(page: Int, vendorId: String) = service.getActivePrograms(
        preferenceStorage.userId,
        PAGINATOR_ITEMS_PER_PAGE,
        page,
        vendorId
    ).applySchedulers(schedulers)

    fun getAssociatedPrograms(vendorId: String, page: Int) = service.getAssociatedPrograms(
        preferenceStorage.userId,
        PAGINATOR_ITEMS_PER_PAGE,
        page,
        vendorId
    ).applySchedulers(schedulers)

    fun getLeadStatuses(programId: String, page: Int) = service.getLeadStatuses(
        preferenceStorage.userId,
        PAGINATOR_ITEMS_PER_PAGE,
        page,
        programId
    ).applySchedulers(schedulers)

    fun getOffers(programId: String, page: Int) = service.getOffers(
        preferenceStorage.userId,
        programId,
        page,
        PAGINATOR_ITEMS_PER_PAGE

    ).applySchedulers(schedulers)

    fun getRewards(page: Int, vendorId: String) = service.getRewards(
        preferenceStorage.userId,
        page,
        PAGINATOR_ITEMS_PER_PAGE,
        vendorId


    ).applySchedulers(schedulers)

    fun getBenefits(page: Int, vendorId: String, benefitCategoryId: Int) = service.getBenefits(
        preferenceStorage.userId,
        page,
        PAGINATOR_ITEMS_PER_PAGE,
        vendorId, benefitCategoryId
    ).applySchedulers(schedulers)

    fun checkIfNewUserExists(users: ReferalHolder) = service.checkIfNewuserExists(users)
        .applySchedulers(schedulers)

    fun getFAQs() =
        service.getFAQ(preferenceStorage.userId).applySchedulers(schedulers)

    fun getPrivacyPolicy() =
        service.getPrivacyPolicy(preferenceStorage.userId).applySchedulers(schedulers)

    fun getContactUs() =
        service.getContactUs(preferenceStorage.userId).applySchedulers(schedulers)

    fun getAppTutorial(page: Int) =
        service.getAppTutorial(
            preferenceStorage.userId,
            page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun referUsers(users: ReferalHolder, comment: String) = service.referUsers(users, comment)
        .applySchedulers(schedulers)

    fun uploadImage(file: MultipartBody.Part): Single<UserReponse> {
        val userIdBody = RequestBody.create(okhttp3.MultipartBody.FORM, preferenceStorage.userId)
        return service.updateImage(userIdBody, file).applySchedulers(schedulers)
    }

    fun getUserData() = service.getProfile(preferenceStorage.userId).applySchedulers(schedulers)

    fun updateUser(name: String, email: String, userdob: String, user_anniversary: String) =
        service.updateProfile(
            preferenceStorage.userId,
            name,
            email,
            userdob,
            user_anniversary
        ).applySchedulers(schedulers)

    fun changeMobile(mob: String,code: String,email: String) =
        service.changeNumber(preferenceStorage.userId, mob, 1,code,email).applySchedulers(schedulers)

    fun verifyChangeNumber(otp: String, phone: String) =
        service.verifyChangeNumber(preferenceStorage.userId, phone, otp).applySchedulers(schedulers)

    fun resendOtp(phone: String, isLogin: Boolean,ccdode:String,email: String) =
        service.resendOtp(
            phone,
            if (isLogin) 1 else 2,
            preferenceStorage.userId,
            1,ccdode,email
        ).applySchedulersForStatus(schedulers)


    fun getNotificationList(page: Int) =
        service.getNotificationList(
            preferenceStorage.userId,
            page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getNotificationStatus(push_id: String) =
        service.getNotificationStatus(preferenceStorage.userId, push_id)
            .applySchedulers(schedulers)

    fun addFeedback(vendorId: String, comment: String, rating: String) = service
        .addFeedback(preferenceStorage.userId, vendorId, comment, rating).applySchedulersForStatus(
            schedulers
        )

    fun getUnreadCount() =
        service.getNotificationCount(preferenceStorage.userId).applySchedulers(schedulers)

    fun getVersions() = service.getAppVersions(preferenceStorage.userId).applySchedulers(schedulers)

    fun getConstruction(projectId: String, page: Int) =
        service.getConstructionList(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getFloorPlan(projectId: String, page: Int) =
        service.getFloorPlan(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getGallery(projectId: String, page: Int) =
        service.getGallery(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getFaqs(projectId: String, page: Int) =
        service.getFaqs(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getTestimonials(projectId: String, page: Int) =
        service.getTestimonials(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getDocuments(projectId: String, page: Int) =
        service.getDocuments(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE
        ).applySchedulers(schedulers)

    fun getProgramsForProject(projectId: String, page: Int, vendorId: String) =
        service.getProgramsForProject(
            projectId, preferenceStorage.userId, page,
            PAGINATOR_ITEMS_PER_PAGE, vendorId
        ).applySchedulers(schedulers)

    fun registerEmployee(
        username: String,  address: String, flatNumber: String, custEmpId: String,
        projectId: String,
        designation: String,
        companyName: String
    ) =
        service.registerUserEmp(
            preferenceStorage.userId.toInt(),
            username,
            preferenceStorage.userType,
            address,
            flatNumber,
            custEmpId,
            projectId,
            designation,
            companyName
        ).applySchedulers(schedulers)

    fun getProjectList() =
        service.getProjectList(preferenceStorage.userId.toInt()).applySchedulers(schedulers)

    fun requestCallback(vendorPhone: String) =
        service.requestCallBack(vendorPhone, preferenceStorage.userId.toInt()).applySchedulers(
            schedulers
        )

    fun getDomainLIst() =
        service.getDomainList(preferenceStorage.userId.toInt()).applySchedulers(schedulers)

    fun verifyEmail(otp: String) =
        service.verifyEmail(preferenceStorage.userId.toInt(), otp).applySchedulers(schedulers)

    fun resendEmail() =
        service.resendEmail(preferenceStorage.userId.toInt()).applySchedulers(schedulers)

    fun getCategories(vendorId: String) =
        service.getCategory(preferenceStorage.userId, vendorId).applySchedulers(schedulers)

    fun getMilestone(soNo:String,page: Int,userId:Int)=
        ConnectReApp.networkService.getMilestone(/*,soNo, */userId,soNo, page,
            100000)
    fun getDemands(soNo:String,page: Int,userId:Int)=
        ConnectReApp.networkService.getDemands(/*preferenceStorage.userId.toInt(),soNo, */ userId,soNo,page,
            100000)
    fun getReceipts(soNo:String,page: Int,userId:Int)=
        ConnectReApp.networkService.getReciepts(/*preferenceStorage.userId.toInt(),soNo, */userId,soNo, page,
            100000)
    fun getOverview(soNo: Int,userId:Int)=
        ConnectReApp.networkService.getOverview(/*,, */userId,soNo)

}
