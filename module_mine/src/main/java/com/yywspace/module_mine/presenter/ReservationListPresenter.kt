package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_mine.iview.IReservationListView
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.model.ReservationModel
import com.yywspace.module_mine.model.UserInfoModel

class ReservationListPresenter : BasePresenter<IReservationListView>() {
    fun getReservationList(owner: LifecycleOwner, userId: Int) {
        ReservationModel.getReservationList(userId).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getReservationListResult(ReservationModel.getLocalReservationList())
                return@Observer
            }
            view.getReservationListResult(it.data)
        })
    }
}