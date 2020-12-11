package com.loyalie.connectre.data

import com.loyalie.connectre.ui.rewards.RewardWrapper
import com.loyalie.connectre.util.convertToMMMMYYYY
import java.io.Serializable
import kotlin.collections.ArrayList

data class FaqList(val faqList: List<FaqItem>)

data class FaqItem(
    val faqId: String,
    val faqQues: String?,
    val faqAns: String?,
    val question: String?,
    val answer: String?
) : Serializable

data class PrivacyPolicy(val privacyPolicy: String)

data class ContactUsResp(val contactUs: ContactUs)

data class ContactUs(
    val contactId: String,
    val contactLat: Double?,
    val contactLong: Double?,
    val contactTitle: String?,
    val contactDesc: String?,
    val contactPh: String?,
    val contactEmail: String?
)

data class NotificationResp(
    val notificationList: List<NotificationItem>,
    val notificationListCount: Int
)

data class NotificationItem(
    val push_id: String,
    val message: String?,
    val title: String?,
    val prog_id: String?,
    val type: Int,
    val timestamp: String?,
    var read_status: Int?,
    val vendor_id: String?,
    val card_id: String?
)

data class AppTutorialsResp(
    val AppTutorials: List<TutorialItem>,
    val AppTutorialCount: Int
)

data class TutorialItem(
    val appTutorialId: String,
    val appTutorialLink: String?,
    val appTutorialDesc: String?,
    val appTutorialDate: String?,
    val appTutorialStatus: String?,
    val appTutorialTitle: String?
) : Serializable

data class AddressDetailItem(val heading: String, val value: String)

data class VenueItem(val name: String, val distance: String)

data class GalleryItem(
    val url: String?, val tagLine: String?, var imageUrl: String?,
    var width: Int, var height: Int
) : Serializable


data class LoginResponse(
    val userId: String,
    val sec_key: String
)


data class UserItem(
    val user_id: String,
    val user_name: String,
    val user_email: String,
    val user_phone: String,
    val user_avatar: String?,
    val dob: String?,
    val anniversary: String?,
    val app_status: Int,
    val regDone: Int,
    val userType: Int,
    val emailVerify: Int
)

data class UserReponse(val userDetails: UserItem)

data class DeveloperItem(
    val vendor_id: String,
    val vendor_name: String,
    val vendor_logo: String?,
    val vendor_image: String?,
    val colorcode: String?,
    val vendor_phone: String?
) : Serializable

data class DeveloperResponse(val developerList: List<DeveloperItem>)

data class BannerImageItem(
    val imageId: String,
    val imageName: String
)


data class EventItem(
    val eventId: String,
    val eventName: String,
    val eventImage: String?,
    val eventDescription: String?,
    val eventStartDate: String,
    val eventEndDate: String,
    val eventLocation: String?,
    var userStatus: Int
) : Serializable

data class HomeResponse(
    val developerDetails: DeveloperItem,
    val bannerImageList: List<BannerImageItem>,
    val projectCategorys: List<projectCategorys>,
    val eventList: List<EventItem>,
    val footerImageList: List<BannerImageItem>,
    val propertiesList: List<propertiesList>, val propertiesListCount: Int
)

data class propertiesList(
    val id: Int,
    val sales_order_id: Int,
    val amount: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val user_id: Int
)

data class ProjectResponse(val projectList: List<ProjectItem>, val projectListCount: Int)
data class ProjectItem(
    val projectId: String,
    val projectName: String?,
    val apartment: String?,
    val rate: String?,
    val constructionStatus: Int?,
    val projectLogo: String?,
    val projectBgImage: String?,
    val developedBy: String?,
    val address: String?,
    val possessionDate: String,
    val reraNumber: String?,
    val reraWebsite: String?,
    val projectTitle: String?,
    val projectDesc: String,
    val websiteUrl: String?, val projectCategory: Int, val categoryName: String?, val vendor_id: Int
)

data class projectCategorys(
    val pcatId: Int,
    val categoryName: String,
    val projects: List<ProjectItem>,
    val count: Int
)

data class LocationItem(
    val latitude: String?, val longitude: String?,
    val googleLocation: String?, val locationAddress: String?
) : Serializable

data class HighlightItem(val type: String?, val details: ArrayList<HighlightDetail>?) : Serializable

data class HighlightDetail(
    val title: String,
    val description: String
) : Serializable

data class AdditionalHighlightItem(val pahTitle: String, val pahDescription: String) : Serializable

data class TestimonialItem(
    val tstPersonName: String,
    val tstDesignation: String?,
    val tstRating: String,
    val tstDetails: String?,
    val tstPersonImage: String?
) : Serializable

data class DocumentItem(val docUrl: String, val docName: String) : Serializable

