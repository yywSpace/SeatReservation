package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.User
import com.yywspace.module_mine.user.statistic.StatisticLineData
import com.yywspace.module_mine.user.statistic.StatisticOverview
import com.yywspace.module_mine.user.statistic.StatisticPieData

interface IStatisticView : BaseViewImp {
    fun getStatisticLineDataListResult(lineDataList: List<StatisticLineData>?)

    fun getStatisticPieDataListResult(pieDataList: List<StatisticPieData>?)

    fun getStatisticOverviewResult(statisticOverview: StatisticOverview?)
}