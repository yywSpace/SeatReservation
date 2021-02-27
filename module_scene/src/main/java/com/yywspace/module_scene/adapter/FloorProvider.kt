package com.yywspace.module_scene.adapter

import android.text.InputType
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.input
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_scene.R
import com.yywspace.module_scene.iview.IFloorProviderView
import com.yywspace.module_scene.iview.IOrganizationProviderView
import com.yywspace.module_scene.presenter.FloorProviderPresenter
import com.yywspace.module_scene.presenter.OrganizationProviderPresenter
import org.jetbrains.annotations.NotNull


class FloorProvider(private val activity: AppCompatActivity) : BaseNodeProvider(), IFloorProviderView {
    private val presenter: FloorProviderPresenter = FloorProviderPresenter()

    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.scene_tree_item_floor

    init {
        presenter.attachView(this)
    }

    override fun convert(@NotNull helper: BaseViewHolder, @NotNull item: BaseNode) {
        val floor: Floor = item as Floor
        helper.setText(R.id.floor_title, floor.floorName)
    }

    override fun onLongClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int): Boolean {
        val floor = data as Floor
        val optionMenuDialog = MaterialDialog(view.context).apply {
            customView(R.layout.scene_tree_item_dialog)
        }
        val customView = optionMenuDialog.getCustomView()
        customView.findViewById<TextView>(R.id.scene_org_edit).setOnClickListener {
            MaterialDialog(context).show {
                title(text = "修改楼层名称")
                input(allowEmpty = false, inputType = InputType.TYPE_CLASS_TEXT, prefill = floor.floorName) { dialog, text ->
                    floor.floorName = text.toString()
                    presenter.updateFloor(activity, floor)
                    getAdapter()?.notifyItemChanged(position)
                    optionMenuDialog.dismiss()
                }
                positiveButton(R.string.base_dialog_confirm)
                negativeButton(R.string.base_dialog_cancel)
            }
            optionMenuDialog.dismiss()
        }

        customView.findViewById<TextView>(R.id.scene_org_delete).setOnClickListener {
            val parent = getAdapter()?.findParentNode(data)
                    ?: throw Exception("Parent location not found")
            presenter.deleteFloor(activity, floor)
            getAdapter()?.nodeRemoveData(getAdapter()?.getItem(parent)!!, data)
            optionMenuDialog.dismiss()
        }
        customView.findViewById<TextView>(R.id.scene_child_add).setOnClickListener {
            val room = Room(
                    -1,
                    floor.id,
                    "请修改房间信息",
                    "请修改房间描述",
                    "请修改房间地址", 0, 0)
            presenter.insertRoom(activity, room, floor)
            optionMenuDialog.dismiss()
        }

        customView.findViewById<TextView>(R.id.scene_child_multi_add).setOnClickListener {
            MaterialDialog(context).show {
                title(text = "添加数量")
                input(allowEmpty = false, inputType = InputType.TYPE_CLASS_NUMBER) { dialog, text ->
                    val num = text.toString().toInt()
                    for (i in 0 until num) {
                        getAdapter()?.nodeAddData(data, Room(
                                -1,
                                floor.id,
                                "请修改房间信息-$i",
                                "请修改房间描述-$i",
                                "a-$i", 0, 0))
                    }
                    optionMenuDialog.dismiss()
                }
                positiveButton(R.string.base_dialog_confirm)
                negativeButton(R.string.base_dialog_cancel)
            }

        }
        optionMenuDialog.show()
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


    override fun updateFloorResult(response: BaseResponse<Any>) {
        if (response.code == 1) {
            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun deleteFloorResult(response: BaseResponse<Any>) {
        if (response.code == 1) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun insertRoomResult(response: BaseResponse<Int>, room: Room, floor: Floor) {
        if (response.code == 1) {
            room.id = response.data
            getAdapter()?.nodeAddData(floor, room)
        } else {
            Toast.makeText(context, "插入失败", Toast.LENGTH_SHORT).show();
        }
    }

}