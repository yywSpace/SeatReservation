package com.yywspace.module_home.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.model.ReservationModel
import com.yywspace.module_base.model.SeatModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.iview.IReservationStateView

class ReservationStatePresenter : BasePresenter<IReservationStateView>() {

    fun cancelReservation(owner: LifecycleOwner, reservation: Reservation) {
        ReservationModel.cancelReservation(reservation.id).observe(owner, Observer {
            view.cancelReservationResult(it, reservation)
        })
    }

//    fun updateStatusTime(owner: LifecycleOwner, reservation: Reservation) {
//        reservation.statusTime = System.currentTimeMillis()
//        ReservationModel.updateReservation(reservation).observe(owner, Observer {
//            view.updateStatusTimeResult(it, reservation)
//        })
//    }

    fun getRunningReservation(owner: LifecycleOwner, userId: Int) {
        ReservationModel.getRunningReservation(userId).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getRunningReservationResult(ReservationModel.getLocalRunningReservation(userId))
                return@Observer
            }
            view.getRunningReservationResult(it.data)
        })
    }

    fun reservationSignIn(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 3
        reservation.statusTime = System.currentTimeMillis()
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.reservationSignInResult(it, reservation)
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
}
