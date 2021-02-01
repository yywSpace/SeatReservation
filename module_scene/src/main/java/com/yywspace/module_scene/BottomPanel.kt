package com.yywspace.module_scene

import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.view.Shape


class BottomPanel(val context: Context, layoutInflater: LayoutInflater) {
    private var alphaAdjustPanel: View
    private var sizeAdjustPanel: View
    private var locationAdjustPanel: View
    var onShapeElementChanged: (() -> Unit)? = null
    lateinit var popupWindowMap: Map<String, PopupWindow>
    var hasDragSeekBar = false
    var currentX = 0f
    var currentY = 0f

    init {
        alphaAdjustPanel = layoutInflater.inflate(R.layout.scene_shape_opt_alpha, null, false)
        sizeAdjustPanel = layoutInflater.inflate(R.layout.scene_shape_opt_size, null, false)
        locationAdjustPanel = layoutInflater.inflate(R.layout.scene_shape_opt_move, null, false)
        initPopupWindowMap()
    }

    fun popupWindow(panel: String, parent: View) {
        if (popupWindowMap[panel]?.isShowing == true)
            popupWindowMap[panel]?.dismiss()
        else {
            for ((key, value) in popupWindowMap) {
                if (key != panel)
                    value.dismiss()
                else {
                    value.showAtLocation(parent, Gravity.BOTTOM, 0, 175)
                }
            }
        }
    }

