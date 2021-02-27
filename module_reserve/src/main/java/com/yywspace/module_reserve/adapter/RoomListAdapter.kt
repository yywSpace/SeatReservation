package com.yywspace.module_reserve.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.area.City
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_reserve.R


class RoomListAdapter : BaseQuickAdapter<Room, BaseViewHolder>(R.layout.reserve_room_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Room) {
        holder.setText(R.id.room_name, item.roomName)
        holder.setText(R.id.room_location, item.roomLocation)
        // holder.setImageBitmap(R.id.room_image, item.roomImage)
        holder.setText(R.id.room_person_num, "${item.totalSeats - item.emptySeats}/${item.totalSeats}")

        Glide.with(context)
                .load(AppConfig.BASE_URL + "upload/" + item.roomImagePath)
                .placeholder(R.drawable.ic_bg)
                .error(R.drawable.ic_bg)
                .into(holder.getView(R.id.room_image))
    }
}