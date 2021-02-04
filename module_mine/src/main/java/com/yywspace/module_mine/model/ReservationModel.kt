package com.yywspace.module_mine.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.scene.Room
import kotlin.random.Random

object ReservationModel {
    fun getReservationList(): LiveData<List<Reservation>?> {
        val liveData = MutableLiveData<List<Reservation>?>()
        val list = mutableListOf<Reservation>()
        for (i in 0 until Random.Default.nextInt(5, 9)) {
            list.add(Reservation((i * 10000).toLong(), (i * 20000).toLong(), "location$i", "seat$i", Random.Default.nextInt(0, 3)))
        }
        liveData.value = list
        return liveData
    }
}