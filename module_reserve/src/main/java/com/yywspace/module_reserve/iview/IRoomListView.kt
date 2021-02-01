package com.yywspace.module_reserve.iview

import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room

interface IRoomListView : BaseViewImp {
    fun getRoomListResult(roomList: List<Room>?)
}
