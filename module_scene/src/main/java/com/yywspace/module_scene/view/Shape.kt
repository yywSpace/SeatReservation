package com.yywspace.module_scene.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF

abstract class Shape {
    lateinit var name: String

    // 是否被选中
    var isFocus = false
    var isLocked = false
    var touchLocation: TouchLocation = TouchLocation.OTHER
    var backgroundColor: Int = Color.GRAY
    var backgroundAlpha: Int = 128
    var onShapeTranslateListener: ((shape: Shape) -> Unit?)? = null
    var onShapeScaleListener: ((shape: Shape) -> Unit)? = null
    var onShapeStretchListener: ((shape: Shape) -> Unit)? = null

    abstract fun draw(canvas: Canvas?)

    open fun scale(scale: Float, centerX: Float, centerY: Float) {
        onShapeScaleListener?.invoke(this)
    }

    open fun translate(deltaX: Float, deltaY: Float) {
        onShapeTranslateListener?.invoke(this)
    }

    open fun stretch(deltaX: Float, deltaY: Float) {
        onShapeStretchListener?.invoke(this)
    }

    abstract fun inArea(x: Float, y: Float): Boolean

    abstract fun inCorner(x: Float, y: Float): Boolean

    abstract fun getCenter(): PointF

    abstract fun changeBackground(color: Int)

    abstract fun changeAlpha(alpha: Int)

    abstract fun getWidth(): Float

    abstract fun getHeight(): Float

    abstract fun setWidth(width: Float)

    abstract fun setHeight(height: Float)

    abstract fun getX(): Float

    abstract fun getY(): Float

    abstract fun setX(x: Float)

    abstract fun setY(y: Float)
}