package com.yywspace.module_scene.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat

interface IFloorProviderView : BaseViewImp {
    fun updateFloorResult(response: BaseResponse<Any>)

    fun deleteFloorResult(response: BaseResponse<Any>)

    fun insertRoomResult(response: BaseResponse<Int>, room: Room, floor: Floor)
}