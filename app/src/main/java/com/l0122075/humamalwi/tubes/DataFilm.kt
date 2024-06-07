package com.l0122075.humamalwi.tubes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataFilm(
    val id: String? = null,
    var imgfilm: String? = null,
    var Judul: String? = null,
    var Sinopsis: String? = null,
    var TahunRilis: Int? = null,
    var Sutradara: String? = null,
    var Durasi: Int? = null,
    var Genre: String? = null

): Parcelable