package com.yywspace.module_base.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

import androidx.core.widget.NestedScrollView


public object ViewScreenShotUtils {
    /**
     * 获取scrollview的截屏
     */
    public fun scrollViewScreenShot(scrollView: NestedScrollView): Bitmap? {
        var h = 0
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
        val bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }
}