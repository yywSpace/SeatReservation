package com.yywspace.module_scene.adapter

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.GlideEngine
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_scene.R
import com.yywspace.module_scene.databinding.SceneShapeRoomInfoBinding
import com.yywspace.module_scene.iview.IRoomProviderView
import com.yywspace.module_scene.presenter.RoomProviderPresenter
import org.jetbrains.annotations.NotNull

class RoomProvider(private val activity:AppCompatActivity) : BaseNodeProvider(), IRoomProviderView {
    private val presenter: RoomProviderPresenter = RoomProviderPresenter()

    override val itemViewType: Int
        get() = 3
    override val layoutId: Int
        get() = R.layout.scene_tree_item_room

    init {
        presenter.attachView(this)
    }

    override fun convert(@NotNull helper: BaseViewHolder, @NotNull item: BaseNode) {
        val room: Room = item as Room
        helper.setText(R.id.room_title, room.roomName)
    }

    override fun onLongClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int): Boolean {
        val room = data as Room
        val optionMenuDialog = MaterialDialog(view.context).apply {
            customView(R.layout.scene_tree_item_dialog)
        }
        val customView = optionMenuDialog.getCustomView()
        customView.findViewById<TextView>(R.id.scene_org_edit).setOnClickListener {
            showRoomEditDialog(data, position)
            optionMenuDialog.dismiss()
        }
        customView.findViewById<TextView>(R.id.scene_org_delete).setOnClickListener {
            val parent = getAdapter()?.findParentNode(data)
                    ?: throw Exception("Parent location not found")
            presenter.deleteRoom(activity, room)
            getAdapter()?.nodeRemoveData(getAdapter()?.getItem(parent)!!, data)
            optionMenuDialog.dismiss()
        }
        customView.findViewById<TextView>(R.id.scene_child_add).visibility = View.GONE
        customView.findViewById<TextView>(R.id.scene_child_multi_add).visibility = View.GONE
        optionMenuDialog.show()
        return true
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {

    }

    private fun showRoomEditDialog(data: BaseNode, position: Int) {
        val room = data as Room
        var imagePath: String? = null
        val dialog = MaterialDialog(context).apply {
            title(text = "房间信息")
            customView(R.layout.scene_shape_room_info)
        }
        val binding = SceneShapeRoomInfoBinding.bind(dialog.getCustomView())
        binding.shapeRoomName.editText?.setText(room.roomName)
        binding.shapeRoomDesc.editText?.setText(room.roomDesc)
        Glide.with(context)
                .load(AppConfig.BASE_URL + "upload/" + room.roomImagePath)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(binding.shapeRoomImage)
        binding.shapeRoomImage.setOnClickListener {
            PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofAll())
                    .selectionMode(PictureConfig.SINGLE)
                    .isEnableCrop(true)
                    .cropImageWideHigh(200, 200)
                    .withAspectRatio(1, 1)//裁剪比例
                    .rotateEnabled(false)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: List<LocalMedia?>) {
                            // 结果回调
                            if (result.isEmpty()) return
                            val media = result[0]
                            val path = if (media?.isCut == true) media.cutPath else media?.path
                            if (path != null) {
                                imagePath = path
                            }
                            Glide.with(context as Activity)
                                    .load(path)
                                    .into(binding.shapeRoomImage)
                        }

                        override fun onCancel() {
                            // 取消
                        }
                    })
        }
        dialog.show {
            noAutoDismiss()
            positiveButton(R.string.scene_select_btn) {
                if (binding.shapeRoomName.editText?.text.toString() == "") {
                    binding.shapeRoomName.error = "房间名不可为空"
                    return@positiveButton
                }
                if (binding.shapeRoomDesc.editText?.text.toString() == "") {
                    binding.shapeRoomDesc.error = "房间描述不可为空"
                    return@positiveButton
                }
                room.roomName = binding.shapeRoomName.editText?.text.toString()
                room.roomDesc = binding.shapeRoomDesc.editText?.text.toString()
                if (imagePath != null) {
                    presenter.uploadFile(activity, imagePath!!, room)
                }
                presenter.updateRoom(activity, room)
                getAdapter()?.notifyItemChanged(position)
                dismiss()
            }
            negativeButton(R.string.scene_select_cal) { dismiss() }
        }
    }

    override fun updateRoomResult(response: BaseResponse<Any>) {
        if (response.code == 1) {
            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun deleteRoomResult(response: BaseResponse<Any>, room: Room) {
        if (response.code == 1) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun uploadFileResult(response: BaseResponse<String>, room: Room) {
        if (response.code == 1) {
            room.roomImagePath = response.data
            presenter.updateRoom(activity, room)
        } else
            Toast.makeText(context, "图像上传失败", Toast.LENGTH_SHORT).show();
    }
}
