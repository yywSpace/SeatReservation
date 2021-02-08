package com.yywspace.module_mine.user.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.util.TimeUtils
import com.yywspace.module_mine.R

class UserReservationListAdapter : BaseQuickAdapter<Reservation, BaseViewHolder>(R.layout.mine_reservation_list_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Reservation) {
        holder.setVisible(R.id.tvTopLine, holder.adapterPosition != 0)
        holder.setText(R.id.reservation_start_time, TimeUtils.longToString(item.startTime, TimeUtils.formatPattern))
        holder.setText(R.id.reservation_seat_text, item.seatName)
        holder.setText(R.id.reservation_time_duration,
                "${(item.endTime - item.startTime) / 60}:${(item.endTime - item.startTime) % 60}")
        holder.setText(R.id.reservation_location_text, item.location)
        when (item.status) {
            0 -> {
                holder.setText(R.id.reservation_status_label, "")
                holder.setTextColor(R.id.reservation_status_label, Color.BLUE)
                holder.setBackgroundResource(R.id.reservation_status_icon, R.drawable.ic_run)
            }
            1 -> {
                holder.setText(R.id.reservation_status_label, "")
                holder.setTextColor(R.id.reservation_status_label, Color.GREEN)
                holder.setBackgroundResource(R.id.reservation_status_icon, R.drawable.ic_success)
            }
            2 -> {
                holder.setText(R.id.reservation_status_label, "")
                holder.setTextColor(R.id.reservation_status_label, Color.RED)
                holder.setBackgroundResource(R.id.reservation_status_icon, R.drawable.ic_fail)
            }
        }
    }
}