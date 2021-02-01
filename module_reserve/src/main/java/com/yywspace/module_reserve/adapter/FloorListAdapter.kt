package com.yywspace.module_reserve.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.area.City
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.R


class FloorListAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.reserve_organization_floor_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: String) {
        LogUtils.d("楼层$item")
        holder.setText(R.id.organization_floor, "楼层$item")
        holder.setText(R.id.reserve_person_num, "9/10")
    }
}