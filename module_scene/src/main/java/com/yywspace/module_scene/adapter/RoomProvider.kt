package com.yywspace.module_scene.adapter

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
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

class RoomProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 3
    override val layoutId: Int
        get() = R.layout.scene_tree_item_room

    override fun convert(@NotNull helper: BaseViewHolder, @NotNull item: BaseNode) {
        val room: Room = item as Room
        helper.setText(R.id.room_title, room.roomName)
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
        customView.findViewById<TextView>(R.id.scene_child_add).visibility = View.GONE
        dialog.show()
        return true
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {

    }

}