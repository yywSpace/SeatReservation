package com.yywspace.module_scene.adapter

import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import org.jetbrains.annotations.NotNull


class NodeTreeAdapter(activity:AppCompatActivity) : BaseNodeAdapter() {
    companion object {
        const val EXPAND_COLLAPSE_PAYLOAD = 110
    }

    init {
        addNodeProvider(OrganizationProvider(activity))
        addNodeProvider(FloorProvider(activity))
        addNodeProvider(RoomProvider(activity))
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        val node: BaseNode = data[position]
        if (node is Organization) {
            return 1
        } else if (node is Floor) {
            return 2
        } else if (node is Room) {
            return 3
        }
        return -1
    }
}