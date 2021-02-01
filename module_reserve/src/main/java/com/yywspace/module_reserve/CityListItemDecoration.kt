package com.yywspace.module_reserve

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CityListItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    var dividerHeight = 1

    /**
     * divider 左右padding
     */
    private val dividerGap = 50
    private val dividerPaint: Paint = Paint()

    init {
        dividerPaint.color = Color.GRAY;
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount - 1
        val left = parent.paddingLeft + dividerGap
        val right = parent.width - parent.paddingRight - dividerGap
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = view.bottom + dividerHeight.toFloat()
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }
}