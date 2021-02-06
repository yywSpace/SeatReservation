package com.yywspace.module_reserve.iview

import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat

interface IFloorListView : BaseViewImp {
    fun getFloorListResult(floorList: List<Floor>?)
}
