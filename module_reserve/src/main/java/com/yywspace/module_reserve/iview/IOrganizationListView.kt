package com.yywspace.module_reserve.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization

interface IOrganizationListView : BaseViewImp {
    fun getOrganizationListResult(organizationList: List<Organization>?)

    fun makeOrganizationFavourite(response: BaseResponse<Any>)
}
