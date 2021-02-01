package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView

abstract class IReservationState() {
    var reservationContext: ReservationContext? = null

    abstract fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context)
}