package com.l0122075.humamalwi.tubes

import android.content.Context
import android.content.SharedPreferences

class preferences(context: Context) {
    private val TAG_STATUS = "status"
    private val TAG_EMAIL = "email"
    private val TAG_USER_ID = "user_id"
    private val TAG_APP = "app"

    private val pref: SharedPreferences = context.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE)

    var prefStatus: Boolean
        get() = pref.getBoolean(TAG_STATUS, false)
        set(value) = pref.edit().putBoolean(TAG_STATUS, value).apply()

    var prefEmail: String?
        get() = pref.getString(TAG_EMAIL, "")
        set(value) = pref.edit().putString(TAG_EMAIL, value).apply()

    var prefUserId: String?
        get() = pref.getString(TAG_USER_ID, "")
        set(value) = pref.edit().putString(TAG_USER_ID, value).apply()

    fun prefClear() {
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_EMAIL).apply()
        pref.edit().remove(TAG_USER_ID).apply()
    }
}
