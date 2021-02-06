package com.yywspace.module_base.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Organization(
        var id: Int = -1,
        var name: String,
        var location: String,
        var desc: String,
        var totalSeats: Int,
        var emptySeats: Int,
        var isFavourite: Boolean) : Parcelable