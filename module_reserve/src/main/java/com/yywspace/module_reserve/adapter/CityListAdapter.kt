package com.yywspace.module_reserve.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.area.City
import com.yywspace.module_reserve.R


class CityListAdapter : BaseQuickAdapter<City, BaseViewHolder>(R.layout.reserve_city_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: City) {
        holder.setText(R.id.reserve_city, item.name)
    }
}