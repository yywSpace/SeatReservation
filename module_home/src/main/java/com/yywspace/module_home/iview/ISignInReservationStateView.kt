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

interface ISignInReservationStateView : BaseViewImp {

    fun updateEndTimeResult(response: BaseResponse<Any>, reservation: Reservation)

    fun reservationSignOutResult(response: BaseResponse<Any>, reservation: Reservation)

    fun reservationAFKResult(response: BaseResponse<Any>, reservation: Reservation)

    fun reservationFailureResult(response: BaseResponse<Any>, reservation: Reservation)
}
