package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.bean.scene.Room

interface IReservationListView : BaseViewImp {
    fun getReservationListResult(reservationList: List<Reservation>?)
}