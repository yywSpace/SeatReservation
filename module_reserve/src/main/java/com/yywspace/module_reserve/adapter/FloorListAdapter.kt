package com.yywspace.module_reserve.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.area.City
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.R


class FloorListAdapter : BaseQuickAdapter<Floor, BaseViewHolder>(R.layout.reserve_organization_floor_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Floor) {
        LogUtils.d("楼层$item")
        holder.setText(R.id.organization_floor, item.floorName)
        holder.setText(R.id.reserve_person_num, "${item.totalSeats - item.emptySeats}/${item.totalSeats}")
    }
}