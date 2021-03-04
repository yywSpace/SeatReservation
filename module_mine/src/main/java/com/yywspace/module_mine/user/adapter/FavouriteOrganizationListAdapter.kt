package com.yywspace.module_mine.user.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_mine.R

class FavouriteOrganizationListAdapter : BaseQuickAdapter<Organization, BaseViewHolder>(R.layout.mine_favourite_organization_item) {
    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: Organization) {
        holder.setText(R.id.mine_favourite_org_name, item.name)
        holder.setText(R.id.mine_favourite_org_location, item.location)
        holder.setBackgroundResource(R.id.mine_favourite_org_collect,
                if (item.isFavourite) R.drawable.ic_collected else R.drawable.ic_collect)
        holder.setText(R.id.mine_favourite_org_person_num,
                "${item.totalSeats - item.emptySeats}/${item.totalSeats}")
        Glide.with(context)
                .load(AppConfig.BASE_URL + "upload/" + item.imagePath)
                .placeholder(R.drawable.ic_bg)
                .error(R.drawable.ic_bg)
                .into(holder.getView(R.id.mine_favourite_organization_image))
    }
}