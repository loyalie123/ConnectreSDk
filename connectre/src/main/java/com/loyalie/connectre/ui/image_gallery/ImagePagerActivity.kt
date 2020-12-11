package com.loyalie.connectre.ui.image_gallery

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.ui.profile.ChooserUtils.Companion.createImageFile
import com.loyalie.connectre.util.AppExecutors
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_pager.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

class ImagePagerActivity : BaseActivity() {
    @Inject
    lateinit var picasso: Picasso
    var files = ArrayList<Uri>()
    var galList: ArrayList<GalleryItem>? = null
    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pager)
        window.setStatusBarColor(Color.BLACK)
        intent?.getSerializableExtra(IMAGES)?.let {
            galList = it as ArrayList<GalleryItem>
            val curPos = intent.getIntExtra(CURRENT_POS, 0)
            galList?.let {
                initViewPager(it, curPos)
                titleTV.text = (it[curPos].tagLine)
            }
            shareIV.setOnClickListener {
                try {
                    loadingDialog!!.show()
                    AppExecutors.getInstance().diskIo.execute {

                        val url = URL(galList!![curPos].url)
                        val image =
                            BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        val fileShared = writeBitmapToFile(image, applicationContext)
                        files.add(
                            FileProvider.getUriForFile(
                                ConnectReApp.instance,
                                "com.example.mylibrary.fileprovider",
                                fileShared
                            )
                        )


                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                        /*      if (files.size == 0) {
                                  shareIntent.type = "text/plain"
                              } else {*/
                        shareIntent.type = "image/*"
                        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
//                        }
//                            shareIntent.type = "image/*"
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, "")
//                            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        AppExecutors.getInstance().mainThread.execute {
                            loadingDialog!!.dismiss()
                            startActivity(Intent.createChooser(shareIntent, "Share Image Using"))
                        }
                    }
                } catch (e: Exception) {
                    "Unable to share image".toast(this)
                }
            }

            closeIV.setOnClickListener {
                finish()
            }

//            shareIV.setOnClickListener {
//                shareImage(galList?.get(galVP?.currentItem ?: curPos)?.url ?: "",
//                    galList?.get(galVP?.currentItem ?: curPos)?.tagLine ?: "")
//            }

            galVP.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {

                }
                return@setOnTouchListener false
            }
        }


    }

    private fun writeBitmapToFile(bitmap: Bitmap, context: Context): File {

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        val file = createImageFile(context)
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        return file
    }


    private fun initViewPager(images: List<GalleryItem>, pos: Int) {
        if (images.isEmpty()) {
            finish()
            return
        }
//        else if (images?.size == 1){
//            pagerIndicator.visibility = View.GONE
//        }
        val projPager =
            ImageGalleryAdapter(this, images, object : ImageGalleryAdapter.OnTapListener {
                override fun onTapped(position: Int) {

                    ImagePagerActivity.start(
                        this@ImagePagerActivity, galList as ArrayList<GalleryItem>,
                        position
                    )


                }
            }, picasso)
        galVP.adapter = projPager
//        pagerIndicator.attachToViewPager(imgVp)
        galVP.currentItem = pos

        galVP.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                galList?.let {
                    if (position < it.size) {
                        titleTV.text = (it[position].tagLine)
                    }
                }
            }

            override fun onPageSelected(position: Int) {

            }

        })
    }


    companion object {
        const val IMAGES = "images"
        const val CURRENT_POS = "current_pos"
        fun start(context: Context, images: ArrayList<GalleryItem>, crrentPage: Int) {
            context.startActivity(Intent(context, ImagePagerActivity::class.java).apply {
                putExtra(IMAGES, images)
                putExtra(CURRENT_POS, crrentPage)
            })

        }
    }


}
