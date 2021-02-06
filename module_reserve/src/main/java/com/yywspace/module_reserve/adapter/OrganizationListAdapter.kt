package com.yywspace.module_reserve.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_reserve.R



class OrganizationListAdapter : BaseQuickAdapter<Organization, BaseViewHolder>(R.layout.reserve_organization_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Organization) {
        holder.setText(R.id.org_name, item.name)
        holder.setText(R.id.reserve_location, item.location)
        holder.setBackgroundResource(R.id.reserve_collect,
                if (item.isFavourite) R.drawable.ic_collected else R.drawable.ic_collect)
        holder.setText(R.id.reserve_person_num,
                "${item.totalSeats - item.emptySeats}/${item.totalSeats}")
    }
}