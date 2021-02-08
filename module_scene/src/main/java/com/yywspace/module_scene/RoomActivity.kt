package com.yywspace.module_scene

import android.Manifest
import android.graphics.RectF
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.textfield.TextInputLayout
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.contract.PhotoSelectResultContract
import com.yywspace.module_scene.databinding.SceneRoomActivityBinding
import com.yywspace.module_scene.view.RectShape
import com.yywspace.module_scene.view.Shape


class RoomActivity : AppCompatActivity() {
    companion object {
        var shapeCount = 0;
    }

    lateinit var seatMaps: MutableMap<String, Seat>
    lateinit var binding: SceneRoomActivityBinding
    var shape: Shape? = null
    private lateinit var bottomPanel: BottomPanel

    lateinit var bottomOptionPanel: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SceneRoomActivityBinding.inflate(layoutInflater)
        seatMaps = mutableMapOf()
        setContentView(binding.root)
        setSupportActionBar(binding.sceneToolBar)
        initBottomOptPanel()
        binding.imageMap.apply {
            setOnShapeClickListener { shape, xOnImage, yOnImage ->
                this@RoomActivity.shape = shape

                Toast.makeText(context, shape.name, Toast.LENGTH_SHORT).show();
            }
        }
        bottomPanel = BottomPanel(this, layoutInflater)
        bottomPanel.onShapeElementChanged = {
            binding.imageMap.invalidate()
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
                val shapeName = "seat${shapeCount++}"
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
                seatMaps[shapeName] = Seat(-1,shapeName,shapeName)
            }
            R.id.scene_replace_background -> {
                photoSelectLauncherPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            R.id.scene_clear -> {
                binding.imageMap.removeAllShapes()
                seatMaps.clear()
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
                seatMaps.remove(shape?.name)
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
                    title(text = "座位信息")
                    customView(R.layout.scene_shape_seat_info)
                }
                val seat = seatMaps[shape?.name]
                val customView = dialog.getCustomView()
                val nameInputLayout = customView.findViewById<TextInputLayout>(R.id.shape_seat_name)
                nameInputLayout.editText?.setText(seat?.seatName)
                val descInputLayout = customView.findViewById<TextInputLayout>(R.id.shape_seat_desc)
                descInputLayout.editText?.setText(seat?.seatDesc)

                dialog.show {
                    noAutoDismiss()
                    positiveButton(R.string.scene_select_btn) {
                        if (nameInputLayout.editText?.text.toString() == "") {
                            nameInputLayout.error = "座位名不可为空"
                            return@positiveButton
                        }
                        if (descInputLayout.editText?.text.toString() == "") {
                            descInputLayout.error = "座位描述不可为空"
                            return@positiveButton
                        }
                        seat?.seatName = nameInputLayout.editText?.text.toString()
                        seat?.seatDesc = descInputLayout.editText?.text.toString()
                        Toast.makeText(context, seat.toString(), Toast.LENGTH_SHORT).show();
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
                        lifecycleOwner(this@RoomActivity)
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

            findViewById<ImageView>(R.id.shape_copy).setOnClickListener {
                if (!binding.imageMap.hasShapesFocus()) {
                    Toast.makeText(context, "请先选择图形", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val shapeName = "seat${shapeCount++}"
                binding.imageMap.addShape(RectShape(
                        RectF(shape!!.getX(),
                                shape!!.getY() + shape!!.getHeight() + 100,
                                shape!!.getX() + shape!!.getWidth(),
                                shape!!.getY() + +shape!!.getHeight() + 100 + shape!!.getHeight()), shapeName, context).apply {
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
                seatMaps[shapeName] = Seat(-1,shapeName,shapeName)

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
}