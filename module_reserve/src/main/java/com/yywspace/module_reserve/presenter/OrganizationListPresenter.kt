package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_base.model.OrganizationModel

class OrganizationListPresenter : BasePresenter<IOrganizationListView>() {
    fun getOrganizationList(owner: LifecycleOwner, location: String, userId: Int) {
        OrganizationModel.getOrganizationListByLocation(userId, location).observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getOrganizationListResult(OrganizationModel.getLocalOrganizationList())
                return@Observer
            }
            val orgList = mutableListOf<Organization>()
            for (org in it.data) {
                orgList.add(
                        Organization(org.id, org.name, org.location, org.desc,
                                org.imagePath, org.totalSeats, org.emptySeats, org.isActivate, org.isFavourite))
            }
            view.getOrganizationListResult(it.data)
        })
    }

    fun makeOrganizationFavourite(owner: LifecycleOwner, organizationId: Int, userId: Int, favouriteStatus: Boolean) {
        OrganizationModel.makeOrganizationFavourite(organizationId, userId, favouriteStatus)
                .observe(owner, Observer {
                    view.makeOrganizationFavourite(it)
                })
    }

}