    fun initAlphaAdjustPanel(shape: Shape) {
        alphaAdjustPanel.apply {
            val alphaLabel = findViewById<TextView>(R.id.shape_alpha_label).apply {
                text = shape.backgroundAlpha.toString()
            }
            findViewById<SeekBar>(R.id.shape_alpha_label_seek_bar)
                    .apply {
                        setOnSeekBarChangeListener(null)
                        progress = shape.backgroundAlpha
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                alphaLabel.text = progress.toString()
                                shape.changeAlpha(progress)
                                LogUtils.d("${shape.name} ${shape.backgroundAlpha}")
                                onShapeElementChanged?.invoke()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                        })
                    }
        }
    }

    private fun initSizeAdjustPanel(shape: Shape) {
        sizeAdjustPanel.apply {
            val widthLabel = findViewById<TextView>(R.id.shape_width_label).apply {
                text = String.format("%.2f", shape.getWidth())
                setOnClickListener {
                    MaterialDialog(context).show {
                        title(text = "设定宽度")
                        input(waitForPositiveButton = false, hint = "${shape.getWidth()}", inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL) { dialog, text ->
                            var isValid = true
                            try {
                                val width = text.toString().toFloat()
                                shape.setWidth(width)
                                onShapeElementChanged?.invoke()
                            } catch (e: NumberFormatException) {
                                isValid = false
                                dialog.getInputField().error = "输入必须为数字"
                            }
                            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                        }
                        positiveButton(R.string.scene_select_btn)
                        negativeButton(R.string.scene_select_cal)
                    }
                }
            }
            val heightLabel = findViewById<TextView>(R.id.shape_height_label).apply {
                text = String.format("%.2f", shape.getHeight())
                setOnClickListener {
                    MaterialDialog(context).show {
                        title(text = "设定高度")
                        input(waitForPositiveButton = false, hint = "${shape.getWidth()}", inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL) { dialog, text ->
                            var isValid = true
                            try {
                                val height = text.toString().toFloat()
                                shape.setHeight(height)
                                onShapeElementChanged?.invoke()

                            } catch (e: NumberFormatException) {
                                isValid = false
                                dialog.getInputField().error = "输入必须为数字"
                            }
                            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                        }
                        positiveButton(R.string.scene_select_btn)
                        negativeButton(R.string.scene_select_cal)
                    }
                }
            }
            findViewById<SeekBar>(R.id.shape_width_seek_bar)
                    .apply {
                        val currentWidth = shape.getWidth()
                        setOnSeekBarChangeListener(null)
                        progress = 100
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                shape.setWidth(currentWidth + (progress - 100) * .5f)
                                widthLabel.text = String.format("%.2f", shape.getWidth())
                                onShapeElementChanged?.invoke()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                            }

                        })
                    }
            findViewById<SeekBar>(R.id.shape_height_seek_bar)
                    .apply {
                        val currentHeight = shape.getHeight()
                        setOnSeekBarChangeListener(null)
                        progress = 100
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                shape.setHeight(currentHeight + (progress - 100) * .5f)
                                heightLabel.text = String.format("%.2f", shape.getHeight())
                                onShapeElementChanged?.invoke()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }

                        })
                    }

            findViewById<ImageButton>(R.id.shape_seek_bar_reset).apply {
                setOnClickListener {
                    initSizeAdjustPanel(shape)
                }
            }
        }
    }

    private fun initLocationAdjustPanel(shape: Shape) {
        locationAdjustPanel.apply {
            val xLabel = findViewById<TextView>(R.id.shape_x_label).apply {
                text = String.format("%.2f", shape.getX())
                setOnClickListener {
                    MaterialDialog(context).show {
                        title(text = "设定x方向位置")
                        input(waitForPositiveButton = false, hint = "${shape.getWidth()}", inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL) { dialog, text ->
                            var isValid = true
                            try {
                                val x = text.toString().toFloat()
                                shape.setX(x)
                                onShapeElementChanged?.invoke()
                            } catch (e: NumberFormatException) {
                                isValid = false
                                dialog.getInputField().error = "输入必须为数字"
                            }
                            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                        }
                        positiveButton(R.string.scene_select_btn)
                        negativeButton(R.string.scene_select_cal)
                    }
                }
            }
            val yLabel = findViewById<TextView>(R.id.shape_y_label).apply {
                text = String.format("%.2f", shape.getY())
                setOnClickListener {
                    MaterialDialog(context).show {
                        title(text = "设定y方向位置")
                        input(waitForPositiveButton = false, hint = "${shape.getWidth()}", inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL) { dialog, text ->
                            var isValid = true
                            try {
                                val y = text.toString().toFloat()
                                shape.setY(y)
                                onShapeElementChanged?.invoke()

                            } catch (e: NumberFormatException) {
                                isValid = false
                                dialog.getInputField().error = "输入必须为数字"
                            }
                            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                        }
                        positiveButton(R.string.scene_select_btn)
                        negativeButton(R.string.scene_select_cal)
                    }
                }
            }
            findViewById<SeekBar>(R.id.shape_x_seek_bar)
                    .apply {
                        setOnSeekBarChangeListener(null)
                        if (!hasDragSeekBar) {
                            currentX = shape.getX()
                            progress = 100
                        }
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                shape.setX(currentX + (progress - 100) * .5f)
                                xLabel.text = String.format("%.2f", shape.getX())
                                onShapeElementChanged?.invoke()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                hasDragSeekBar = true
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                hasDragSeekBar = false
                            }

                        })
                    }
            findViewById<SeekBar>(R.id.shape_y_seek_bar)
                    .apply {
                        setOnSeekBarChangeListener(null)
                        if (!hasDragSeekBar) {
                            currentY = shape.getY()
                            progress = 100
                        }
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                shape.setY(currentY + (progress - 100) * .5f)
                                yLabel.text = String.format("%.2f", shape.getY())
                                onShapeElementChanged?.invoke()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                hasDragSeekBar = true

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                hasDragSeekBar = false
                            }

                        })
                    }

            findViewById<ImageButton>(R.id.shape_seek_bar_reset).apply {
                setOnClickListener {
                    initLocationAdjustPanel(shape)
                }
            }
        }
    }

    fun assembleSubOptPanel(shape: Shape?) {
        if (shape == null)
            return
        initAlphaAdjustPanel(shape)
        initSizeAdjustPanel(shape)
        initLocationAdjustPanel(shape)
    }

    fun dismissAll() {
        popupWindowMap.values.forEach {
            it.dismiss()
        }
    }



    private fun initPopupWindowMap() {
        val alphaPanel = PopupWindow(alphaAdjustPanel,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            // 点击外部不消失
            isFocusable = false
            isOutsideTouchable = false
            animationStyle = R.style.SceneBottomAnimStyle  //进入退出的动画
        }
        val sizePanel = PopupWindow(sizeAdjustPanel,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            // 点击外部不消失
            isFocusable = false
            isOutsideTouchable = false
            animationStyle = R.style.SceneBottomAnimStyle  //进入退出的动画
        }
        val locationPanel = PopupWindow(locationAdjustPanel,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            // 点击外部不消失
            isFocusable = false
            isOutsideTouchable = false
            animationStyle = R.style.SceneBottomAnimStyle  //进入退出的动画
        }
        popupWindowMap = mapOf(
                "alpha" to alphaPanel,
                "size" to sizePanel,
                "location" to locationPanel
        )
    }
}