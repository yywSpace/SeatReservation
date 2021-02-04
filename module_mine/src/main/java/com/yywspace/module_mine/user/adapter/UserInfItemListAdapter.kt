package com.yywspace.module_mine.user.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_mine.R
import com.yywspace.module_mine.user.UserInfoItem


class UserInfItemListAdapter : BaseQuickAdapter<UserInfoItem, BaseViewHolder>(R.layout.mine_user_info_recycler_view_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: UserInfoItem) {
        holder.setImageResource(R.id.mine_item_icon, item.iconResource)
        holder.setText(R.id.mine_item_name, item.name)
    }
}