package com.yywspace.module_scene.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Seat

interface ISeatListView : BaseViewImp {

    fun getSeatListResult(seatList: List<Seat>?)

    fun updateSeatResult(response: BaseResponse<Any>, seat: Seat)

    fun insertSeatResult(response: BaseResponse<Int>, seat: Seat)

    fun deleteSeatResult(response: BaseResponse<Any>, seat: Seat)
}