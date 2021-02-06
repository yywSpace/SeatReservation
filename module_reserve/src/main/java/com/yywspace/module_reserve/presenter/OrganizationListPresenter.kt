package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.model.OrganizationModel

class OrganizationListPresenter : BasePresenter<IOrganizationListView>() {
    fun getOrganizationList(owner: LifecycleOwner) {
        OrganizationModel.getOrganizationList().observe(owner, Observer {
            if (it == null || it.data == null) {
                view.getOrganizationListResult(OrganizationModel.getLocalOrganizationList())
                return@Observer
            }
            view.getOrganizationListResult(it.data)
        })
    }
}
