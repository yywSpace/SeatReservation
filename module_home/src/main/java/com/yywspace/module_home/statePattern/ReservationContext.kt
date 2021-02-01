package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView

object ReservationContext {
    val reservationState: IReservationState = ReservationState()
    val initialReservationState: IReservationState = InitialReservationState()
    val afkReservationState: IReservationState = AFKReservationState()
    val signInReservationState: IReservationState = SignInReservationState()

    var currentState: IReservationState = initialReservationState.also {
        it.reservationContext = this
    }
        set(value) {
            value.reservationContext = this
            field = value
        }


    fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView,context: Context) {
        currentState.handleView(binding, inflater, reservationView,context)
    }

}