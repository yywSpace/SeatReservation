package com.yywspace.module_reserve.adapter

import android.content.Context
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.area.Province
import com.yywspace.module_reserve.R

class ProvinceListAdapter(context: Context) : BaseQuickAdapter<Province, BaseViewHolder>(R.layout.reserve_province_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Province) {
        if (item.isSelected) {
            holder.setTextColor(R.id.reserve_province, context.getColor(R.color.reserveLightBlue))
            holder.itemView.setBackgroundResource(R.drawable.reserve_provience_item_bg)
        } else {
            holder.setTextColor(R.id.reserve_province, Color.BLACK)
            holder.itemView.background = null
        }
        holder.setText(R.id.reserve_province, item.name)
    }
}