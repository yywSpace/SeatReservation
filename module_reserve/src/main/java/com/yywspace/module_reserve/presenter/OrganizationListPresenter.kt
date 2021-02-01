package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.model.OrganizationModel

class OrganizationListPresenter : BasePresenter<IOrganizationListView>() {
    fun getOrganizationList(owner: LifecycleOwner) {
        OrganizationModel.getOrganizationList().observe(owner, Observer {
            view.getOrganizationListResult(it)
        })
    }
}
