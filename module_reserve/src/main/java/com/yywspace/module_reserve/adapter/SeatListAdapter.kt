package com.yywspace.module_reserve.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_reserve.R


class SeatListAdapter : BaseQuickAdapter<Seat, BaseViewHolder>(R.layout.reserve_room_seat_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Seat) {
        when (item.seatStatus) {
            0 -> {
                holder.setText(R.id.seat_status, "空闲")
                holder.setTextColor(R.id.seat_status, Color.BLUE)
                holder.setImageResource(R.id.seat_img, R.drawable.ic_seat_blue)
            }
            1 -> {
                holder.setText(R.id.seat_status, "繁忙")
                holder.setText(R.id.seat_message, item.seatMsg)
                holder.setTextColor(R.id.seat_status, Color.RED)
                holder.setImageResource(R.id.seat_img, R.drawable.ic_seat_red)
            }
            2 -> { // 不可用

            }
        }
        holder.setText(R.id.seat_name, item.seatName)
    }
}