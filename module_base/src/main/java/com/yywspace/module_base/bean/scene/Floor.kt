package com.yywspace.module_base.bean.scene

import android.os.Parcelable
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Floor(
        var id: Int,
        var organizationId: Int,
        var floorName: String,
        var totalSeats: Int = 0,
        var emptySeats: Int = 0) : Parcelable, BaseExpandNode() {
    override val childNode: @RawValue MutableList<BaseNode> = mutableListOf()
}
