package com.l0122075.humamalwi.tubes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
    val id: String? = null,
    val img: String? = null,
    var email: String? = null,
    var username: String? = null,
    var umur: Int? = null,
    var notelp: String? = null
): Parcelable

