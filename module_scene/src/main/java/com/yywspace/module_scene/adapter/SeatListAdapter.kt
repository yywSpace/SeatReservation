package com.yywspace.module_scene.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_scene.R


class SeatListAdapter : BaseQuickAdapter<Seat, BaseViewHolder>(R.layout.scene_tree_item_seat) {

    override fun convert(holder: BaseViewHolder, item: Seat) {
        holder.setText(R.id.seat_title, item.seatName)
        when (item.seatStatus) {
            0 -> {
                holder.setImageResource(R.id.ic_seat_status_icon, R.drawable.ic_seat_status_empty)
            }
            1 -> {
                holder.setImageResource(R.id.ic_seat_status_icon, R.drawable.ic_seat_status_busy)
            }
            2 -> { // 不可用
                holder.setImageResource(R.id.ic_seat_status_icon, R.drawable.ic_seat_status_forbidden)
            }
        }
    }
}