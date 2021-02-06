package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.iview.IRoomListView
import com.yywspace.module_reserve.iview.ISeatListView
import com.yywspace.module_reserve.model.RoomModel
import com.yywspace.module_reserve.model.SeatModel

class SeatListPresenter : BasePresenter<ISeatListView>() {
    fun getSeatList(owner: LifecycleOwner, id: Int) {
        SeatModel.getSeatList(id).observe(owner, Observer {
            LogUtils.d(it.toString())
            if (it == null || it.data == null) {
                view.getSeatListResult(SeatModel.getLocalSeatList())
                return@Observer
            }
            view.getSeatListResult(it.data)
        })
    }
}
