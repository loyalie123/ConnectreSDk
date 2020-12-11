package com.loyalie.connectre.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.loyalie.connectre.R
import com.loyalie.connectre.custom_views.SafeClickListener
import com.loyalie.connectre.data.PreferenceStorage
import com.loyalie.connectre.ui.enter_phn.EnterPhoneNumberActivity
import com.loyalie.connectre.widget.Toasty
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}


fun EditText.getTrimText() = text.toString().trim()

fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(trim()).matches()
fun getPixelValue(context: Context, dimenId: Int): Int {
    val resources: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dimenId.toFloat(),
        resources.getDisplayMetrics()
    ).toInt()
}

fun Context.getUbunduBold(): Typeface? = ResourcesCompat.getFont(this, R.font.sinkinsans_700bold)

fun Context.getUbundu(): Typeface? = ResourcesCompat.getFont(this, R.font.sinkinsans_400regular)

fun Context.dialPhone(number: String) {
    if (number.isBlank()) return
    try {
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number))
        startActivity(dialIntent)
    } catch (e: java.lang.Exception) {

    }


}

fun Context.dialWatsapp(number: String) {
    if (number.isBlank()) return
    try {
//        var url = "http://wa.me/"+number;
        var sendIntent = Intent();
        sendIntent.setAction(Intent.ACTION_VIEW)
        var url = "https://api.whatsapp.com/send?phone=" + number;
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
//      var sendIntent =  Intent("android.intent.action.MAIN");
//                    sendIntent.setAction(Intent.ACTION_VIEW);
//                    sendIntent.setPackage("com.whatsapp");
//                    var url = "http://wa.me/send?phone=" +number;
//                    sendIntent.setData(Uri.parse(url));
//                    if(sendIntent.resolveActivity(packageManager) != null){
//                         startActivity(sendIntent);
//                    }
    } catch (e: java.lang.Exception) {

    }
}

fun Context.sendEmail(emailId: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailId, null))
    startActivity(Intent.createChooser(emailIntent, "Send email using"));
}

fun Context.logout(preferenceStorage: PreferenceStorage, isAuthFail: Boolean) {
    preferenceStorage.clearAll()
    EnterPhoneNumberActivity.start(this, isAuthFail, true, false)
}

fun View.showKeyboard() {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInputFromWindow(
        getApplicationWindowToken(),
        InputMethodManager.SHOW_FORCED, 0
    )
}

fun Context.openInBrowser(url: String) {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (e: Exception) {
        getString(R.string.error_in_fetching_doc).toast(this)
    }
}


fun TabLayout.changeTextFonts(tabPosition: Int, style: Typeface, textSize: Float) {
    val tabLayout = (this.getChildAt(0) as ViewGroup).getChildAt(tabPosition) as LinearLayout
    val tabTextView = tabLayout.getChildAt(1) as AppCompatTextView
    tabTextView.typeface = style
    tabTextView.textSize = textSize

}

fun Context.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    ).toInt()
}

/**
 * For Actvities, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(this, provider).get(VM::class.java)

fun Activity.hideKeyboard() {
    val view = findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


fun String.toast(context: Context) {
    Toasty(context).toast(this)
}


fun ImageView.loadUrl(url: String, picasso: Picasso) {
    picasso.load(url).fit().centerCrop().into(this)

}

fun ImageView.loadUrlWithPh(url: String?, picasso: Picasso) {
    val ph = context.getGreyPlaceHolder()
    if (url.isNullOrBlank()) setImageDrawable(ph)
    picasso.load(url)
        .fit()
        .centerCrop()
        .placeholder(ph)
        .error(ph)
        .into(this)
}

fun ImageView.loadUrlWithPhVendor(url: String?, picasso: Picasso) {
    val ph = context.setPH()
    if (url.isNullOrBlank()) setImageResource(R.drawable.default_vendor)
    picasso.load(url)
        .fit()
        .centerCrop()
        .placeholder(ph)
        .error(ph)
        .into(this)
}

fun Context.setPH() = R.drawable.default_vendor
fun ImageView.loadUrlCenterInside(url: String, picasso: Picasso) {
    picasso.load(url).fit().centerInside().into(this)
}

fun String.convertToMMMDDYYY(): String {
    if (isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    var date = Date()
    try {
        date = inputFormat.parse(this)
    } catch (e: ParseException) {

    }

    return outputFormat.format(date)
}

fun String.convertToyyyyMMdd(): String {
    if (isEmpty()) return ""
    val inputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date = Date()
    try {
        date = inputFormat.parse(this)
    } catch (e: ParseException) {

    }

    return outputFormat.format(date)
}

fun String.convertToddMMyyyy(): String {
    if (isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    var date = Date()
    try {
        date = inputFormat.parse(this)
    } catch (e: ParseException) {

    }

    return outputFormat.format(date)
}
fun String.convertToddMMyyy(): String {
    if (isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    var date = Date()
    try {
        date = inputFormat.parse(this)
    } catch (e: ParseException) {

    }

    return outputFormat.format(date)
}
fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")
fun String.convertToddthMMyyy(): String {
    if (isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    var date = Date()
    var d=""
    try {
        date = inputFormat.parse(this)

         d=outputFormat.format(date)
        var day:String=date.date.toString()

        d=d.replace(day,day+"th",false)
    } catch (e: ParseException) {

    }

    return d
}

fun String.gethhmmaFromApiDateString(): Date {
    try {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(this)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return Date()
    }
}

fun String.convertToHHMMA(): String {
    try {
        /*val sdf = SimpleDateFormat("DD MM YYYY HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(this)
        outputFormat.timeZone = TimeZone.getDefault()
        return outputFormat.format(date)*/
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(SimpleDateFormat("DD MM YYYY HH:mm:ss", Locale.getDefault()).parse(this))
            .toString()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return ""
    }
}


fun Date.getDateAsNumber(): Int {
    return Integer.parseInt(SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(time))
}

fun Date.getTimeFormatInhmma(): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(time).toUpperCase()
}

fun Date.getDateAsddMMyyyyathmma(): String {
    return SimpleDateFormat("dd/MM/yyyy 'at' h:mm a", Locale.getDefault()).format(time)
}

fun Date.convertToMMMDDYYY(): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(time)
}

fun String.convertToMMMMYYYY(): String {
    if (isEmpty()) return ""
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
    try {
        return outputFormat.format(inputFormat.parse(this))
    } catch (e: ParseException) {

    }

    return ""
}

fun Context.getGreyPlaceHolder() =
    ColorDrawable(ContextCompat.getColor(this, R.color.textColorGreyAlpha10))

fun Context.getAlertForregistration(context: Context, view: View) {
//then we will inflate the custom alert dialog xml that we created
    val viewGroup = view.findViewById(android.R.id.content) as ViewGroup
    val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert, viewGroup, false)
    val builder = AlertDialog.Builder(this)
    builder.setView(dialogView)
    val alertDialog = builder.create()
    alertDialog.show()
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}