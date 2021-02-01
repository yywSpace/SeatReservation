package com.yywspace.module_scene.view

import android.content.Context
import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.R
import com.yywspace.module_scene.imagemap.support.ScaleUtility


class RectShape() : Shape() {
    private var cornerRadius: Float = 20f
    private lateinit var context: Context
    private lateinit var lockBitmap: Bitmap

    // 用于选中corner时确定可选中的作用范围，2表示选中corner的实际范围为正常的2倍
    private var cornerRadiusFactor: Float = 3f

    // 光晕效果距离边框距离
    private val edgeHaloGap = 0

    private var backgroundRect: RectF = RectF(100f, 100f, 400f, 400f)

    private var paintBackground: Paint = Paint().apply {
        color = backgroundColor
        alpha = backgroundAlpha
    }

    override fun changeBackground(color: Int) {
        backgroundColor = color
        paintBackground = Paint().apply {
            this.color = backgroundColor
            alpha = backgroundAlpha
        }
    }

    override fun changeAlpha(alpha: Int) {
        backgroundAlpha = alpha
        paintBackground = Paint().apply {
            color = backgroundColor
            this.alpha = backgroundAlpha
        }
    }

    override fun getWidth(): Float {
        return backgroundRect.right - backgroundRect.left
    }

    override fun getHeight(): Float {
        return backgroundRect.bottom - backgroundRect.top

    }

    override fun setWidth(width: Float) {
        backgroundRect.right = backgroundRect.left + width;
    }

    override fun setHeight(height: Float) {
        backgroundRect.bottom = backgroundRect.top + height;
    }

    override fun getX(): Float {
        return backgroundRect.left
    }

    override fun getY(): Float {
        return backgroundRect.top
    }

    override fun setX(x: Float) {
        val deltaX = x - getX()
        translate(deltaX, 0f)
    }

    override fun setY(y: Float) {
        val deltaY = y - getY()
        translate(0f, deltaY)
    }


    private lateinit var paintCorner: Paint
    private lateinit var paintEdge: Paint

    constructor(backgroundRect: RectF, name: String, context: Context) : this() {
        this.backgroundRect = backgroundRect
        this.name = name
        this.context = context
        paintEdge = Paint().apply {
            color = context.getColor(R.color.sceneColorLightBlue)
            maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)
            strokeWidth = 1.5f
        }
        paintCorner = Paint().apply {
            color = context.getColor(R.color.sceneColorLightBlue)
            // maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)
        }

