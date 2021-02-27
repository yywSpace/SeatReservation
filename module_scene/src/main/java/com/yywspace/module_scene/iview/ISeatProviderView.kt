package com.yywspace.module_scene.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Seat

interface ISeatProviderView : BaseViewImp {
    fun updateSeatResult(response: BaseResponse<Any>)

    fun deleteSeatResult(response: BaseResponse<Any>)

    fun insertSeatResult(response: BaseResponse<Any>)
}