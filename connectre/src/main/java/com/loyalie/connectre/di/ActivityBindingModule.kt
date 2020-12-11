package com.loyalie.connectre.di

import com.loyalie.connectre.ui.change_password.ChangePhoneNoActivity
import com.loyalie.connectre.ui.change_password.ChangePhoneNumberModule
import com.loyalie.connectre.ui.contact_us.ContactUsActivity
import com.loyalie.connectre.ui.contact_us.ContactUsModule
import com.loyalie.connectre.ui.dashboard.DashNotificationAct
import com.loyalie.connectre.ui.dashboard.DashboardAct
import com.loyalie.connectre.ui.dashboard.DashboardModule
import com.loyalie.connectre.ui.developers.SelectDeveloperActivity
import com.loyalie.connectre.ui.developers.SelectDeveloperModule
import com.loyalie.connectre.ui.enter_phn.EnterPhoneModule
import com.loyalie.connectre.ui.enter_phn.EnterPhoneNumberActivity
import com.loyalie.connectre.ui.event.EventActivity
import com.loyalie.connectre.ui.event.EventModule
import com.loyalie.connectre.ui.faq.FAQActivity
import com.loyalie.connectre.ui.faq.FAQModule
import com.loyalie.connectre.ui.feedback.FeedbackActivity
import com.loyalie.connectre.ui.feedback.FeedbackModule
import com.loyalie.connectre.ui.home.HomeActivity
import com.loyalie.connectre.ui.home.HomeModule
import com.loyalie.connectre.ui.image_gallery.ImagePagerActivity
import com.loyalie.connectre.ui.lead.details.LeadStatusDetailsActivity
import com.loyalie.connectre.ui.lead.details.LeadStatusModule
import com.loyalie.connectre.ui.notification.NotificationModule
import com.loyalie.connectre.ui.offer.OfferDetailsActivity
import com.loyalie.connectre.ui.offer.OfferModule
import com.loyalie.connectre.ui.otp.OtpActivity
import com.loyalie.connectre.ui.otp.OtpEmailActivity
import com.loyalie.connectre.ui.otp.OtpModule
import com.loyalie.connectre.ui.privacy_policy.PrivacyModule
import com.loyalie.connectre.ui.privacy_policy.PrivacyPolicyActivity
import com.loyalie.connectre.ui.profile.ProfileActivity
import com.loyalie.connectre.ui.profile.ProfileModule
import com.loyalie.connectre.ui.project.ProjectActivity
import com.loyalie.connectre.ui.project.ProjectModule
import com.loyalie.connectre.ui.refer.ReferListActivity
import com.loyalie.connectre.ui.refer.ReferListModule
import com.loyalie.connectre.ui.refer_contact.ReferContactListActivity
import com.loyalie.connectre.ui.refer_contact.ReferContactModule
import com.loyalie.connectre.ui.register.RegCustomerAct
import com.loyalie.connectre.ui.register.RegModule
import com.loyalie.connectre.ui.register.RegTypeAct
import com.loyalie.connectre.ui.register.RegisterAct
import com.loyalie.connectre.ui.rewards.BenefitsDetailAct
import com.loyalie.connectre.ui.rewards.NotiLeadsActivity
import com.loyalie.connectre.ui.rewards.RewardsActivity
import com.loyalie.connectre.ui.rewards.RewardsModule
import com.loyalie.connectre.ui.splash.SplashActivity
import com.loyalie.connectre.ui.tutorial.TutorialListingActivity
import com.loyalie.connectre.ui.tutorial.TutorialsListingModule
import com.loyalie.connectre.ui.tutorial.details.TutorialDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [EnterPhoneModule::class])
    internal abstract fun enterPhoneActivity(): EnterPhoneNumberActivity

    @ContributesAndroidInjector(modules = [OtpModule::class])
    internal abstract fun otpActivity(): OtpActivity

    @ContributesAndroidInjector(modules = [OtpModule::class])
    internal abstract fun otpEmailActivity(): OtpEmailActivity


    @ContributesAndroidInjector(modules = [SelectDeveloperModule::class])
    internal abstract fun selectDeveloperActivity(): SelectDeveloperActivity

    @ContributesAndroidInjector(modules = [HomeModule::class])
    internal abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector()
    internal abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector()
    internal abstract fun regAct(): RegTypeAct

    @ContributesAndroidInjector(modules = [RegModule::class])
    internal abstract fun regEmpAct(): RegisterAct


    @ContributesAndroidInjector(modules = [RegModule::class])
    internal abstract fun regCustomerAct(): RegCustomerAct


    @ContributesAndroidInjector(modules = [ProjectModule::class])
    internal abstract fun projectActivity(): ProjectActivity

    @ContributesAndroidInjector(modules = [ReferListModule::class])
    internal abstract fun referListActivity(): ReferListActivity

    @ContributesAndroidInjector(modules = [EventModule::class])
    internal abstract fun eventActivity(): EventActivity

    /*  @ContributesAndroidInjector(modules = [LeadListModule::class])
      internal abstract fun leadListActivity(): LeadListActivity*/

    @ContributesAndroidInjector(modules = [LeadStatusModule::class])
    internal abstract fun leadStatusDetailsActivity(): LeadStatusDetailsActivity

    @ContributesAndroidInjector(modules = [OfferModule::class])
    internal abstract fun offerDetailsActivity(): OfferDetailsActivity

    @ContributesAndroidInjector(modules = [FAQModule::class])
    internal abstract fun faqActivity(): FAQActivity

    @ContributesAndroidInjector(modules = [PrivacyModule::class])
    internal abstract fun privacyActivity(): PrivacyPolicyActivity

    @ContributesAndroidInjector(modules = [RewardsModule::class])
    internal abstract fun rewardsActivity(): RewardsActivity

    @ContributesAndroidInjector()
    internal abstract fun benefitsDetail(): BenefitsDetailAct

    @ContributesAndroidInjector(modules = [ReferContactModule::class])
    internal abstract fun referContactListActivity(): ReferContactListActivity

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    internal abstract fun profileActivity(): ProfileActivity

    @ContributesAndroidInjector(modules = [ChangePhoneNumberModule::class])
    internal abstract fun changePhoneNoActivity(): ChangePhoneNoActivity


    @ContributesAndroidInjector(modules = [ContactUsModule::class])
    internal abstract fun contactUsActivity(): ContactUsActivity

    @ContributesAndroidInjector(modules = [NotificationModule::class])
    internal abstract fun notificationActivity(): NotiLeadsActivity

    @ContributesAndroidInjector(modules = [TutorialsListingModule::class])
    internal abstract fun tutorialsListingActivity(): TutorialListingActivity

    @ContributesAndroidInjector(modules = [FeedbackModule::class])
    internal abstract fun feedbackActivity(): FeedbackActivity

    @ContributesAndroidInjector
    internal abstract fun imagePagerActivity(): ImagePagerActivity

    @ContributesAndroidInjector
    internal abstract fun tutorialDetailsActivity(): TutorialDetailsActivity

    @ContributesAndroidInjector(modules = [DashboardModule::class])
    internal abstract fun dashBoardAct(): DashboardAct

    @ContributesAndroidInjector
    internal abstract fun dashNotiBoardAct(): DashNotificationAct


}