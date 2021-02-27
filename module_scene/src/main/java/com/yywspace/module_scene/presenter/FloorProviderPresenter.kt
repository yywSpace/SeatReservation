package com.yywspace.module_scene.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.model.FloorModel
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.model.RoomModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.iview.IFloorProviderView

class FloorProviderPresenter : BasePresenter<IFloorProviderView>() {
    fun updateFloor(owner: LifecycleOwner, floor: Floor) {
        FloorModel.updateFloor(floor).observe(owner, Observer {
            view.updateFloorResult(it)
        })
    }

    fun deleteFloor(owner: LifecycleOwner, floor: Floor) {
        FloorModel.deleteFloor(floor.id).observe(owner, Observer {
            view.deleteFloorResult(it)
        })
    }

    fun insertRoom(owner: LifecycleOwner, room: Room, floor: Floor) {
        RoomModel.insertRoom(room).observe(owner, Observer {
            view.insertRoomResult(it, room, floor)
        })
    }
}