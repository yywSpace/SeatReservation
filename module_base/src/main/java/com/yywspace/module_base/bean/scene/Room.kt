package com.yywspace.module_base.bean.scene

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(
        var id: Int = -1,
        var roomName: String = "",
        var roomDesc: String = "",
        var roomLocation: String = "",
        var totalSeats: Int = 0,
        var emptySeats: Int = 0,
        var roomImage: Bitmap? = null
) : Parcelable
