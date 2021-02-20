package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_reserve.iview.IRoomListView
import com.yywspace.module_base.model.RoomModel

class RoomListPresenter : BasePresenter<IRoomListView>() {
    fun getRoomList(owner: LifecycleOwner,id:Int) {
        RoomModel.getRoomList(id).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getRoomListResult(RoomModel.getLocalRoomList())
                return@Observer
            }
            view.getRoomListResult(it.data)
        })
    }
}
