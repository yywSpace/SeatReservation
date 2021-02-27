package com.yywspace.module_scene.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.model.FloorModel
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.model.RoomModel
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.iview.IOrganizationMainView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class OrganizationMainPresenter : BasePresenter<IOrganizationMainView>() {
    fun getFloorList(owner: LifecycleOwner, organization: Organization) {
        FloorModel.getFloorList(organization.id).observe(owner, Observer {
            LogUtils.d(it.toString())
            if (it == null || it.data == null) {
                view.getFloorListResult(FloorModel.getLocalFloorList(), organization)
                return@Observer
            }
            view.getFloorListResult(it.data, organization)
        })
    }

    fun updateOrganization(owner: LifecycleOwner, organization: Organization) {
        OrganizationModel.updateOrganization(organization).observe(owner, Observer {
            view.updateOrganizationResult(it)
        })
    }

    fun getOrganizationListByGroup(owner: LifecycleOwner, group: String) {
        // TODO: 21-2-20 根据group读取
        OrganizationModel.getOrganizationListByGroup(group).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getOrganizationListResult(OrganizationModel.getLocalOrganizationList())
                return@Observer
            }
            LogUtils.d("getOrganizationListResult")
            view.getOrganizationListResult(it.data)
        })
    }


    fun getRoomList(owner: LifecycleOwner, floor: Floor) {
        RoomModel.getRoomList(floor.id).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getRoomListResult(RoomModel.getLocalRoomList(), floor)
                return@Observer
            }
            view.getRoomListResult(it.data, floor)
        })
    }

    fun insertOrganization(owner: LifecycleOwner, organization: Organization) {
        OrganizationModel.insertOrganization(organization).observe(owner, Observer {
            view.insertOrganizationResult(it, organization)
        })
    }

}