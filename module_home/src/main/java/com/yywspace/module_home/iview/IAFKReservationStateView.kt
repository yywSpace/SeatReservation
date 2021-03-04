package com.yywspace.module_home.iview

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.model.ReservationModel

interface IAFKReservationStateView : BaseViewImp {

    fun backToSeatResult(response: BaseResponse<Any>, reservation: Reservation)
    fun releaseSeatResult(response: BaseResponse<Any>, reservation: Reservation)
//    fun updateStatusTimeResult(response: BaseResponse<Any>, reservation: Reservation)
    fun reservationFailureResult(response: BaseResponse<Any>, reservation: Reservation)
}
