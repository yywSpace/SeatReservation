package com.yywspace.module_reserve.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.net.ServerUtils
import kotlin.random.Random

object FloorModel {
    fun getFloorList(id: Int): LiveData<BaseResponse<List<Floor>>> {
        return ServerUtils.getCommonApi().getFloorList(id)
    }

    fun getLocalFloorList(): List<Floor> {
        val list: MutableList<Floor> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(1, 9)) {
            list.add(Floor(-1, "楼层$i", 10, 100))
        }
        return list
    }
}