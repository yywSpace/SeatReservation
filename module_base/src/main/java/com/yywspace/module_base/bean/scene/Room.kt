package com.yywspace.module_base.bean.scene

import android.graphics.Bitmap
import android.os.Parcelable
import com.chad.library.adapter.base.entity.node.BaseNode
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize data class Room (
        var id: Int = -1,
        var floorId: Int = -1,
        var roomName: String = "",
        var roomDesc: String = "",
        var roomLocation: String = "",
        var totalSeats: Int = 0,
        var emptySeats: Int = 0,
        var roomImagePath: String = "",
        override val childNode: @RawValue MutableList<BaseNode> = mutableListOf()
) : Parcelable, BaseNode()
