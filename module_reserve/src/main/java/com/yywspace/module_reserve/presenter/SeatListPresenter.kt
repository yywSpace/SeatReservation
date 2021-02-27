package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.iview.ISeatListView
import com.yywspace.module_base.model.SeatModel

class SeatListPresenter : BasePresenter<ISeatListView>() {
    fun getSeatList(owner: LifecycleOwner, id: Int) {
        SeatModel.getSeatList(id).observe(owner, Observer {
            LogUtils.d(it.toString())
            if (it == null || it.data == null) {
                view.getSeatListResult(SeatModel.getLocalSeatList())
                return@Observer
            }
            if (Reservation.runningReservation != null) {
                it.data.forEach { seat->
                    if(seat.id == Reservation.runningReservation!!.seatId) {
                        seat.seatStatus = 3 // running
                    }
                }
            }
            view.getSeatListResult(it.data)
        })
    }

    fun reserveSeat(reservation: Reservation, owner: LifecycleOwner) {
        SeatModel.insertReservation(reservation).observe(owner, Observer {
            Reservation.runningReservation = reservation
            view.reserveSeatResult(it)
        })
    }
}
