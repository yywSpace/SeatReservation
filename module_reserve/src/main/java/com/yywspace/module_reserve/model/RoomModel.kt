package com.yywspace.module_reserve.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.net.ServerUtils
import kotlin.random.Random

object RoomModel {
    fun getRoomList(id: Int): LiveData<BaseResponse<List<Room>>> {
        return ServerUtils.getCommonApi().getRoomList(id)
    }

    fun getLocalRoomList(): List<Room> {
        val list: MutableList<Room> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(1, 9)) {
            list.add(Room(
                    -1,
                    "name$i",
                    "只见入门便是曲折游廊，阶下石子漫成甬路。上面小小两三房舍，一明两暗，里面都是合着地步打就的床几椅案。从里间房内又得一小门，出去则是后院，有大株梨花兼着芭蕉。又有两间小小退步。后院墙下忽开一隙，清泉一派，开沟仅尺许，灌入墙内，绕阶缘屋至前院，盘旋竹下而出。",
                    "b10", 10, 1))
        }
        return list
    }
}