package com.yywspace.module_base.model

import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

object RoomModel {
    fun getRoomList(id: Int): LiveData<BaseResponse<List<Room>>> {
        return ServerUtils.getCommonApi().getRoomList(id)
    }

    fun insertRoom(room: Room): LiveData<BaseResponse<Int>> {
        val body: RequestBody = JsonUtils.getGson().toJson(room)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().insertRoom(body)
    }

    fun updateRoom(room: Room): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(room)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().updateRoom(body)
    }

    fun deleteRoom(roomId: Int): LiveData<BaseResponse<Any>> {
        return ServerUtils.getCommonApi().deleteRoom(roomId)
    }

    fun getLocalRoomList(): List<Room> {
        val list: MutableList<Room> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(1, 9)) {
            list.add(Room(
                    -1,
                    -1,
                    "Room$i",
                    "只见入门便是曲折游廊，阶下石子漫成甬路。上面小小两三房舍，一明两暗，里面都是合着地步打就的床几椅案。从里间房内又得一小门，出去则是后院，有大株梨花兼着芭蕉。又有两间小小退步。后院墙下忽开一隙，清泉一派，开沟仅尺许，灌入墙内，绕阶缘屋至前院，盘旋竹下而出。",
                    "b10", 10, 1))
        }
        return list
    }
}