package com.loyalie.connectre.ui.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ProjectUiModel
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.offer.OffersFragment
import com.loyalie.connectre.ui.project.details.DetailsFragment
import com.loyalie.connectre.ui.project.documentation.DocumentationFragment
import com.loyalie.connectre.ui.project.gallery.GalleryFragment
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_project.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import java.util.*
import javax.inject.Inject

class ProjectActivity : BaseActivity(), View.OnClickListener {
    lateinit var projectPagerAdapter: ProjectPagerAdapter

    private val mFragments = ArrayList<Fragment>()

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    lateinit var projectId: String
    lateinit var vendorId: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ProjectVM


    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        projectId = intent.getStringExtra(PROJECT_ID)!!
        vendorId = intent.getStringExtra(VENDOR_ID)!!
        viewModel = viewModelProvider(viewModelFactory)
        initToolbar()

        viewModel.getProjectData(projectId, vendorId)

        initClickListener()

        observeVM()
    }

    private fun setUpVP(project: ProjectUiModel) {
        tab_layout.setVisible()
        mFragments.clear()
        mFragments.add(OffersFragment.newInstance(project.offers, projectId, vendorId))
        mFragments.add(DetailsFragment.newInstance(project.projectDetailFragItem))
//        mFragments.add(LocationFragment.newInstance(project.locationItem))
//        mFragments.add(ConstructionProgressFragment.newInstance(project.constructionProgress,projectId))
//        mFragments.add(FloorPlanFragment.newInstance(project.floorPlan,projectId))
        mFragments.add(GalleryFragment.newInstance(project.gallery, projectId))
        mFragments.add(DocumentationFragment.newInstance(project.documents, projectId))

//        mFragments.add(FAQFragment.newInstance(project.faqList,projectId))
//        mFragments.add(TestimonialsFragment.newInstance(project.testimonials,projectId))


        tab_layout.post {
            tab_layout.changeTextFonts(0, this@ProjectActivity.getUbunduBold()!!, 18f)
        }
        setTabSelection(tab_layout)
        projectPagerAdapter = ProjectPagerAdapter(this, supportFragmentManager, mFragments)
        projectVP.adapter = projectPagerAdapter
        tab_layout.setupWithViewPager(projectVP)

    }

    private fun setTabSelection(tab_layout: CustomTabLayout?) {
        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    tab_layout.changeTextFonts(it.position, this@ProjectActivity.getUbundu()!!, 14f)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    tab_layout.changeTextFonts(
                        it.position,
                        this@ProjectActivity.getUbunduBold()!!,
                        18f
                    )
                }
            }
        })
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        projectToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = intent.getStringExtra(PROJECT_NAME)
        tab_layout.setBackgroundColor(extractedColor)

    }

    private fun observeVM() {
        viewModel.projectUIModel.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    setUpVP(it.data)


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.load()
                }
            }
        })
    }

    companion object {
        const val PROJECT_ID = "project_id"
        const val VENDOR_ID = "vendor_id"
        const val PROJECT_NAME = "project_name"
        fun start(context: Context, projectId: String, vendorId: String, projectName: String) {
            context.startActivity(Intent(context, ProjectActivity::class.java).apply {
                putExtra(PROJECT_ID, projectId)
                putExtra(VENDOR_ID, vendorId)
                putExtra(PROJECT_NAME, projectName)
            })
        }
    }

}
