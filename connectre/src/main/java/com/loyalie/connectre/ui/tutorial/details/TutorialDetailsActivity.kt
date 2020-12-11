package com.loyalie.connectre.ui.tutorial.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.data.TutorialItem
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.Utils
import com.loyalie.connectre.util.loadUrlWithPh
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tutorial_details.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject


class TutorialDetailsActivity : BaseActivity(), View.OnClickListener {
    lateinit var videoUrl: String

    @Inject
    lateinit var picasso: Picasso

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.backArrowIV -> {
                onBackPressed()
            }

            R.id.videoThumbnailIV -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + videoUrl)
                        )
                    )
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_details)

        initToolbar()
        initClickListener()
        initDatas(intent.getSerializableExtra("tutorial") as TutorialItem)
        val url = "https://img.youtube.com/vi/" + videoUrl + "/hqdefault.jpg"
        videoThumbnailIV.loadUrlWithPh(url, picasso)

    }

    private fun initDatas(tutorialItem: TutorialItem) {
        tutorialHeadingTV.setText(tutorialItem.appTutorialTitle)
        descriptionTV.setText(tutorialItem.appTutorialDesc)
        videoUrl = tutorialItem.appTutorialLink ?: ""
    }

    private fun initClickListener() {
        backArrowIV.setOnClickListener(this)
        videoThumbnailIV.setOnClickListener(this)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        tutorialDetailsToolbar.setBackgroundColor(extractedColor)
        window.statusBarColor = Utils.darkenColor(extractedColor, 0.8f)
        titleTV.text = getString(R.string.app_tutorials)
    }


}
