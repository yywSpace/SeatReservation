package com.yywspace.module_home.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.model.ReservationModel
import com.yywspace.module_base.model.SeatModel
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.iview.ISignInReservationStateView

class ReservationSignInStatePresenter : BasePresenter<ISignInReservationStateView>() {

    fun updateEndTime(owner: LifecycleOwner, reservation: Reservation) {
        reservation.endTime = System.currentTimeMillis()
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.updateEndTimeResult(it, reservation)
        })
    }

    fun reservationSignOut(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 1
        reservation.endTime = System.currentTimeMillis()
        reservation.statusTime = System.currentTimeMillis()
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.reservationSignOutResult(it, reservation)
        })
        SeatModel.changeSeatStatus(reservation.seatId, 0).observe(owner, Observer {
            if (it != null)
                LogUtils.d(it.toString())
            else
                LogUtils.d("it.toString()")
        })
    }

    fun reservationAFK(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 4
        reservation.statusTime = System.currentTimeMillis()
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.reservationAFKResult(it, reservation)
        })
    }

    fun reservationFailure(owner: LifecycleOwner, reservation: Reservation) {
        reservation.status = 2
        ReservationModel.updateReservation(reservation).observe(owner, Observer {
            view.reservationFailureResult(it, reservation)
        })
    }

}
