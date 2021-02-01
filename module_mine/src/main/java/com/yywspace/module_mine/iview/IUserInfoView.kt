package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.bean.scene.Room

interface IUserInfoView : BaseViewImp {
    fun getUserInfoResult(user: User?)
}