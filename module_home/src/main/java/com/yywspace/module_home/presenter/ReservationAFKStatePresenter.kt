package com.yywspace.module_home.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.model.ReservationModel
import com.yywspace.module_base.model.SeatModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.iview.IAFKReservationStateView
import com.yywspace.module_home.iview.IReservationStateView

class ReservationAFKStatePresenter : BasePresenter<IAFKReservationStateView>() {
    fun releaseSeat(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 1
        reservation.endTime = System.currentTimeMillis()
        reservation.statusTime = System.currentTimeMillis()
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.releaseSeatResult(it, reservation)
        })
        SeatModel.changeSeatStatus(reservation.seatId, 0).observe(owner, Observer {
            LogUtils.d(it.toString())
        })
    }
    fun reservationFailure(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 2
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.reservationFailureResult(it, reservation)
        })
        SeatModel.changeSeatStatus(reservation.seatId, 0).observe(owner, Observer {
            LogUtils.d(it.toString())
        })
    }

//    fun updateStatusTime(owner: LifecycleOwner, reservation: Reservation) {
//        reservation.statusTime = System.currentTimeMillis()
//        ReservationModel.updateReservation(reservation).observe(owner, Observer {
//            view.updateStatusTimeResult(it, reservation)
//        })
//    }

    fun backToSeat(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 0
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.backToSeatResult(it, reservation)
        })
    }
}
