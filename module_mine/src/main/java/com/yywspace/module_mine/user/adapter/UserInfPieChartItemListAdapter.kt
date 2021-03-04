package com.yywspace.module_mine.user.adapter

import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_mine.R
import com.yywspace.module_base.bean.statistic.StatisticOrganization


class UserInfPieChartItemListAdapter : BaseQuickAdapter<StatisticOrganization, BaseViewHolder>(R.layout.mine_user_info_pie_chart_recycler_view_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: StatisticOrganization) {
        holder.setText(R.id.mine_org_name, item.orgName)
        holder.setText(R.id.mine_org_time_radio, String.format("%.2f%%",item.timeRatio * 100))
        holder.itemView.findViewById<ProgressBar>(R.id.mine_org_time_radio_progressbar)
                .progress = (item.timeRatio * 100).toInt()
        holder.setImageResource(R.id.mine_org_icon, R.drawable.ic_org)
    }
}