package com.yywspace.module_mine.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.statistic.StatisticReservation
import com.yywspace.module_base.bean.statistic.StatisticOverview
import com.yywspace.module_base.bean.statistic.StatisticOrganization
import com.yywspace.module_base.net.ServerUtils
import kotlin.random.Random

object StatisticModel {
    fun getStatisticLineDataList(userId: Int): LiveData<BaseResponse<List<StatisticReservation>>> {
        return ServerUtils.getCommonApi().getStatisticReservation(userId)
    }

    fun getLocalStatisticLineDataList(): List<StatisticReservation> {
        val list = mutableListOf<StatisticReservation>()
        for (i in 0 until 100) {
            list.add(StatisticReservation(i.toLong(), Random.nextLong(100)))
        }
        return list
    }

    fun getStatisticPieDataList(userId: Int, limit: Int): LiveData<BaseResponse<List<StatisticOrganization>>> {
        return ServerUtils.getCommonApi().getStatisticOrganization(userId, limit)
    }


    fun getLocalStatisticPieDataList(): List<StatisticOrganization> {
        val list = mutableListOf<StatisticOrganization>()
        for (i in 0 until 10) {
            list.add(StatisticOrganization("Org$i", 10, .1f, ""))
        }
        return list
    }

    fun getLocalStatisticOverview(): StatisticOverview {
        return StatisticOverview(10, 4, 10000, 3000)
    }

    fun getStatisticOverview(userId: Int): LiveData<BaseResponse<StatisticOverview>> {
        return ServerUtils.getCommonApi().getStatisticOverview(userId)
    }
}