        lockBitmap = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_lock, context.theme)?.toBitmap()!!
        lockBitmap = Bitmap.createScaledBitmap(lockBitmap, 80, 80, true)
    }

    override fun draw(canvas: Canvas?) {
        drawBackgroundColor(canvas)
        if (isFocus) {
            if (isLocked) {
                drawLockIcon(canvas)
            } else {
                drawCorner(canvas)
            }
            drawEdgeHolo(canvas)
        }
    }

    override fun scale(scale: Float, centerX: Float, centerY: Float) {
        super.scale(scale, centerX, centerY)
        LogUtils.d("$scale $centerX $centerY ")
        val leftTop = ScaleUtility.scaleByPoint(backgroundRect.left, backgroundRect.top, centerX, centerY, scale)
        backgroundRect.left = leftTop.x
        backgroundRect.top = leftTop.y
        val rightBottom = ScaleUtility.scaleByPoint(backgroundRect.right, backgroundRect.bottom, centerX, centerY, scale)
        backgroundRect.right = rightBottom.x
        backgroundRect.bottom = rightBottom.y
    }

    override fun translate(deltaX: Float, deltaY: Float) {
        super.translate(deltaX, deltaY)
        backgroundRect.apply {
            left += deltaX
            right += deltaX
            top += deltaY
            bottom += deltaY
        }
    }

    override fun stretch(deltaX: Float, deltaY: Float) {
        super.stretch(deltaX, deltaY)
        when (touchLocation) {
            TouchLocation.LEFT_TOP -> {
                if (backgroundRect.left + deltaX <= backgroundRect.right - cornerRadius * 6)
                    backgroundRect.left += deltaX
                if (backgroundRect.top + deltaY <= backgroundRect.bottom - cornerRadius * 6)
                    backgroundRect.top += deltaY
            }
            TouchLocation.RIGHT_TOP -> {
                if (backgroundRect.right + deltaX >= backgroundRect.left + cornerRadius * 6)
                    backgroundRect.right += deltaX
                if (backgroundRect.top + deltaY <= backgroundRect.bottom - cornerRadius * 6)
                    backgroundRect.top += deltaY
            }
            TouchLocation.LEFT_BOTTOM -> {
                if (backgroundRect.left + deltaX <= backgroundRect.right - cornerRadius * 6)
                    backgroundRect.left += deltaX
                if (backgroundRect.bottom + deltaX >= backgroundRect.top + cornerRadius * 6)
                    backgroundRect.bottom += deltaY
            }
            TouchLocation.RIGHT_BOTTOM -> {
                if (backgroundRect.right + deltaX >= backgroundRect.left + cornerRadius * 6)
                    backgroundRect.right += deltaX
                if (backgroundRect.bottom + deltaX >= backgroundRect.top + cornerRadius * 6)
                    backgroundRect.bottom += deltaY
            }
            else -> {

            }
        }
    }

    /***
     * 父View中点击坐标
     */
    override fun inArea(x: Float, y: Float): Boolean {
        var ret = false
        if (x > backgroundRect.left && x < backgroundRect.right)
            if (y > backgroundRect.top && y < backgroundRect.bottom)
                ret = true
        return ret
    }

    override fun getCenter(): PointF {
        return PointF(
                (backgroundRect.left + backgroundRect.right) / 2f,
                (backgroundRect.top + backgroundRect.bottom) / 2f
        )
    }

    override fun inCorner(x: Float, y: Float): Boolean {
        var res = false
        if (x > backgroundRect.left - cornerRadius * cornerRadiusFactor && x < backgroundRect.left + cornerRadius * cornerRadiusFactor &&
                y > backgroundRect.top - cornerRadius * cornerRadiusFactor && y < backgroundRect.top + cornerRadius * cornerRadiusFactor) {
            touchLocation = TouchLocation.LEFT_TOP
            res = true
        }
        if (x > backgroundRect.right - cornerRadius * cornerRadiusFactor && x < backgroundRect.right + cornerRadius * cornerRadiusFactor &&
                y > backgroundRect.top - cornerRadius * cornerRadiusFactor && y < backgroundRect.top + cornerRadius * cornerRadiusFactor) {
            touchLocation = TouchLocation.RIGHT_TOP
            res = true
        }
        if (x > backgroundRect.left - cornerRadius * cornerRadiusFactor && x < backgroundRect.left + cornerRadius * cornerRadiusFactor &&
                y > backgroundRect.bottom - cornerRadius * cornerRadiusFactor && y < backgroundRect.bottom + cornerRadius * cornerRadiusFactor) {
            touchLocation = TouchLocation.LEFT_BOTTOM
            res = true
        }
        if (x > backgroundRect.right - cornerRadius * cornerRadiusFactor && x < backgroundRect.right + cornerRadius * cornerRadiusFactor &&
                y > backgroundRect.bottom - cornerRadius * cornerRadiusFactor && y < backgroundRect.bottom + cornerRadius * cornerRadiusFactor) {
            touchLocation = TouchLocation.RIGHT_BOTTOM
            res = true
        }
        return res
    }


    private fun drawCorner(canvas: Canvas?) {
        canvas?.drawCircle(backgroundRect.left, backgroundRect.top, cornerRadius, paintCorner)
        canvas?.drawCircle(backgroundRect.right, backgroundRect.top, cornerRadius, paintCorner)
        canvas?.drawCircle(backgroundRect.left, backgroundRect.bottom, cornerRadius, paintCorner)
        canvas?.drawCircle(backgroundRect.right, backgroundRect.bottom, cornerRadius, paintCorner)
    }

    private fun drawEdgeHolo(canvas: Canvas?) {
        canvas?.drawLine(backgroundRect.left - edgeHaloGap, backgroundRect.top - edgeHaloGap, backgroundRect.right + edgeHaloGap, backgroundRect.top - edgeHaloGap, paintEdge)
        canvas?.drawLine(backgroundRect.right + edgeHaloGap, backgroundRect.top - edgeHaloGap, backgroundRect.right + edgeHaloGap, backgroundRect.bottom + edgeHaloGap, paintEdge)
        canvas?.drawLine(backgroundRect.left - edgeHaloGap, backgroundRect.bottom + edgeHaloGap, backgroundRect.right + edgeHaloGap, backgroundRect.bottom + edgeHaloGap, paintEdge)
        canvas?.drawLine(backgroundRect.left - edgeHaloGap, backgroundRect.top - edgeHaloGap, backgroundRect.left - edgeHaloGap, backgroundRect.bottom + edgeHaloGap, paintEdge)
    }

    private fun drawLockIcon(canvas: Canvas?) {
        canvas?.drawBitmap(lockBitmap,
                Rect(0, 0, lockBitmap.width, lockBitmap.height),
                Rect((backgroundRect.right - lockBitmap.width).toInt(),
                        (backgroundRect.top - lockBitmap.height).toInt(),
                        backgroundRect.right.toInt(),
                        backgroundRect.top.toInt()),
                Paint())
    }

    private fun drawBackgroundColor(canvas: Canvas?) {
        canvas?.drawRect(backgroundRect, paintBackground)
    }

    override fun toString(): String {
        return name
    }
}