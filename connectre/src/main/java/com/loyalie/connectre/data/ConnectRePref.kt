package com.loyalie.connectre.data

import android.content.Context
import android.preference.PreferenceManager
import com.loyalie.connectre.util.ConnectReApp

object ConnectRePref {

    val EXTRACTED_COLOR = "extracted_color"

    fun setExtractedColor(color: String) = putString(
        ConnectReApp.instance,
        EXTRACTED_COLOR,
        color
    )

    fun getExtractedColor() = getString(
        ConnectReApp.instance,
        EXTRACTED_COLOR,
        "#1ad4a0"
    )

    fun getString(mContext: Context?, key: String, _default: String): String? {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        return preferences.getString(key, _default)
    }

    fun putString(mContext: Context?, key: String, value: String) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        val edit = preferences.edit()
        edit.putString(key, value)
        edit.apply()

    }

    fun putInt(mContext: Context?, key: String, value: Int) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        val edit = preferences.edit()
        edit.putInt(key, value)
        edit.apply()
    }

    fun getInt(mContext: Context?, key: String, _default: Int): Int {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        return preferences.getInt(key, _default)
    }

    fun putBoolean(mContext: Context?, key: String, value: Boolean?) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        val edit = preferences.edit()
        edit.putBoolean(key, value!!)
        //edit.clear();
        edit.apply()
    }

    fun putFloat(mContext: Context?, key: String, value: Float) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        val edit = preferences.edit()
        edit.putFloat(key, value)
        edit.apply()
    }

    fun getBoolean(mContext: Context?, key: String, _default: Boolean?): Boolean? {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        return preferences.getBoolean(key, _default!!)
    }

    fun getFloat(mContext: Context?, key: String, _default: Float): Float {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(mContext)
        return preferences.getFloat(key, _default)
    }

}