package com.loyalie.connectre.ui.profile


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loyalie.connectre.R
import kotlinx.android.synthetic.main.chooser_item.view.*
import kotlinx.android.synthetic.main.dialog_layout.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


const val PICK_IMG = 133
const val CAPTURE_IMG = 122
const val PATH = "secureimages/"

data class ChooserItem(
    val intent: Intent,
    val appName: String,
    val icon: Drawable,
    val isCapture: Boolean
)

class ChooserAdapter(
    val products: List<ChooserItem>,
    val context: Activity,
    val dialog: ChooserDialog
) : RecyclerView.Adapter<ChooserAdapter.ChooserVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooserVH {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.chooser_item, parent, false)

        return ChooserVH(v)
    }

    override fun getItemCount() = products.size


    override fun onBindViewHolder(holder: ChooserVH, position: Int) {
        holder.bind(products[position])
    }

    inner class ChooserVH(iv: View) : RecyclerView.ViewHolder(iv) {
        init {
            itemView.setOnClickListener {
                context.startActivityForResult(
                    products[adapterPosition].intent,
                    if (products[adapterPosition].isCapture) CAPTURE_IMG else PICK_IMG
                )
                dialog.dismiss()
            }
        }

        fun bind(product: ChooserItem) {
            itemView.name.text = product.appName
            itemView.img.setImageDrawable(product.icon)
        }
    }
}


class ChooserDialog(val c: Activity) : BottomSheetDialog(c) {
    init {
        setContentView(R.layout.dialog_layout)
        list.layoutManager = LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false)
        list.adapter = ChooserAdapter(ChooserUtils.getChooserData(c), c, this)
        show()
    }

}

class ChooserUtils() {

    companion object {

        private var lastPathProvidedToCameraClient = ""

        fun getChooserData(context: Context): List<ChooserItem> {
            val chooserList = ArrayList<ChooserItem>()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra("return-data", true)
            val tempFile = createImageFile(context)
            lastPathProvidedToCameraClient = tempFile.absolutePath
            takePhotoIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    tempFile
                )
            )
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.setType("image/*")
            setAppDatas(context, chooserList, takePhotoIntent, true)
            setAppDatas(context, chooserList, pickIntent, false)
            return chooserList
        }

        fun setAppDatas(
            context: Context,
            list: MutableList<ChooserItem>,
            intent: Intent,
            isCapture: Boolean
        ): List<ChooserItem> {
            val resInfo = context.packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resInfo) {
                val packageName = resolveInfo.activityInfo.packageName
                val targetedIntent = Intent(intent)
                val icon = context.packageManager.getApplicationIcon(packageName)
                val appName = context.packageManager.getApplicationLabel(
                    context.packageManager.getApplicationInfo(
                        packageName,
                        0
                    )
                ).toString()
                targetedIntent.`package` = packageName
                list.add(ChooserItem(targetedIntent, appName, icon, isCapture))
            }
            return list
        }

        fun createImageFile(context: Context): File {
            val internalStorage = context.getFilesDir()
            val mStorage = File(internalStorage, PATH)

            if (mStorage.isDirectory()) {
                for (f in mStorage.listFiles()) {
                    f.delete()
                }
            }

            if (!mStorage.exists()) mStorage.mkdirs()
            val fileName = UUID.randomUUID().toString() + ".jpg"
            return File(mStorage, fileName)
        }

        fun getCapturedImagePathWithRotation(context: Context): Pair<String, Int> {
            return Pair(
                lastPathProvidedToCameraClient, getRotationFromCamera(
                    context,
                    Uri.fromFile(File(lastPathProvidedToCameraClient))
                )
            )
        }


        fun getPickedImageWithPath(data: Intent, context: Context): Pair<String, Int> {
//            Log.d("rtevRot", getRotationFromGallery(context, data.data).toString())

            val image = MediaStore.Images.Media.getBitmap(context.contentResolver, data.data)
            if (image != null) {
                return Pair<String, Int>(
                    writeBitmapToFile(image, context).absolutePath,
                    getRotationFromGallery(context, data.data!!)
                )
            }
            return Pair("", 0)
        }


        private fun writeBitmapToFile(bitmap: Bitmap, context: Context): File {

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
            val bitmapdata = bos.toByteArray()
            val file = createImageFile(context)
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return file
        }

        private fun getRotationFromGallery(context: Context, imageUri: Uri): Int {
            var result = 0
            val columns = arrayOf(MediaStore.Images.Media.ORIENTATION)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(imageUri, columns, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val orientationColumnIndex = cursor.getColumnIndex(columns[0])
                    result = cursor.getInt(orientationColumnIndex)
                }
            } catch (e: Exception) {
                //Do nothing
            } finally {
                cursor?.close()
            }//End of try-catch block
            return result
        }

        private fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
            var rotate = 0
            try {

                context.contentResolver.notifyChange(imageFile, null)
                val exif = ExifInterface(imageFile.path!!)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )

                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return rotate
        }
    }

}



