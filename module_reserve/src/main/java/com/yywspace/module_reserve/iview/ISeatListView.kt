package com.yywspace.module_reserve.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat

interface ISeatListView : BaseViewImp {
    fun getSeatListResult(seatList: List<Seat>?)
    fun reserveSeat(response: BaseResponse<Any>)
}
