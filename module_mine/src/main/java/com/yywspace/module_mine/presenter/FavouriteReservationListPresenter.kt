package com.yywspace.module_mine.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_mine.iview.IFavouriteReservationListView
import com.yywspace.module_mine.iview.IReservationListView
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.model.FavouriteOrganizationModel
import com.yywspace.module_mine.model.ReservationModel
import com.yywspace.module_mine.model.UserInfoModel

class FavouriteReservationListPresenter : BasePresenter<IFavouriteReservationListView>() {
    fun getFavouriteReservationList(owner: LifecycleOwner) {
        FavouriteOrganizationModel.getFavouriteOrganizationList().observe(owner, Observer {
            view.getFavouriteOrganizationListResult(it)
        })
    }
}