data class ProjectDetailItem(
    val project: ProjectItem?,
    val documentationList: ArrayList<DocumentItem>?,
    val documentationListCount: Int,
    val faqList: ArrayList<FaqItem>?,
    val faqListCount: Int,
    val testmonialsList: ArrayList<TestimonialItem>?,
    val testmonialsListCount: Int,
    val ConstructionProgress: ArrayList<GalleryItem>?,
    val ConstructionCount: Int,
    val FloorPlan: ArrayList<GalleryItem>?,
    val FloorCount: String,
    val Gallery: ArrayList<GalleryItem>?,
    val GalleryCount: Int,
    val ReferralList: ArrayList<ProgramItem>?,
    val ReferralListCount: Int,
    val Location: LocationItem?,
    val Highlights: List<HighlightItem>?,
    val AdditionalHighlights: List<AdditionalHighlightItem>?,
    val DetailsPageImage: List<GalleryItem>?
)


data class ProjectUiModel(
    val projectDetailFragItem: ProjectDetailFragItem,
    val locationItem: LocationFragItem,
    val constructionProgress: ArrayList<GalleryItem>,
    val floorPlan: ArrayList<GalleryItem>,
    val documents: ArrayList<DocumentItem>,
    val faqList: ArrayList<FaqItem>,
    val gallery: ArrayList<GalleryItem>,
    val testimonials: ArrayList<TestimonialItem>,
    val offers: ArrayList<ProgramItem>
) : Serializable


data class ProjectDetailFragItem(
    val bulletPairs: ArrayList<Pair<String, String>>,
    val reraNumber: String,
    val reraWebsite: String,
    val projectTitle: String,
    val projectDesc: String,
    val projectName: String,
    val disclaimer: String?,
    val detailImageList: List<GalleryItem>,
    val websiteUrl: String
) : Serializable

data class LocationFragItem(
    val location: LocationItem,
    val highlights: List<HighlightItem>,
    val addnlHls: List<AdditionalHighlightItem>,
    val projectName: String
) : Serializable


data class ProgramItem(
    val referral_name: String,
    val referral_description: String?,
    val referral_image: String?,
    val referral_end_date: String,
    val referral_start_date: String,
    val referral_id: String,
    val referral_url: String?
) : Serializable

data class ProgramResponse(
    val ReferralList: List<ProgramItem>,
    val ReferralListCount: Int
)

data class AssociatedProgramItem(
    val referral_id: String,
    val referral_name: String,
    val referral_description: String,
    val referral_image: String?
)

data class AssociatedProgramResponse(
    val associatedPgmList: List<AssociatedProgramItem>,
    val associatedPgmListCount: Int
)

data class LeadItem(
    val userName: String,
    val userPhone: String,
    val referralName: String,
    val referralDesc: String,
    val referralStampStatus: Int,
    val referralImage: String?,
    val sfdcLeadStatus: String?
)

data class LeadResponse(
    val leadStatus: List<LeadItem>,
    val leadStatusCount: Int,
    val PgmTitle: String,
    val PgmDescription: String,
    val PgmImage: String
)

data class ApplyEventResponse(val eventStatus: Int)


data class OfferItem(
    val rewardName: String,
    val rewardDescription: String,
    val rewardStatus: Int,
    val rewardCode: String?,
    val visitToGo: Int?,
    val claimDate: String
)

data class OfferResponse(
    val OfferDetailsCount: Int,
    val ShareLink: String?,
    val PgmTitle: String?,
    val PgmDescription: String,
    val OfferDetails: List<OfferItem>
)

data class RewardItem(
    val reward_description: String?,
    val reward_name: String?,
    val claim_direct: String,
    val claim_status: Int,
    val vendor_logo: String?,
    val prog_name: String,
    val expiry_date: String

)

data class RewardDetails(
    val reward_description: String?,
    val reward_name: String?,
    val claim_direct: String,
    val claim_status: Int,
    val vendor_logo: String?,
    val prog_name: String,
    val expiry_date: String

)

data class RewardsResponse(val RewardDetails: List<RewardItem>)
data class ReferalRewardsResponse(val RewardDetails: List<RewardItem>)


data class BenefitItem(
    val description: String?,
    val url: String?,
    val title: String?,
    val logo: String?,
    val status: Int,
    val couponCode: String?,
    val expiryDate: String?,
    val benefitCategoryId: Int?,
    val benefitCategoryName: String?,
    val termsCondition: String?

) : Serializable, RewardWrapper {
    override fun title() = title ?: ""

    override fun subTitle() = description ?: ""

    override fun expiry() = expiryDate?.convertToMMMMYYYY() ?: ""

    override fun code() = couponCode

    override fun img() = logo

    override fun url() = url
    override fun catID() = benefitCategoryId

    override fun catName() = benefitCategoryName
    override fun termsCondition() = termsCondition

}

data class BenefitResponse(val Benefits: List<BenefitItem>)

data class ContactItem(
    val ex_user_id: String,
    val ex_user_name: String,
    val new_user_name: String,
    val new_user_phone: String,
    val new_user_email: String,
    val referral_id: String,
    val referral_user_id: String
)

