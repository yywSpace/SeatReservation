package com.yywspace.module_scene.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat

interface IRoomProviderView : BaseViewImp {

    fun updateRoomResult(response: BaseResponse<Any>)

    fun deleteRoomResult(response: BaseResponse<Any>, room: Room)

    fun uploadFileResult(response: BaseResponse<String>, room: Room)
}