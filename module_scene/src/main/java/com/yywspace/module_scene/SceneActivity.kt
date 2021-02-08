package com.yywspace.module_scene

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.textfield.TextInputLayout
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.contract.PhotoSelectResultContract
import com.yywspace.module_scene.databinding.SceneActivityBinding
import com.yywspace.module_scene.view.RectShape
import com.yywspace.module_scene.view.Shape


@Route(path = RouterPath.SCENE_PATH)
open class SceneActivity : AppCompatActivity() {
    companion object {
        var shapeCount = 0;
    }
    lateinit var roomMaps: MutableMap<String, Room>
    lateinit var binding: SceneActivityBinding
    var shape: Shape? = null
    private lateinit var bottomPanel: BottomPanel
    lateinit var roomImageView: ImageView

    lateinit var bottomOptionPanel: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SceneActivityBinding.inflate(layoutInflater)
        roomMaps = mutableMapOf()
        setContentView(binding.root)
        setSupportActionBar(binding.sceneToolBar)
        initBottomOptPanel()
        bottomPanel = BottomPanel(this, layoutInflater)
        bottomPanel.onShapeElementChanged = {
            binding.imageMap.invalidate()
        }

        binding.imageMap.apply {
            setOnShapeClickListener { shape, xOnImage, yOnImage ->
                this@SceneActivity.shape = shape
                if (isPreview) {
                    MaterialDialog(context).show {
                        title(text = "进入房间")
                        message(text = "是否要编辑房间座位")
                        positiveButton(R.string.scene_select_btn) {
                            startActivity(Intent(this@SceneActivity, RoomActivity::class.java))
                            Toast.makeText(context, "进入房间", Toast.LENGTH_SHORT).show();
                        }
                        negativeButton(R.string.scene_select_cal)
                        lifecycleOwner(this@SceneActivity)
                    }
                } else
                    bottomPanel.assembleSubOptPanel(shape)
            }
            setOnBackgroundClickListener {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scene_room_create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scene_preview -> {
                Toast.makeText(this, "scene_preview", Toast.LENGTH_SHORT).show();
                binding.imageMap.isPreview = true
                bottomPanel.dismissAll()
                binding.imageMap.setShapesFocus(false)
                binding.imageMap.invalidate()
                bottomOptionPanel.visibility = View.INVISIBLE
            }
            R.id.scene_edit -> {
                Toast.makeText(this, "scene_edit", Toast.LENGTH_SHORT).show();
                binding.imageMap.isPreview = false
                bottomOptionPanel.visibility = View.VISIBLE
            }
            R.id.scene_add -> {
                val shapeName = "rect${shapeCount++}"
                binding.imageMap.addShape(RectShape(RectF(100f, 100f, 400f, 400f), shapeName, this).apply {
                    backgroundAlpha = 128
                    onShapeScaleListener = {
                        if (shape?.isFocus == true)
                            bottomPanel.assembleSubOptPanel(it)
                    }
                    onShapeStretchListener = {
                        if (shape?.isFocus == true)
                            bottomPanel.assembleSubOptPanel(it)
                    }
                    onShapeTranslateListener = {
                        if (shape?.isFocus == true)
                            bottomPanel.assembleSubOptPanel(it)
                    }
                })
                roomMaps[shapeName] = Room(-1,"Room${roomMaps.size}")
            }
            R.id.scene_replace_background -> {
                photoSelectLauncherPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            R.id.scene_clear -> {
                binding.imageMap.removeAllShapes()
                roomMaps.clear()
                binding.imageMap.invalidate()
            }
            R.id.scene_bottom_opt_panel -> {
                bottomOptionPanel.visibility =
                        if (bottomOptionPanel.visibility == View.INVISIBLE)
                            View.VISIBLE
                        else View.INVISIBLE
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initBottomOptPanel() {
        bottomOptionPanel = binding.root.findViewById<LinearLayout>(R.id.shape_opt_panel).apply {
            findViewById<ImageView>(R.id.shape_lock).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                shape?.isLocked = shape?.isLocked != true
                bottomPanel.popupWindowMap.values.forEach {
                    it.dismiss()
                }
                binding.imageMap.invalidate()
            }
            findViewById<ImageView>(R.id.shape_size_adjust).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (shape?.isLocked == true) {
                    Toast.makeText(context, "图形已锁定", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                bottomPanel.popupWindow("size", bottomOptionPanel)
            }
            findViewById<ImageView>(R.id.shape_location_adjust).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (shape?.isLocked == true) {
                    Toast.makeText(context, "图形已锁定", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                bottomPanel.popupWindow("location", bottomOptionPanel)
            }
            findViewById<ImageView>(R.id.shape_delete).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                binding.imageMap.removeShape(shape)
                roomMaps.remove(shape?.name)
                bottomPanel.popupWindowMap.values.forEach {
                    it.dismiss()
                }
            }
            findViewById<ImageView>(R.id.shape_detail).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val dialog = MaterialDialog(context).apply {
                    title(text = "房间信息")
                    customView(R.layout.scene_shape_room_info)
                }
                val room = roomMaps[shape?.name]
                LogUtils.d(room.toString())
                val customView = dialog.getCustomView()
                val nameInputLayout = customView.findViewById<TextInputLayout>(R.id.shape_room_name)
                nameInputLayout.editText?.setText(room?.roomName)
                val descInputLayout = customView.findViewById<TextInputLayout>(R.id.shape_room_desc)
                descInputLayout.editText?.setText(room?.roomDesc)

                roomImageView = customView.findViewById<ImageView>(R.id.shape_room_image)
                        .apply {
                            if (room?.roomImage != null) {
                                setImageBitmap(room.roomImage)
                            }
                            setOnClickListener {
                                photoSelectLauncherRoomImagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                dialog.show {
                    noAutoDismiss()
                    positiveButton(R.string.scene_select_btn) {
                        if (nameInputLayout.editText?.text.toString() == "") {
                            nameInputLayout.error = "房间名不可为空"
                            return@positiveButton
                        }
                        if (descInputLayout.editText?.text.toString() == "") {
                            descInputLayout.error = "房间描述不可为空"
                            return@positiveButton
                        }
                        room?.roomName = nameInputLayout.editText?.text.toString()
                        room?.roomDesc = descInputLayout.editText?.text.toString()
                        dismiss()
                    }
                    negativeButton(R.string.scene_select_cal) { dismiss() }
                }
            }
            findViewById<ImageView>(R.id.shape_color_pick).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (shape is RectShape) {
                    MaterialDialog(context).show {
                        title(R.string.scene_colors)
                        colorChooser(
                                colors = ColorPalette.Primary,
                                subColors = ColorPalette.PrimarySub,
                                allowCustomArgb = true
                        ) { _, color ->
                            (shape as RectShape).changeBackground(color)
                            binding.imageMap.invalidate()
                        }
                        positiveButton(R.string.scene_select_btn)
                        negativeButton(R.string.scene_select_cal)
                        lifecycleOwner(this@SceneActivity)
                    }
                } else {
                    Toast.makeText(context, "当前图像无法调节颜色", Toast.LENGTH_SHORT).show();
                }
            }
            findViewById<ImageView>(R.id.shape_alpha_adjust).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                bottomPanel.popupWindow("alpha", bottomOptionPanel)
            }
        }
    }

    private val photoSelectLauncher = registerForActivityResult(PhotoSelectResultContract()) { result ->
        if (result == null)
            return@registerForActivityResult
        binding.imageMap.setImageBitmap(result)
    }

    private val photoSelectLauncherPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted)
            photoSelectLauncher.launch()
    }
    private val photoSelectLauncherRoomImage = registerForActivityResult(PhotoSelectResultContract()) { result ->
        if (result == null)
            return@registerForActivityResult
        roomMaps[shape?.name]?.roomImage = result
        roomImageView.setImageBitmap(result)
    }

    private val photoSelectLauncherRoomImagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted)
            photoSelectLauncherRoomImage.launch()
    }


}