data class ReferalHolder(val multipleReferrals: List<ContactItem>)

data class ReferredUserStatusItem(
    val userId: String,
    val userName: String?,
    val status: Int,
    val user_phone: String
)

data class ReferralResponse(val userStatus: List<ReferredUserStatusItem>)

data class NotificationCountItem(val notificationCount: Int)

data class VersionReponse(
    val CurrentAndroidVersion: Int,
    val MinAndroidVersion: Int
)

data class ConstructionProgressResponse(
    val ConstructionProgress: List<GalleryItem>,
    val ConstructionCount: Int
)

data class FloorPlanresponse(val FloorPlan: List<GalleryItem>, val FloorCount: Int)

data class GalleryResponse(val Gallery: List<GalleryItem>, val GalleryCount: Int)

data class FaqResponse(val faqList: List<FaqItem>, val faqListCount: Int)

data class TestimonialResponse(
    val testmonialsList: List<TestimonialItem>,
    val testmonialsListCount: Int
)

data class DocumentResponse(
    val documentationList: List<DocumentItem>,
    val documentationListCount: Int
)

data class ProjectOfferResponse(
    val ReferralList: List<ProgramItem>,
    val ReferralListCount: Int,
    val ShareLink: String
)

data class RegResponse(val userDetails: userDetails)

data class userDetails(
    val user_id: Int,
    val user_name: String,
    val user_email: String,
    val user_phone: String,
    val user_avatar: String,
    val regDone: Int,
    val userType: Int,
    val address: String,
    val flatNumber: String,
    val custEmpId: String,
    val companyName: String,
    val designation: String
)

data class domainList(val domainId: Int, val domainName: String, val domainStatus: Int)

data class domainResponse(val domainList: List<domainList>)

data class catResponse(val categoryList: List<categoryList>)
data class categoryList(
    val benefitCategoryId: Int,
    val benefitCategoryName: String,
    val benefitCategoryStatus: Int,
    val benefitCategoryDate: String?,
    val benefitCategoryVendorId: Int
)

data class MilestoneList(
    val allotmentDays: String,
    val billingAmount: String,
    val milestoneDate: String,
    val billingStatus: String,
    val milestoneId: Int
)

data class milestoneRes(val MilestoneList: List<MilestoneList>, val totalRecords: Int)
data class demandsRes(val DemandList: List<DemandList>, val totalRecords: Int)

data class DemandList(
    val allotmentDays: String,
    val billingTotalAmountDue: Double,
    val invoiceNumber: String,
    val serviceTaxValue: Double,
    val id: Int,
    val sbcValue: Double,
    val kkcValue: Double,
    val cgstValue: Double,
    val sgstValue: Double,
    val tdsValue: Double,
    val vatValue: Double,
    var isExpanded: Boolean
) {

}

data class receiptRes(val ReceiptList: List<ReceiptList>, val totalRecords: Int)
data class ReceiptList(
    val allotmentDays: String,
    val totalAmount: Double, /*val map:HashMap<String,String>?,*/
    val receiptNo: String,
    var aggregatedReceipts: List<aggregatedReceipts>?
)

data class aggregatedReceipts(
    val documentNumber: String,
    val paymentCategory: String,
    val amount: Double
)

data class paymentResponse(val paymentOverView: paymentOverView)
data class paymentOverView(
    val amountPaid: Double,
    val agreementValue: Double,
    val totalApplicableTaxes: Double,
    val totalBilledValue: Double,
    val towardsBilledPrincipal: Double,
    val towardsBilledTds: Double,
    val towardsBilledTaxes: Double,
    val towardsBilledInterest: Double,
    val totalUnBilledValue: Double,
    val towardsUnBilledPrincipal: Double,
    val towardsUnBilledTds: Double,
    val towardsUnBilledTaxes: Double,
    val towardsUnBilledInterest: Double,
    val totalPaid: Double,
    val towardsPaymentPrincipal: Double,
    val towardsPaymentTds: Double,
    val towardsPaymentTaxes: Double,
    val towardsPaymentInterest: Double,
    val towardsOutstandingAmount: Double,
    val towardsOutstandingPrincipal:Double,
    val towardsOutstandingTds:Double,
    val towardsOutstandingTaxes:Double,
    val towardsOutstandingInterest:Double,
    val allotmentDays: String,
    val upcomingAmount: Double,
    val dueDate: String,   val towardsPaymentOtherCharges:Double,val enteredDate:String
)

data class greifList(
    val name: String,
    val desc: String,
    val longDesc: String,
    val date: String,
    val status: String,
    val id: Int
)

//data class PropertyList(val name:String,val desc:String)
data class BillingDummyList(
    val name: String,
    val value: Double,
    val status: Int,
    val color: String,
    val id: Int,
    val principal: Double,
    val tds: Double,
    val gst: Double,
    val intereset: Double,
    val towardsPaymentOtherCharges:Double
)