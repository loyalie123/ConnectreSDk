package com.loyalie.connectre.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.loyalie.connectre.R
import com.loyalie.connectre.util.*
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_crop_image.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CropImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)

        initStatusColor()

        intent.getStringExtra(IMAGE_PATH).let { path ->

            cropImageView.setImageUriAsync(Uri.fromFile(File(path)))
            cropImageView.setOnSetImageUriCompleteListener(object :
                CropImageView.OnSetImageUriCompleteListener {
                override fun onSetImageUriComplete(
                    view: CropImageView?,
                    uri: Uri?,
                    error: java.lang.Exception?
                ) {
                    uri?.let {
                        cropImageView.rotatedDegrees = intent.getIntExtra(ROTATED_ANGLE, 0)
                    }
                }
            })


            okBtn.setOnClickListener {

                cropImageView.setOnCropImageCompleteListener { v, result ->
                    try {
                        AppExecutors.getInstance().diskIo.execute {
                            val intent = Intent()
                            intent.putExtra(FINAL_IMAGE_PATH, saveBitmapTofile(result.bitmap))
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }

                cropImageView.getCroppedImageAsync()

            }

            closeBtn.setOnClickListener {
                finish()
            }

            closeBtn.setBackgroundColor(ConnectReApp.themeColor)
            okBtn.setBackgroundColor(ConnectReApp.themeColor)

        }
    }

    private fun saveBitmapTofile(bitmap: Bitmap): String {
        val internalStorage = getFilesDir()
        val mStorage = File(internalStorage, PATH)

        if (!mStorage.exists()) {
            if (!mStorage.mkdirs()) {

            }
        }

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray()
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val file = File(mStorage, fileName)

        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return file.absolutePath
    }


    private fun initStatusColor() {
        window.setStatusBarColor(Utils.darkenColor(ConnectReApp.themeColor, 0.8f))
        DrawableCompat.setTint(closeIV.getDrawable(), ContextCompat.getColor(this, R.color.white));
    }
}
