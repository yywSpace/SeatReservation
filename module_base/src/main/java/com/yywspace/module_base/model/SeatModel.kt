package com.yywspace.module_base.model

import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import com.yywspace.module_base.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

object SeatModel {
    fun getSeatList(id: Int): LiveData<BaseResponse<List<Seat>>> {
        return ServerUtils.getCommonApi().getSeatList(id)
    }

    fun getLocalSeatList(): List<Seat> {
        val list: MutableList<Seat> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(5, 9)) {
            list.add(Seat(-1, -1, "seat$i", "正常", "天生我材必有用，千金散尽还复来。", Random.nextInt(2)))
        }
        return list
    }

    fun insertSeat(seat: Seat): LiveData<BaseResponse<Int>> {
        val body: RequestBody = JsonUtils.getGson().toJson(seat)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().insertSeat(body)
    }

    fun updateSeat(seat: Seat): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(seat)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().updateSeat(body)
    }

    fun changeSeatStatus(seatId: Int, status: Int): LiveData<BaseResponse<Any>> {
        return ServerUtils.getCommonApi().changeSeatStatus(seatId, status)
    }

    fun deleteSeat(seatId: Int): LiveData<BaseResponse<Any>> {
        return ServerUtils.getCommonApi().deleteSeat(seatId)
    }

    fun insertReservation(reservation: Reservation): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(reservation)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        LogUtils.d(reservation.toString())
        LogUtils.d(JsonUtils.getGson().toJson(reservation))
        return ServerUtils.getCommonApi().insertReservation(body)
    }
}