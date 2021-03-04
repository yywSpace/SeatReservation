package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_mine.iview.IStatisticView
import com.yywspace.module_mine.model.StatisticModel

class StatisticPresenter : BasePresenter<IStatisticView>() {

    fun getStatisticLineDataList(owner: LifecycleOwner, userId: Int) {
        StatisticModel.getStatisticLineDataList(userId).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getStatisticLineDataListResult(StatisticModel.getLocalStatisticLineDataList())
                return@Observer
            }
            view.getStatisticLineDataListResult(it.data)
        })
    }

    fun getStatisticPieDataList(owner: LifecycleOwner, userId: Int, limit: Int) {
        StatisticModel.getStatisticPieDataList(userId, limit).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getStatisticPieDataListResult(StatisticModel.getLocalStatisticPieDataList())
                return@Observer
            }
            view.getStatisticPieDataListResult(it.data)
        })
    }

    fun getStatisticOverview(owner: LifecycleOwner, userId: Int) {
        StatisticModel.getStatisticOverview(userId).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getStatisticOverviewResult(StatisticModel.getLocalStatisticOverview())
                return@Observer
            }
            view.getStatisticOverviewResult(it.data)
        })
    }
}