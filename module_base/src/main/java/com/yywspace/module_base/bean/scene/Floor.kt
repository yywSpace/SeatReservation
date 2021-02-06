package com.yywspace.module_base.bean.scene

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Floor(
        var id: Int,
        var floorName: String,
        var totalSeats: Int,
        var emptySeats: Int):Parcelable