package com.loyalie.connectre.network

import com.loyalie.connectre.data.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ConnectreService {

    @FormUrlEncoded
    @POST("api_mbuser/loginApi")
    fun login(
        @Field("user_phone") user_phone: String,
        @Field("type") type: Int,
        @Field("country_code") country_code: String,
        @Field("user_email") user_email: String
    ): Single<ApiResponse<LoginResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/otpLogin")
    fun verifyOtp(
        @Field("userId") userId: String,
        @Field("otp_password") otp_password: String,
        @Field("deviceRegId") deviceRegId: String,
        @Field("deviceType") deviceType: String
    ): Single<ApiResponse<UserReponse>>

    @FormUrlEncoded
    @POST("api_mbuser/developerList")
    fun getDevelopers(
        @Field("userId") userId: String,
        @Field("recordsper_page") recordsper_page: Int,
        @Field("page") page: Int
    ): Single<ApiResponse<DeveloperResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/developerHome")
    fun getDeveloperHomeData(
        @Field("userId") userId: String,
        @Field("vendorId") vendorId: String,
        @Field("eventPage") eventPage: Int,
        @Field("eventRecordsPerPage") eventRecordsPerPage: Int,
        @Field("projectPage") projectPage: Int,
        @Field("proRecordsPerPage") proRecordsPerPage: Int,
        @Field("propertiesPage") propertiesPage: Int,
        @Field("propertiesRecordsPerPage") propertiesRecordsPerPage: Int
    ): Single<ApiResponse<HomeResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/developerHome_ProjectList")
    fun getProjects(
        @Field("userId") userId: String,
        @Field("vendorId") vendorId: String,
        @Field("projectPage") projectPage: Int,
        @Field("proRecordsPerPage") proRecordsPerPage: Int,
        @Field("proCatId") proCatId: Int
    ): Single<ApiResponse<ProjectResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/developerHome_EventList")
    fun getEvents(
        @Field("userId") userId: String,
        @Field("vendorId") vendorId: String,
        @Field("eventPage") eventPage: Int,
        @Field("eventRecordsPerPage") eventRecordsPerPage: Int
    ): Single<ApiResponse<HomeResponse>>


    @FormUrlEncoded
    @POST("api_mbuser/developerHome_PropertiesList")
    fun getProperties(
        @Field("userId") userId: Int,
        @Field("vendorId") vendorId: Int,
        @Field("propertiesPage") propertiesPage: Int,
        @Field("prtyRecordsPerPage") prtyRecordsPerPage: Int
    )
            : Single<ApiResponse<HomeResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/projectDetails")
    fun getProjectDetails(
        @Field("userId") userId: String,
        @Field("vendorId") vendorId: String,
        @Field("projectId") projectId: String,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<ProjectDetailItem>>

    @FormUrlEncoded
    @POST("api_mbuser/apply_events")
    fun applyEvent(
        @Field("user_id") userId: String,
        @Field("event_id") event_id: String
    ): Single<ApiResponse<ApplyEventResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/activeReferralUser")
    fun getActivePrograms(
        @Field("user_id") user_id: String,
        @Field("recordsPerPage") recordsper_page: Int,
        @Field("page") page: Int,
        @Field("vendor_id") vendorId: String
    ): Single<ApiResponse<ProgramResponse>>

    @POST("api_mbuser/addNewReferrals")
    fun referUsers(
        @Body referalHolder: ReferalHolder, @Query("comments") comment: String
    ): Single<ApiResponse<ReferralResponse>>

    @POST("api_mbuser/checkExistingUser")
    fun checkIfNewuserExists(
        @Body referalHolder: ReferalHolder
    ): Single<ApiResponse<ReferralResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/associatedPgmList")
    fun getAssociatedPrograms(
        @Field("user_id") userId: String,
        @Field("recordsPerPage") recordsper_page: Int,
        @Field("page") page: Int,
        @Field("vendor_id") vendor_id: String
    ): Single<ApiResponse<AssociatedProgramResponse>>


    @FormUrlEncoded
    @POST("api_mbuser/leadStatus")
    fun getLeadStatuses(
        @Field("user_id") userId: String,
        @Field("recordsPerPage") recordsper_page: Int,
        @Field("page") page: Int,
        @Field("referral_id") referral_id: String
    ): Single<ApiResponse<LeadResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/offerDetails")
    fun getOffers(
        @Field("userId") userId: String,
        @Field("referralId") referralId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<OfferResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/faq")
    fun getFAQ(
        @Field("userId") userId: String
    ): Single<ApiResponse<FaqList>>

    @FormUrlEncoded
    @POST("api_mbuser/rewardDetails")
    fun getRewards(
        @Field("user_id") user_id: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int,
        @Field("vendorId") vendorId: String
    ): Single<ApiResponse<RewardsResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/getBenefits")
    fun getBenefits(
        @Field("user_id") user_id: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int,
        @Field("vendorId") vendorId: String,
        @Field("benefitCategoryId") benefitCategoryId: Int
    ): Single<ApiResponse<BenefitResponse>>

    @Multipart
    @POST("api_mbuser/updateUserImage")
    fun updateImage(
        @Part("user_id") user_id: RequestBody,
        @Part file: MultipartBody.Part?
    ): Single<ApiResponse<UserReponse>>

    @FormUrlEncoded
    @POST("api_mbuser/getUserProfile")
    fun getProfile(
        @Field("user_id") user_id: String
    ): Single<ApiResponse<UserReponse>>

    @FormUrlEncoded
    @POST("api_mbuser/privacyPolicy")
    fun getPrivacyPolicy(
        @Field("userId") userId: String
    ): Single<ApiResponse<PrivacyPolicy>>

    @FormUrlEncoded
    @POST("api_mbuser/contactUs")
    fun getContactUs(
        @Field("user_id") user_id: String
    ): Single<ApiResponse<ContactUsResp>>

    @FormUrlEncoded
    @POST("api_mbuser/UpdateUserProfile")
    fun updateProfile(
        @Field("user_id") user_id: String,
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_dob") user_dob: String,
        @Field("user_anniversary") user_anniversary: String
    ): Single<ApiResponse<UserReponse>>

    @FormUrlEncoded
    @POST("api_mbuser/getOTP_changeNumber")
    fun changeNumber(
        @Field("user_id") user_id: String,
        @Field("user_phone") user_phone: String,
        @Field("type") type: Int,
        @Field("country_code") country_code: String,
        @Field("user_email") user_email: String
    ): Single<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("api_mbuser/verifyOTP_changeNumber")
    fun verifyChangeNumber(
        @Field("user_id") user_id: String,
        @Field("user_phone") user_phone: String,
        @Field("otp_password") otp_password: String
    ): Single<ApiResponse<UserReponse>>

    @FormUrlEncoded
    @POST("api_mbuser/resendOTP")
    fun resendOtp(
        @Field("user_phone") user_phone: String,
        @Field("otpType") otpType: Int,
        @Field("userId") userId: String,
        @Field("type") type: Int,
        @Field("country_code") country_code: String,
        @Field("user_email") user_email: String
    ): Single<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("api_mbuser/AppTutuorial")
    fun getAppTutorial(
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<AppTutorialsResp>>

    @FormUrlEncoded
    @POST("api_mbuser/notificationList")
    fun getNotificationList(
        @Field("user_id") user_id: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<NotificationResp>>

    @FormUrlEncoded
    @POST("api_mbuser/read_push_info")
    fun getNotificationStatus(
        @Field("user_id") user_id: String,
        @Field("push_id") push_id: String
    ): Single<ApiResponse<Unit>>

    @FormUrlEncoded
    @POST("api_mbuser/vendor_feedback")
    fun addFeedback(
        @Field("user_id") user_id: String,
        @Field("vendor_id") vendor_id: String,
        @Field("comments") comments: String,
        @Field("rating") rating: String
    ): Single<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("api_mbuser/unReadCount")
    fun getNotificationCount(
        @Field("user_id") user_id: String
    ): Single<ApiResponse<NotificationCountItem>>


    @FormUrlEncoded
    @POST("api_mbuser/getMobileVersion")
    fun getAppVersions(
        @Field("user_id") user_id: String
    ): Single<ApiResponse<VersionReponse>>

    @FormUrlEncoded
    @POST("api_mbuser/constructionImageList")
    fun getConstructionList(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<ConstructionProgressResponse>>


    @FormUrlEncoded
    @POST("api_mbuser/floorPlanImageList")
    fun getFloorPlan(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<FloorPlanresponse>>

    @FormUrlEncoded
    @POST("api_mbuser/galleryImageList")
    fun getGallery(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<GalleryResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/faqList")
    fun getFaqs(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<FaqResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/testmonialsList")
    fun getTestimonials(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<TestimonialResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/documentationList")
    fun getDocuments(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): Single<ApiResponse<DocumentResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/ReferralList")
    fun getProgramsForProject(
        @Field("projectId") projectId: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("recordsPerPage") recordsPerPage: Int,
        @Field("vendorId") vendorId: String

    ): Single<ApiResponse<ProjectOfferResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/registerUserEmp")
    fun registerUserEmp(
        @Field("userId") userId: Int,
        @Field("user_name") user_name: String,
        @Field("userType") userType: Int,
        @Field(
            "user_email"
        ) user_email: String,
        @Field("flatNumber") flatNumber: String,
        @Field("custEmpId") custEmpId: String,
        @Field("projectId") projectId: String,
        @Field("designation") designation: String,
        @Field("companyName") companyName: String
    ): Single<ApiResponse<RegResponse>>


    @FormUrlEncoded
    @POST("api_mbuser/projectList")
    fun getProjectList(@Field("userId") userId: Int): Single<ApiResponse<ProjectResponse>>


    @FormUrlEncoded
    @POST("api_mbuser/requestCall")
    fun requestCallBack(
        @Field("vendorPhone") vendorPhone: String,
        @Field("user_id") user_id: Int
    ): Single<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("api_mbuser/domainList")
    fun getDomainList(@Field("userId") userId: Int): Single<ApiResponse<domainResponse>>

    @FormUrlEncoded
    @POST("api_mbuser/verifyEmail")
    fun verifyEmail(
        @Field("userId") userId: Int,
        @Field("emailVerifyCode") emailVerifyCode: String
    ): Single<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("api_mbuser/resendEmailCode")
    fun resendEmail(@Field("userId") userId: Int): Single<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("api_mbuser/getBenefitCategoryList")
    fun getCategory(
        @Field("user_id") user_id: String,
        @Field("vendorId") vendorId: String
    ): Single<ApiResponse<catResponse>>

    @FormUrlEncoded
    @POST("ApiPayment/getMilestones")
    fun getMilestone(
        @Field("userId") userId: Int,
        @Field("salesOrderId") salesOrderId: String,
        @Field("offset") offset: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): retrofit2.Call<milestoneRes>

    @FormUrlEncoded
    @POST("ApiPayment/getDemands")
    fun getDemands(
        @Field("userId") userId: Int,
        @Field("salesOrderId") salesOrderId: String,
        @Field("offset") offset: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ):
            retrofit2.Call<demandsRes>

    @FormUrlEncoded
    @POST("ApiPayment/getReceipts")
    fun getReciepts(
        @Field("userId") userId: Int,
        @Field("salesOrderId") salesOrderId: String,
        @Field("offset") offset: Int,
        @Field("recordsPerPage") recordsPerPage: Int
    ): retrofit2.Call<receiptRes>

    @FormUrlEncoded
    @POST("ApiPayment/getPaymentOverview")
    fun getOverview(
        @Field("userId") userId: Int,
        @Field("salesOrderId") salesOrderId: Int

    ): retrofit2.Call<paymentResponse>

}