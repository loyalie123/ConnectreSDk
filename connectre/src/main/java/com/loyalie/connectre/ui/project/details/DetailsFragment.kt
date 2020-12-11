package com.loyalie.connectre.ui.project.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseFragment
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.data.ProjectDetailFragItem
import com.loyalie.connectre.util.dpToPx
import com.loyalie.connectre.util.setGone
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailsFragment : BaseFragment() {
    private var devImageAdapter: DetailsImgPageAdapter? = null

    @Inject
    lateinit var picasso: Picasso

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val project = arguments?.getSerializable(DETAIL_ITEM) as ProjectDetailFragItem?
        if (project == null) return

        initVP(project.detailImageList)
        initRV(project.bulletPairs)
        titleTV.setText(project.projectTitle)
        descriptionTV.setText(project.projectDesc)
        registrationNoTV.setText("RERA registration no: " + project.reraNumber)
        webAddressTV.setText(project.websiteUrl)

    }

    private fun initVP(images: List<GalleryItem>) {
        devImageAdapter = DetailsImgPageAdapter(images, picasso, requireContext())
        detailsVP.adapter = devImageAdapter
        detailsVP.pageMargin = requireContext().dpToPx(20)
        detailsVP.setAnimationEnabled(true)
        detailsVP.setFadeEnabled(true)
        detailsVP.setFadeFactor(0.6f)
        if (images.isEmpty()) detailsVP.setGone()
    }

    private fun initRV(bulletPairs: ArrayList<Pair<String, String>>) {

        detailsRV.isNestedScrollingEnabled = false
        detailsRV.layoutManager = LinearLayoutManager(activity)
        detailsRV.adapter = AddressDetailsAdapter(bulletPairs)

    }

    companion object {
        const val DETAIL_ITEM = "detail"
        fun newInstance(detail: ProjectDetailFragItem) = DetailsFragment().apply {
            arguments = Bundle().apply { putSerializable(DETAIL_ITEM, detail) }
        }

    }
}
