package com.yywspace.module_base.bean

import android.os.Parcelable
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Organization(
        var id: Int = -1,
        var name: String,
        var location: String,
        var desc: String,
        var imagePath: String,
        var totalSeats: Int,
        var emptySeats: Int,
        var isActivate: Boolean = false,
        var isFavourite: Boolean = false,
        var group: String? = "group"
) : Parcelable, BaseExpandNode() {
    @IgnoredOnParcel
    override val childNode: MutableList<BaseNode> = mutableListOf()
}