package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.iview.IRoomListView
import com.yywspace.module_reserve.model.RoomModel

class RoomListPresenter : BasePresenter<IRoomListView>() {
    fun getRoomList(owner: LifecycleOwner) {
        RoomModel.getRoomList().observe(owner, Observer {
            view.getRoomListResult(it)
        })
    }
}
