package com.yywspace.module_base.model

import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

object ReservationModel {

    fun updateReservation(reservation: Reservation): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(reservation)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().updateReservation(body)
    }

    fun cancelReservation(reservationId: Int): LiveData<BaseResponse<Any>> {
        return ServerUtils.getCommonApi().cancelReservation(reservationId)
    }

    fun getRunningReservation(userId: Int): LiveData<BaseResponse<Reservation>> {
        return ServerUtils.getCommonApi().getRunningReservation(userId)
    }

    fun getLocalRunningReservation(userId: Int): Reservation {
        val reservation = Reservation(-1, userId, -1, "seatName", 1000, 1000, "河南大学", 1,1000);
        return reservation
    }

}