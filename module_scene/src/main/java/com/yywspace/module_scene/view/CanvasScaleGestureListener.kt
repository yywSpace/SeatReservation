package com.yywspace.module_scene.view

import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import com.yywspace.module_base.util.LogUtils

class CanvasScaleGestureListener : SimpleOnScaleGestureListener() {
    var onScale: ((scaleFactor: Float, centerX: Float, centerY: Float) -> Unit)? = null
    var onScaleBegin: (() -> Unit)? = null

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        onScaleBegin?.invoke()
        return super.onScaleBegin(detector)
    }
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        onScale?.invoke(detector.scaleFactor, detector.focusX, detector.focusY)
        return true
    }
}