package com.yywspace.module_reserve.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.net.ServerUtils
import kotlin.random.Random

object SeatModel {
    fun getSeatList(id: Int): LiveData<BaseResponse<List<Seat>>> {
        return ServerUtils.getCommonApi().getSeatList(id)
    }

    fun getLocalSeatList(): List<Seat> {
        val list: MutableList<Seat> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(5, 9)) {
            list.add(Seat("seat$i", "正常", "天生我材必有用，千金散尽还复来。", Random.nextInt(2)))
        }
        return list
    }
}