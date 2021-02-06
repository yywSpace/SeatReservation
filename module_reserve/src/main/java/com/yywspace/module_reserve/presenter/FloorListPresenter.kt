package com.yywspace.module_reserve.presenter

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.iview.IFloorListView
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.iview.IRoomListView
import com.yywspace.module_reserve.iview.ISeatListView
import com.yywspace.module_reserve.model.FloorModel
import com.yywspace.module_reserve.model.OrganizationModel
import com.yywspace.module_reserve.model.RoomModel
import com.yywspace.module_reserve.model.SeatModel

class FloorListPresenter : BasePresenter<IFloorListView>() {
    fun getFloorList(owner: LifecycleOwner, id: Int) {
        FloorModel.getFloorList(id).observe(owner, Observer {
            LogUtils.d(it.toString())
            if (it == null || it.data == null) {
                view.getFloorListResult(FloorModel.getLocalFloorList())
                return@Observer
            }
            view.getFloorListResult(it.data)
        })
    }
}
