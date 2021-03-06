package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Setting
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.model.SettingModel
import com.yywspace.module_mine.iview.ISettingView
import com.yywspace.module_mine.iview.IStatisticView
import com.yywspace.module_mine.model.StatisticModel

class SettingPresenter : BasePresenter<ISettingView>() {
    fun getSetting(owner: LifecycleOwner, userName: String) {
        SettingModel.getSettingByUserName(userName).observe(owner, Observer {
            if (it == null || it.data == null) {
                val name = if (User.currentUser == null) "group" else User.currentUser!!.username
                view.getSettingResult(Setting(-1, 30, 30, name!!))
                return@Observer
            }
            view.getSettingResult(it.data)
        })
    }

    fun insertSetting(owner: LifecycleOwner, setting: Setting) {
        SettingModel.insertSetting(setting).observe(owner, Observer {
            view.insertSettingResult(it)
        })
    }
}