package com.yywspace.module_mine.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.User
import com.yywspace.module_mine.user.statistic.StatisticLineData
import com.yywspace.module_mine.user.statistic.StatisticOverview
import com.yywspace.module_mine.user.statistic.StatisticPieData
import kotlin.random.Random

object StatisticModel {
    fun getStatisticLineDataList(): LiveData<List<StatisticLineData>?> {
        val liveData = MutableLiveData<List<StatisticLineData>?>()
        val list = mutableListOf<StatisticLineData>()
        for (i in 0 until 100) {
            list.add(StatisticLineData(i, Random.nextLong(100)))
        }
        liveData.value = list
        return liveData
    }

    fun getStatisticPieDataList(): LiveData<List<StatisticPieData>?> {
        val liveData = MutableLiveData<List<StatisticPieData>?>()
        val list = mutableListOf<StatisticPieData>()
        for (i in 0 until 10) {
            list.add(StatisticPieData("Org$i", 10, .1f, ""))
        }
        liveData.value = list
        return liveData
    }

    fun getStatisticOverview(): LiveData<StatisticOverview?> {
        val liveData = MutableLiveData<StatisticOverview?>()
        liveData.value = StatisticOverview(10, 4, 10000, 3000)
        return liveData
    }
}