package com.yywspace.module_scene.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.model.FileModel
import com.yywspace.module_base.model.FloorModel
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.iview.IOrganizationProviderView

class OrganizationProviderPresenter : BasePresenter<IOrganizationProviderView>() {

    fun updateOrganization(owner: LifecycleOwner, organization: Organization) {
        OrganizationModel.updateOrganization(organization).observe(owner, Observer {
            view.updateOrganizationResult(it)
        })
    }

    fun deleteOrganization(owner: LifecycleOwner, organization: Organization) {
        OrganizationModel.deleteOrganization(organization.id).observe(owner, Observer {
            view.deleteOrganizationResult(it, organization)
        })
    }

    fun insertFloor(owner: LifecycleOwner, floor: Floor, organization: Organization) {
        FloorModel.insertFloor(floor).observe(owner, Observer {
            view.insertFloorResult(it, floor, organization)
        })
    }

    fun uploadFile(owner: LifecycleOwner, path: String, organization: Organization) {
        FileModel.uploadFile(path).observe(owner, Observer {
            view.uploadFileResult(it, organization)
        })
    }
}