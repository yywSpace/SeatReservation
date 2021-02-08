package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.User
import com.yywspace.module_mine.iview.IUserInfoDetailView
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.model.UserInfoModel

class UserInfoDetailPresenter : BasePresenter<IUserInfoDetailView>() {
    fun updateUserInfo(owner: LifecycleOwner, user: User?) {
        UserInfoModel.updateUserInfo(user).observe(owner, Observer {
            view.updateUserInfoResult(it)
        })
    }
}