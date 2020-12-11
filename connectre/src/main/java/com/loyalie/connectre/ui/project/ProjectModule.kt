package com.loyalie.connectre.ui.project

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import com.loyalie.connectre.ui.offer.OffersFragment
import com.loyalie.connectre.ui.offer.ProjectOfferVM
import com.loyalie.connectre.ui.project.construction.ConstructionProgressFragment
import com.loyalie.connectre.ui.project.construction.ConstructionVM
import com.loyalie.connectre.ui.project.details.DetailsFragment
import com.loyalie.connectre.ui.project.documentation.DocumentVM
import com.loyalie.connectre.ui.project.documentation.DocumentationFragment
import com.loyalie.connectre.ui.project.faq.FAQFragment
import com.loyalie.connectre.ui.project.faq.FaqVM
import com.loyalie.connectre.ui.project.floor_plan.FloorPlanFragment
import com.loyalie.connectre.ui.project.floor_plan.FloorPlanVM
import com.loyalie.connectre.ui.project.gallery.GalleryFragment
import com.loyalie.connectre.ui.project.gallery.GalleryVM
import com.loyalie.connectre.ui.project.testimonial.TestimonialVM
import com.loyalie.connectre.ui.project.testimonial.TestimonialsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ProjectModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProjectVM::class)
    abstract fun bindProjectVM(viewModel: ProjectVM): ViewModel


    @ContributesAndroidInjector
    internal abstract fun detailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    internal abstract fun constructionFragment(): ConstructionProgressFragment

    @ContributesAndroidInjector
    internal abstract fun floorPlanFragment(): FloorPlanFragment

    @ContributesAndroidInjector
    internal abstract fun documentationFragment(): DocumentationFragment

    @ContributesAndroidInjector
    internal abstract fun galleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    internal abstract fun offersFragment(): OffersFragment

    @ContributesAndroidInjector
    internal abstract fun fAQFragment(): FAQFragment

    @ContributesAndroidInjector
    internal abstract fun testimonialsFragment(): TestimonialsFragment

    @Binds
    @IntoMap
    @ViewModelKey(ConstructionVM::class)
    abstract fun bindConstructionVM(viewModel: ConstructionVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryVM::class)
    abstract fun bindGalleryVM(viewModel: GalleryVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FloorPlanVM::class)
    abstract fun bindFloorPlanVM(viewModel: FloorPlanVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FaqVM::class)
    abstract fun bindFaqVM(viewModel: FaqVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DocumentVM::class)
    abstract fun bindDocumentVM(viewModel: DocumentVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestimonialVM::class)
    abstract fun bindTestimonialVM(viewModel: TestimonialVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectOfferVM::class)
    abstract fun bindProjectOfferVM(viewModel: ProjectOfferVM): ViewModel
}