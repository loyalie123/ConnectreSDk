package com.loyalie.connectre.ui.contact_us

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ContactUs
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.project.location.MapFragmentObject
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class ContactUsActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ContactUsVM

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    private var phoneNum: String? = null
    private var email: String? = null
    private var contactLoc: LatLng? = null
    private var googleMap: GoogleMap? = null

    private val handler = Handler()
    private var runnable: Runnable? = null
    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }
            R.id.contactMobRL -> {
                if (phoneNum.isNullOrBlank()) getString(R.string.no_contact_number_available).toast(
                    this
                )
                else dialPhone(phoneNum!!)
            }
            R.id.contactEmailRL -> {
                if (!email.isNullOrBlank()) sendEmail(email!!)
                else getString(R.string.no_contact_email_available).toast(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        initToolbar()
        observeVM()
        initClickListener()
        viewModel.getContactUs()
        initMap()
    }

    private fun initMap() {
        try {
            val fm = supportFragmentManager
            val supportMapFragment = MapFragmentObject()
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit()
            supportMapFragment.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(p0: GoogleMap?) {
                    runnable = Runnable {
                        googleMap = p0
                        setMapValues()
                    }
                    handler.postDelayed(runnable!!, 500)
                }
            })
        } catch (e: java.lang.Exception) {

        }


    }

    fun setMapValues() {
        try {
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.map_marker_small)
            contactLoc?.let {
                googleMap?.addMarker(MarkerOptions().position(it).title("Location").icon(icon))
                googleMap?.getUiSettings()?.setZoomGesturesEnabled(true)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(it))
                googleMap?.animateCamera(CameraUpdateFactory.zoomTo(14.0f))
            }

            googleMap?.uiSettings?.isScrollGesturesEnabled = true
            googleMap?.uiSettings?.setAllGesturesEnabled(true)

            googleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
            googleMap?.getUiSettings()?.setZoomControlsEnabled(true)

        } catch (e: Exception) {
            if (map != null) map.visibility = View.GONE
        }
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
        contactMobRL.setOnClickListener(this)
        contactEmailRL.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        contactToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = getString(R.string.contact_us)
        val bgShape = contactMobRL.getBackground() as GradientDrawable
        bgShape.setColor(extractedColor)
        contactMobRL.background = bgShape
        contactEmailRL.background = bgShape
    }

    private fun observeVM() {
        viewModel.contactUsHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    setUIValues(it.data.contactUs)
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

    private fun setUIValues(contactUs: ContactUs) {
        locationAddressTV.text = contactUs.contactDesc
        phoneNum = contactUs.contactPh
        email = contactUs.contactEmail
        if (contactUs.contactLat != null && contactUs.contactLong != null) {
            contactLoc = LatLng(contactUs.contactLat, contactUs.contactLong)
            setMapValues()
        }
    }
}
