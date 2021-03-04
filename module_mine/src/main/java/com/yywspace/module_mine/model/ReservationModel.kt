package com.yywspace.module_mine.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.net.ServerUtils
import kotlin.random.Random

object ReservationModel {
    fun getReservationList(userId: Int): LiveData<BaseResponse<List<Reservation>>> {
        return ServerUtils.getCommonApi().getReservationList(userId)
    }

    fun getLocalReservationList(): List<Reservation> {
        val list = mutableListOf<Reservation>()
        for (i in 0 until Random.Default.nextInt(5, 9)) {
            list.add(Reservation(-1, -1, -1, "", (i * 10000).toLong(), (i * 20000).toLong(), "location$i", Random.Default.nextInt(0, 3), 1000))
        }
        return list
    }
}