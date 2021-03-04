package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.bean.scene.Room

interface IFavouriteReservationListView : BaseViewImp {
    fun getFavouriteOrganizationListResult(organizationList: List<Organization>?)

    fun makeOrganizationFavourite(response: BaseResponse<Any>)

}