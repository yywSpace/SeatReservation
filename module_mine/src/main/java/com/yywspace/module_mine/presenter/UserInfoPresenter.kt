package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.model.UserInfoModel

class UserInfoPresenter : BasePresenter<IUserInfoView>() {
    fun getUserInfo(owner: LifecycleOwner) {
        UserInfoModel.getUserInfo().observe(owner, Observer {
            view.getUserInfoResult(it)
        })
    }
}