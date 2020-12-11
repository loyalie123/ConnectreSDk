package com.loyalie.connectre.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import com.loyalie.connectre.R
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var loginStatus: Boolean
    var userId: String
    var authToken: String
    var userName: String
    var userEmail: String
    var userPhone: String
    var userAvatar: String
    var dob: String
    var anniversary: String
    var app_status: Int
    var regDone: Int
    var userType: Int
    var verifyEmail: Int
    var tandC: String
    var userCCode:Int
    fun clearAll()
}
fun Context.saveApiToken(token: String) {
    val editor = getSharedPreferences(this.getString(R.string.user_data), Context.MODE_PRIVATE).edit()
    editor.putString("pref_auth_token", token)
    editor.apply()
}

fun Context.getApiToken(): String? {
    val sp = getSharedPreferences(this.getString(R.string.user_data), Context.MODE_PRIVATE)
    return sp.getString("pref_auth_token", "")

}
class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {

    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    override var loginStatus by BooleanPreference(prefs, PREF_LOGIN_STATUS, false)
    override var userId by StringPreference(prefs, PREF_USER_ID, "")
    override var authToken by StringPreference(prefs, PREF_AUTH_TOKEN, "")
    override var userName by StringPreference(prefs, PREF_USER_NAME, "")
    override var userEmail by StringPreference(prefs, PREF_USER_EMAIL, "")
    override var userPhone by StringPreference(prefs, PREF_USER_PHONE, "")
    override var userCCode by IntPreference(prefs, PREF_CCODE, 91)
    override var userAvatar by StringPreference(prefs, PREF_USER_IMAGE, "")
    override var dob: String by StringPreference(prefs, PREF_USER_DOB, "")
    override var anniversary: String by StringPreference(prefs, PREF_USER_ANNIVERSARY, "")
    override var app_status: Int by IntPreference(prefs, PREF_USER_STATUS, 0)
    override var regDone: Int
            by IntPreference(prefs, PREF_USER_TYPE, 0)
    override var userType: Int by IntPreference(prefs, PREF_USER_TYPES, 0)
    override var verifyEmail: Int by IntPreference(prefs, PREF_USER_VERIFY, 0)
    override var tandC: String by StringPreference(
        prefs,
        PREF_TANDC,
        "https://shapoorji.loyalie.in/terms_conditions_referral.html"
    )


    override fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val PREFS_NAME = "ivf_pref"
        const val PREF_LOGIN_STATUS = "pref_login_status"
        const val PREF_USER_ID = "pref_user_id"
        const val PREF_AUTH_TOKEN = "pref_auth_token"
        const val PREF_USER_NAME = "pref_user_name"
        const val PREF_USER_EMAIL = "pref_user_email"
        const val PREF_USER_PHONE = "pref_user_phone"
        const val PREF_USER_IMAGE = "pref_user_image"
        const val PREF_USER_DOB = "pref_user_dob"
        const val PREF_USER_ANNIVERSARY = "pref_user_anniversary"
        const val PREF_USER_STATUS = "pref_user_status"
        const val PREF_USER_TYPE = "pref_user_type"
        const val PREF_USER_TYPES = "pref_user_types"
        const val PREF_USER_VERIFY = "pref_email_verify"
        const val PREF_TANDC = "pref_tandc"
        const val PREF_CCODE="pref_ccode"
    }

}

class BooleanPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.edit().putBoolean(name, value).apply()
    }
}

class IntPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.edit().putInt(name, value).apply()
    }
}

class StringPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.getString(name, defaultValue) ?: ""
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        preferences.edit().putString(name, value).apply()
    }
}

interface UserIndependentPref {
    var fcmToken: String
}

class UserIndependantPreferenceStore @Inject constructor(context: Context) : UserIndependentPref {

    private val prefs =
        context.applicationContext.getSharedPreferences(USER_INDEPENDENT_PREF, Context.MODE_PRIVATE)

    override var fcmToken by StringPreference(prefs, FCM_TOKEN, "")

    companion object {
        val USER_INDEPENDENT_PREF = "user_independant_pref"
        val FCM_TOKEN = "fcm_token"
    }

}