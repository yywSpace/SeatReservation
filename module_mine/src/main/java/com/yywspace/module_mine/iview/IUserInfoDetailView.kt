package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.bean.scene.Room

interface IUserInfoDetailView : BaseViewImp {
    fun updateUserInfoResult(response: BaseResponse<Any>)
    fun uploadFileResult(response: BaseResponse<Any>)
}