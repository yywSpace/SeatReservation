package com.yywspace.module_reserve.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yywspace.module_base.base.BasePresenter
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.iview.IFloorListView
import com.yywspace.module_base.model.FloorModel

class FloorListPresenter : BasePresenter<IFloorListView>() {
    fun getFloorList(owner: LifecycleOwner, id: Int) {
        FloorModel.getFloorList(id).observe(owner, Observer {
            LogUtils.d(it.toString())
            if (it == null || it.data == null) {
                view.getFloorListResult(FloorModel.getLocalFloorList())
                return@Observer
            }
            view.getFloorListResult(it.data)
        })
    }
}
