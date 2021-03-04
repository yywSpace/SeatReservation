package com.yywspace.module_scene.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.model.RoomModel
import com.yywspace.module_base.model.SeatModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.iview.ISeatListView

class SeatListPresenter : BasePresenter<ISeatListView>() {

    fun getSeatList(owner: LifecycleOwner, floorId: Int) {
        SeatModel.getSeatList(floorId).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getSeatListResult(SeatModel.getLocalSeatList())
                return@Observer
            }
            view.getSeatListResult(it.data)
        })
    }

    fun updateSeat(owner: LifecycleOwner, seat: Seat) {
        SeatModel.updateSeat(seat).observe(owner, Observer {
            view.updateSeatResult(it, seat)
        })
    }

    fun insertSeat(owner: LifecycleOwner, seat: Seat) {
        SeatModel.insertSeat(seat).observe(owner, Observer {
            view.insertSeatResult(it, seat)
        })
    }

    fun deleteSeat(owner: LifecycleOwner, seat: Seat) {
        SeatModel.deleteSeat(seat.id).observe(owner, Observer {
            view.deleteSeatResult(it, seat)
        })
    }
}