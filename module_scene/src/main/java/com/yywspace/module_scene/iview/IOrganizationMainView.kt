package com.yywspace.module_scene.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat

interface IOrganizationMainView : BaseViewImp {
    fun getOrganizationListResult(organizationList: List<Organization>?)

    fun getFloorListResult(floorList: List<Floor>?, organization: Organization)

    fun getRoomListResult(roomList: List<Room>?, floor: Floor)

    fun updateOrganizationResult(response: BaseResponse<Any>)

    fun insertOrganizationResult(response: BaseResponse<Int>, organization: Organization)
}