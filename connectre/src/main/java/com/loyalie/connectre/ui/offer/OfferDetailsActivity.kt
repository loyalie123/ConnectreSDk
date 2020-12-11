package com.loyalie.connectre.ui.offer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.OfferItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.developers.SelectDeveloperActivity
import com.loyalie.connectre.ui.refer_contact.InputDetailsDialog
import com.loyalie.connectre.ui.refer_contact.ReferContactListActivity
import com.loyalie.connectre.ui.refer_contact.ReferSuccessDialog
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_offer_details.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import kotlinx.android.synthetic.main.offer_bottom_sheet.*
import javax.inject.Inject


class OfferDetailsActivity : BaseActivity(), View.OnClickListener,
    OfferDetailsAdapter.onSharecallback {


    @Inject
    lateinit var picasso: Picasso

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    private val offers = ArrayList<OfferItem>()
    lateinit var offersAdapter: OfferDetailsAdapter
    lateinit var scrollListener: NestedScrollListener
    var commentDialog: InputDetailsDialog? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: OfferVM


    lateinit var programId: String
    lateinit var programName: String
    lateinit var programDesc: String
    lateinit var referalLink: String
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_details)
        programId = intent.getStringExtra(PROGRAM_ID)!!
        programName = intent.getStringExtra(PROGRAM_NAME)!!
        referalLink = intent.getStringExtra(REFERAL_LINK)!!
        programDesc = intent.getStringExtra(PROGRAM_DESC)!!
//        programNameTV.setText(programName)
//        programDescTV.setText(programDesc)
        viewModel = viewModelProvider(viewModelFactory)
//        viewModelRefer = viewModelProvider(viewModelFactory)
        initToolbar()

        initRV()

        initClickListener()
        viewModel.getOffers(programId)

        swipeRL.setOnRefreshListener {
            offers.clear()
            offersAdapter.notifyDataSetChanged()
            scrollListener.reset(0, false)
            viewModel.getOffers(programId, isInitial = true, isRefresh = true)
        }

        observeVM()
        setBottomSheet()
        preferenceStorage.tandC = "https://shapoorji.loyalie.in/terms_conditions_referral.html"
    }

    private fun initRV() {

        val layoutManager = LinearLayoutManager(this)
        offerDetailsRV.layoutManager = layoutManager
        offersAdapter = OfferDetailsAdapter(this, offers, programId, programName, programDesc, this)
        offerDetailsRV.adapter = offersAdapter

        offerDetailsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                    if (offerDetailsRV.findViewHolderForAdapterPosition(i) is OfferDetailsAdapter.OfferVH) {
                        (offerDetailsRV.findViewHolderForAdapterPosition(i) as OfferDetailsAdapter.OfferVH).rotateOfferImageBackground(
                            dy
                        )
                    }
                }
            }
        })


        scrollListener = object : NestedScrollListener(layoutManager, 20) {

            override fun onLoadMore() {
                viewModel.getOffers(programId, false, false)
            }
        }
        offerDetailsRV.addOnScrollListener(scrollListener)
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(shareSheet)

        bottomSheetBehavior?.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                closeBtn.alpha = slideOffset
            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        closeBtn.setVisible()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        closeBtn.setGone()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }

        })

        shareTitleTV.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        }

        closeBtn.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED

        }




        bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)

        shareTitleTV.setBackgroundColor(ConnectReApp.themeColor)

        contactShareBtn.setOnClickListener {
            ReferContactListActivity.start(
                this,
                programId,
                programName
            )
        }

        socialMediaShareBtn.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out $programName and attain exclusive offers!\n$referalLink"
                )
            }

            if (sharingIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(sharingIntent, "Share via"))
            }

        }
        conDetailShareBtn.setOnClickListener {
            commentDialog =
                InputDetailsDialog(this, object : InputDetailsDialog.CommentDialogInteractions {
                    override fun onCommentSubmit(
                        name: String,
                        mobile: String,
                        email: String
                    ) {
                        viewModel.referContacts(
                            name,
                            mobile,
                            email,
                            preferenceStorage.userId,
                            programId
                        )
                    }
                }).apply {
                    show()
                }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if (intent.hasExtra(FROM_PUSH) && intent.getBooleanExtra(FROM_PUSH, false)) {
            startActivity(Intent(this, SelectDeveloperActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }.putExtra("From", "Offer"))
            finish()
        } else super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        offerToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = "Offer Details"
    }

    private fun observeVM() {
        viewModel.programHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    offers.clear()
                    offers.addAll(it.data.first)
                    programName = it.data.second.first
                    referalLink = it.data.third
                    programDesc = it.data.second.second
                    offersAdapter.programName = programName
                    offersAdapter.programDesc = programDesc
                    if (offers.isEmpty()) {
                        defaultTV.setVisible()
                    } else {
                        defaultTV.setGone()
                    }

                    loadingDialog.remove()
                    offersAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    offersAdapter.notifyDataSetChanged()


                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    offersAdapter.showLoading(false)
                    swipeRL.isRefreshing = false
                    scrollListener.setLoading(false)
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    if (it.isInitial && !it.isReload) loadingDialog.load()
                    else if (!it.isReload) offersAdapter.showLoading(true)
                }
            }
        })

        viewModel.referSuccessHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {

                    loadingDialog.dismiss()
                    commentDialog!!.dismiss()
                    ReferSuccessDialog(
                        this,
                        programName, it.data.first, it.data.second, it.data.third,
                        object : ReferSuccessDialog.OnSuccessDismissListener {
                            override fun onDismiss() {
                                finish()
                            }
                        }
                    )


                }
                is ViewState.Error -> {
                    loadingDialog.dismiss()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ReferContactListActivity.START_CODE && resultCode == ReferContactListActivity.REFFERAL_SUCCESS) {
            viewModel.getOffers(programId)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val PROGRAM_ID = "program_id"
        const val REFERAL_LINK = "referal_link"
        const val PROGRAM_NAME = "program_name"
        const val PROGRAM_DESC = "program_desc"


        fun start(
            context: Context,
            programId: String,
            referalLink: String,
            programName: String,
            programDesc: String
        ) {
            context.startActivity(Intent(context, OfferDetailsActivity::class.java).apply {
                putExtra(PROGRAM_ID, programId)
                putExtra(REFERAL_LINK, referalLink)
                putExtra(PROGRAM_NAME, programName)
                putExtra(PROGRAM_DESC, programDesc)
            })
        }
    }

    override fun onShareCallback() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
