package com.yywspace.module_scene.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Seat

interface IOrganizationProviderView : BaseViewImp {
    fun insertFloorResult(response: BaseResponse<Int>, floor: Floor, organization: Organization)

    fun updateOrganizationResult(response: BaseResponse<Any>)

    fun deleteOrganizationResult(response: BaseResponse<Any>, organization: Organization)

    fun uploadFileResult(response: BaseResponse<String>, organization: Organization)
}