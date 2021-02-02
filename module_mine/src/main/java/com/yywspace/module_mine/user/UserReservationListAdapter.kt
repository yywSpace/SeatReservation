package com.yywspace.module_mine.user

import android.graphics.Color
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_mine.R

class UserReservationListAdapter : BaseQuickAdapter<Reservation, BaseViewHolder>(R.layout.mine_reservation_list_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Reservation) {
        holder.setVisible(R.id.tvTopLine, holder.adapterPosition != 0)
        when (item.status) {
            0 -> {
                holder.setBackgroundResource(R.id.tvDot,R.drawable.ic_run)
            }
            1 -> {
                holder.setBackgroundResource(R.id.tvDot,R.drawable.ic_success)
            }
            2 -> {
                holder.setBackgroundResource(R.id.tvDot,R.drawable.ic_fail)
            }
        }
    }
}