package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.statistic.StatisticReservation
import com.yywspace.module_base.bean.statistic.StatisticOverview
import com.yywspace.module_base.bean.statistic.StatisticOrganization

interface IStatisticView : BaseViewImp {
    fun getStatisticLineDataListResult(reservationInfoList: List<StatisticReservation>?)

    fun getStatisticPieDataListResult(pieDataList: List<StatisticOrganization>?)

    fun getStatisticOverviewResult(statisticOverview: StatisticOverview?)
}