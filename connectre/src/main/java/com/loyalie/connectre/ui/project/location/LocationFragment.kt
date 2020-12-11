package com.loyalie.connectre.ui.project.location

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.loyalie.connectre.R
import com.loyalie.connectre.data.AdditionalHighlightItem
import com.loyalie.connectre.data.HighlightDetail
import com.loyalie.connectre.data.LocationFragItem
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.setGone
import com.loyalie.connectre.util.setVisible
import kotlinx.android.synthetic.main.fragment_location.*


/**
 * A simple [Fragment] subclass.
 *
 */
class LocationFragment : Fragment() {
    lateinit var locationPagerAdapter: LocationPagerAdapter

    private val handler = Handler()
    private var runnable: Runnable? = null
    private val hlts = ArrayList<HighlightDetail>()
    private val hltHash = HashMap<Int, List<HighlightDetail>>()
    private var hltAdapter: LocationVenueGridAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val location = arguments?.getSerializable(LOCATION_DATA) as LocationFragItem?
        if (location == null) return

        try {
            setUpVP(location)
            initRV(location.addnlHls)
            if (map == null) return
            map.getViewTreeObserver().addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (map == null) return
                    map.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    if (location.location.latitude.isNullOrBlank() || location.location.longitude.isNullOrBlank()) {
                        errorTV.setVisible()
                    } else {
                        initMap(
                            location.location.latitude,
                            location.location.longitude,
                            location.projectName
                        )
                    }


                }
            })

            if (location.location.googleLocation.isNullOrBlank() && location.location.locationAddress.isNullOrBlank()) {
                locationCV.setGone()
            } else {
                locationTV.setText(location.location.googleLocation)
                locationAddressTV.setText(location.location.locationAddress)
            }


        } catch (e: java.lang.Exception) {

        }


    }

    private fun initMap(lat: String, longitude: String, projectName: String) {
        try {

            val fm = childFragmentManager
            val supportMapFragment = MapFragmentObject()
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit()
            supportMapFragment.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(p0: GoogleMap?) {
                    runnable = Runnable {
                        setMapValues(p0, lat, longitude, projectName)
                    }
                    handler.postDelayed(runnable!!, 500)
                }
            })
        } catch (e: java.lang.Exception) {

        }
    }

    fun setMapValues(p0: GoogleMap?, lat: String, longitude: String, projectName: String) {
        try {
            val projectLocation = LatLng(lat.toDouble(), longitude.toDouble())
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.map_marker_small)

            p0?.addMarker(MarkerOptions().position(projectLocation).title(projectName).icon(icon))
            p0?.uiSettings?.isZoomGesturesEnabled = false
            p0?.moveCamera(CameraUpdateFactory.newLatLng(projectLocation))
            p0?.animateCamera(CameraUpdateFactory.zoomTo(14.0f))

            p0?.uiSettings?.isScrollGesturesEnabled = false
            p0?.uiSettings?.setAllGesturesEnabled(false)

            p0?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
            p0?.getUiSettings()?.setZoomControlsEnabled(true)

            (childFragmentManager.findFragmentById(R.id.map) as MapFragmentObject)
                .setListener(object : MapFragmentObject.OnTouchListener {
                    override fun onTouch() {
                        parentView.requestDisallowInterceptTouchEvent(false)
                    }
                })

        } catch (e: Exception) {
            // if (map != null) map.visibility = View.GONE
        }
    }

    override fun onPause() {
        runnable?.let { handler.removeCallbacks(it) }
        super.onPause()
    }

    private fun initRV(hlts: List<AdditionalHighlightItem>) {
        detailsRV.layoutManager = LinearLayoutManager(activity)
        detailsRV.adapter = LocationHeaderAdapter(hlts, requireContext())
    }

    private fun setUpVP(detail: LocationFragItem) {


        if (detail.highlights.isNotEmpty()) {
            hltHash.clear()
            detail.highlights.forEachIndexed { i, hlt ->
                if (!hlt.details.isNullOrEmpty()) {
                    val tab = locationTab.newTab()
                    locationTab.addTab(tab.setText(hlt.type))
                    hltHash.put(tab.position, hlt.details)
                }
            }

            if (hltHash.isEmpty()) tabBg.setGone()

            hlts.addAll(hltHash.get(0) ?: ArrayList())
            if ((hltHash.get(0) ?: ArrayList()).size < 2) {
                highltRV.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            } else {
                highltRV.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
            }
            hltAdapter = LocationVenueGridAdapter(hlts)
            highltRV.adapter = hltAdapter
        }

        locationTab.setTabTextColors(
            ContextCompat.getColor(requireContext(), R.color.textColorGrey),
            ConnectReApp.themeColor
        )

        locationTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0 == null) return
                hlts.clear()
                hlts.addAll(hltHash.get(p0.position) ?: ArrayList())
                hltAdapter?.notifyDataSetChanged()
                if (hltHash.get(p0.position)?.size ?: 0 < 2) {
                    highltRV.layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
                } else {
                    highltRV.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
                }
            }
        })


    }

    companion object {
        const val LOCATION_DATA = "LOCATION_DATA"
        fun newInstance(detail: LocationFragItem) = LocationFragment().apply {
            arguments = Bundle().apply { putSerializable(LOCATION_DATA, detail) }
        }

    }

}
