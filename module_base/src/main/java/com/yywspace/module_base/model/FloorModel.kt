package com.yywspace.module_base.model

import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

object FloorModel {
    fun getFloorList(id: Int): LiveData<BaseResponse<List<Floor>>> {
        return ServerUtils.getCommonApi().getFloorList(id)
    }

    fun insertFloor(floor: Floor): LiveData<BaseResponse<Int>> {
        val body: RequestBody = JsonUtils.getGson().toJson(floor)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().insertFloor(body)
    }

    fun updateFloor(floor: Floor): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(floor)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().updateFloor(body)
    }

    fun deleteFloor(floorId: Int): LiveData<BaseResponse<Any>> {
        return ServerUtils.getCommonApi().deleteFloor(floorId)
    }


    fun getLocalFloorList(): List<Floor> {
        val list: MutableList<Floor> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(1, 9)) {
            list.add(Floor(-1, -1, "楼层$i", 10, 100))
        }
        return list
    }
}