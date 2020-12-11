package com.loyalie.connectre.ui.project.location


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.data.HighlightDetail
import kotlinx.android.synthetic.main.fragment_location_venue.*


/**
 * A simple [Fragment] subclass.
 *
 */
class LocationVenueFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_venue, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val hlts = arguments?.getSerializable(HIGHLIGHTS) as List<HighlightDetail>?
        if (hlts == null) return
        initVenuGV(hlts)
    }

    private fun initVenuGV(hlt: List<HighlightDetail>) {

        venueGridRV.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        venueGridRV.adapter = LocationVenueGridAdapter(hlt)
    }

    companion object {
        const val HIGHLIGHTS = "HIGHLIGHTS"
        fun newInstance(hlt: ArrayList<HighlightDetail>) = LocationVenueFragment().apply {
            arguments = Bundle().apply { putSerializable(HIGHLIGHTS, hlt) }
        }

    }


}
