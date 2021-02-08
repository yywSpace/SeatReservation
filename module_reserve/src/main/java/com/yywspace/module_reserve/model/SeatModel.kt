package com.yywspace.module_reserve.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import kotlin.random.Random

object SeatModel {
    fun getSeatList(id: Int): LiveData<BaseResponse<List<Seat>>> {
        return ServerUtils.getCommonApi().getSeatList(id)
    }

    fun getLocalSeatList(): List<Seat> {
        val list: MutableList<Seat> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(5, 9)) {
            list.add(Seat(-1,"seat$i", "正常", "天生我材必有用，千金散尽还复来。", Random.nextInt(2)))
        }
        return list
    }

    fun insertReservation(reservation: Reservation): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(reservation)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().insertReservation(body)
    }
}