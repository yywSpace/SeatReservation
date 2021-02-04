package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_mine.iview.IStatisticView
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.model.StatisticModel
import com.yywspace.module_mine.model.UserInfoModel
import com.yywspace.module_mine.user.statistic.StatisticLineData
import com.yywspace.module_mine.user.statistic.StatisticPieData

class StatisticPresenter : BasePresenter<IStatisticView>() {

    fun getStatisticLineDataList(owner: LifecycleOwner) {
        StatisticModel.getStatisticLineDataList().observe(owner, Observer {
            view.getStatisticLineDataListResult(it)
        })
    }

    fun getStatisticPieDataList(owner: LifecycleOwner) {
        StatisticModel.getStatisticPieDataList().observe(owner, Observer {
            view.getStatisticPieDataListResult(it)
        })
    }

    fun getStatisticOverview(owner: LifecycleOwner) {
        StatisticModel.getStatisticOverview().observe(owner, Observer {
            view.getStatisticOverviewResult(it)
        })
    }
}