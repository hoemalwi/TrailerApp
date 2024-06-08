package com.l0122075.humamalwi.tubes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DataUser(
    val id: String? = null,
    var email: String? = null
): Parcelable

