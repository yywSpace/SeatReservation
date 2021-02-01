package com.yywspace.module_scene.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.yywspace.module_base.util.LogUtils


open class TransformableImageView : AppCompatImageView {
    private var mInitialWidth = 900f // 初始宽度
    private var transformMatrix = Matrix() // 用于图片旋转、平移、缩放的矩阵
    private var imageScale = 1f // 图片总缩放大小
    private var lastPointF = PointF()
    private var transformType = TransformType.NONE // 转换类型

    companion object {
        const val MAX_SCALE_VALUE = 10f  // 最大缩放大小
        const val MIN_SCALE_VALUE = 1f // 最小缩放大小
    }

    private var touchListener: TouchListener
    private var shapeTouchListener: ShapeTouchListener
    private var backgroundTouchListener: BackgroundTouchListener

    private val scaleGestureDetector: ScaleGestureDetector

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        scaleGestureDetector = ScaleGestureDetector(context, CanvasScaleGestureListener().apply {
            onScaleBegin = {
                transformType = TransformType.SCALE
            }
            onScale = { scaleFactor, centerX, centerY ->
                postScale(scaleFactor, centerX, centerY)
            }
        })
        shapeTouchListener = ShapeTouchListener()
        backgroundTouchListener = BackgroundTouchListener()
        touchListener = backgroundTouchListener
        scaleType = ScaleType.MATRIX
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 缩放
        if (touchListener is BackgroundTouchListener)
            scaleGestureDetector.onTouchEvent(event)
        // 平移
        touchListener.curr = PointF(event.x, event.y)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchListener.onActionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                touchListener.onActionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                touchListener.onActionUp(event)
            }
        }
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initImgPositionAndSize()
    }

    protected open fun onClick(xOnView: Float, yOnView: Float, shapeTouchListener: ShapeTouchListener?): Pair<ClickedType, Shape?> {
        return Pair(ClickedType.NONE, null)
    }


    protected open fun postScale(scaleFactor: Float, scaleCenterX: Float, scaleCenterY: Float): Boolean {
        imageScale *= scaleFactor
        // 超出缩放范围便返回
        if (imageScale > MAX_SCALE_VALUE || imageScale < MIN_SCALE_VALUE) {
            imageScale /= scaleFactor;
            return false
        }
        LogUtils.d("$imageScale $scaleFactor $scaleCenterX $scaleCenterY ")

        transformMatrix.postScale(scaleFactor, scaleFactor,
                scaleCenterX, scaleCenterY)
        // 追加至图片
        imageMatrix = transformMatrix
        return true
    }

    protected open fun postTranslate(deltaX: Float, deltaY: Float) {
        transformMatrix.postTranslate(deltaX, deltaY)
        // 追加至图片
        imageMatrix = transformMatrix
    }

    /**
     * 初始化图片位置和大小
     */
    private fun initImgPositionAndSize() {
        transformMatrix.reset()
        val imageRect = RectF().apply { set(drawable.bounds) }
        // 计算缩放比例，改变图片大小
        val scaleFactor: Float = mInitialWidth / imageRect.width()
        transformMatrix.postScale(scaleFactor, scaleFactor, imageRect.centerX(), imageRect.centerY())
        // 移动图片到中心
        transformMatrix.postTranslate((right - left) / 2 - imageRect.centerX(),
                (bottom - top) / 2 - imageRect.centerY())
        imageMatrix = transformMatrix
    }

    abstract inner class TouchListener {
        var curr = PointF()
        abstract fun onActionDown(event: MotionEvent);
        abstract fun onActionMove(event: MotionEvent);
        abstract fun onActionUp(event: MotionEvent);
    }

    inner class BackgroundTouchListener : TouchListener() {
        override fun onActionDown(event: MotionEvent) {
            lastPointF.set(event.x, event.y)
            transformType = TransformType.TRANSLATE
            val (clickedType, shape) = onClick(event.x, event.y, null)
            if (clickedType == ClickedType.SHAPE || clickedType == ClickedType.CORNER) {
                touchListener = shapeTouchListener.apply { this.shape = shape }
                invalidate()
            }
        }

        override fun onActionMove(event: MotionEvent) {
            if (transformType == TransformType.TRANSLATE) {
                val dx: Float = curr.x - lastPointF.x
                val dy: Float = curr.y - lastPointF.y
                postTranslate(dx, dy)
                LogUtils.d(imageLocation().toShortString())
            }
            lastPointF.set(curr.x, curr.y)
        }

        override fun onActionUp(event: MotionEvent) {
            transformType = TransformType.NONE
        }
    }

    inner class ShapeTouchListener : TouchListener() {

        var shape: Shape? = null
        var clickedType: ClickedType? = null

        override fun onActionDown(event: MotionEvent) {
            lastPointF.set(event.x, event.y)
            val (clickedType, shape) = onClick(event.x, event.y, shapeTouchListener)
            this.clickedType = clickedType
            if (clickedType == ClickedType.CORNER)
                this.shape = shape

            if (clickedType == ClickedType.BACKGROUND) {
                // 刷新以解决，shape corner不消失问题
                touchListener = backgroundTouchListener
                transformType = TransformType.TRANSLATE
            }
            invalidate()
        }

        override fun onActionMove(event: MotionEvent) {
            if (shape?.isLocked == true)
                return
            if (clickedType == ClickedType.CORNER) {
                val dx = curr.x - lastPointF.x
                val dy = curr.y - lastPointF.y
                shape?.stretch(dx, dy)
            } else {
                val dx = curr.x - lastPointF.x
                val dy = curr.y - lastPointF.y
                shape?.translate(dx, dy)
            }
            invalidate()
            lastPointF.set(curr.x, curr.y)
        }

        override fun onActionUp(event: MotionEvent) {
        }

    }

    fun imageLocation(): RectF {
        val rectF = RectF()
        if (drawable != null) {
            rectF[0f, 0f, drawable.intrinsicWidth.toFloat()] = drawable.intrinsicHeight.toFloat()
            imageMatrix.mapRect(rectF)
        }
        return rectF
    }

    enum class ClickedType {
        SHAPE,
        BACKGROUND,
        CORNER,
        NONE
    }

    enum class TransformType {
        SCALE,
        TRANSLATE,
        NONE
    }
}
