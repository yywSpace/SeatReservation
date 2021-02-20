package com.yywspace.module_scene.adapter

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_scene.R
import org.jetbrains.annotations.NotNull


class FloorProvider() : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.scene_tree_item_floor

    override fun convert(@NotNull helper: BaseViewHolder, @NotNull item: BaseNode) {
        val floor: Floor = item as Floor
        helper.setText(R.id.floor_title, floor.floorName)
    }

    override fun onLongClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int): Boolean {
        val dialog = MaterialDialog(view.context).apply {
            customView(R.layout.scene_tree_item_dialog)
        }
        val customView = dialog.getCustomView()
        customView.findViewById<TextView>(R.id.scene_org_edit).setOnClickListener {
            Toast.makeText(view.context, "scene_org_edit", Toast.LENGTH_SHORT).show()
            dialog.dismiss()

        }
        customView.findViewById<TextView>(R.id.scene_org_delete).setOnClickListener {
            val parent = getAdapter()?.findParentNode(data) ?: throw Exception("Parent location not found")
            getAdapter()?.nodeRemoveData(getAdapter()?.getItem(parent)!!, data)
            dialog.dismiss()

        }
        customView.findViewById<TextView>(R.id.scene_child_add).setOnClickListener {
            getAdapter()?.nodeAddData(data, Room(
                    -1,
                    "请修改房间信息",
                    "只见入门便是曲折游廊，阶下石子漫成甬路。上面小小两三房舍，一明两暗，里面都是合着地步打就的床几椅案。从里间房内又得一小门，出去则是后院，有大株梨花兼着芭蕉。又有两间小小退步。后院墙下忽开一隙，清泉一派，开沟仅尺许，灌入墙内，绕阶缘屋至前院，盘旋竹下而出。",
                    "b10", 10, 1))
            dialog.dismiss()
        }
        dialog.show()
        return true
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        val floor: Floor = data as Floor
        if (floor.isExpanded) {
            getAdapter()?.collapse(position)
        } else {
            getAdapter()?.expandAndCollapseOther(position)
        }
    }
}