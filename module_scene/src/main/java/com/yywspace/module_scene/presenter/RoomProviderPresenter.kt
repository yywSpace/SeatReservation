package com.yywspace.module_scene.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.model.FileModel
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.model.RoomModel
import com.yywspace.module_base.model.SeatModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.iview.IRoomProviderView

class RoomProviderPresenter : BasePresenter<IRoomProviderView>() {

    fun updateRoom(owner: LifecycleOwner, room: Room) {
        RoomModel.updateRoom(room).observe(owner, Observer {
            view.updateRoomResult(it)
        })
    }

    fun deleteRoom(owner: LifecycleOwner, room: Room) {
        RoomModel.deleteRoom(room.id).observe(owner, Observer {
            view.deleteRoomResult(it, room)
        })
    }

    fun uploadFile(owner: LifecycleOwner, path: String, room: Room) {
        FileModel.uploadFile(path).observe(owner, Observer {
            view.uploadFileResult(it, room)
        })
    }

}