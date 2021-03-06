package com.yywspace.module_mine.iview

import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.base.BaseViewImp
import com.yywspace.module_base.bean.Setting
import com.yywspace.module_base.bean.statistic.StatisticReservation
import com.yywspace.module_base.bean.statistic.StatisticOverview
import com.yywspace.module_base.bean.statistic.StatisticOrganization

interface ISettingView : BaseViewImp {
    fun getSettingResult(setting: Setting)

    fun insertSettingResult(response: BaseResponse<Any>)
}