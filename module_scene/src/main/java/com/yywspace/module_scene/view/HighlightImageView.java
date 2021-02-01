package com.yywspace.module_scene.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.yywspace.module_base.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.Pair;

public class HighlightImageView extends TransformableImageView {

    private Boolean isPreview = false;

    public HighlightImageView(Context context) {
        this(context, null);
    }

    public HighlightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final List<Shape> shapesList = new ArrayList<>();

    private OnShapeClickListener onShapeClickListener;
    private OnBackgroundClickListener mOnBackgroundClickListener;

    public void setOnShapeClickListener(OnShapeClickListener onShapeClickListener) {
        this.onShapeClickListener = onShapeClickListener;
    }

    public void addShape(Shape shape) {
        shapesList.add(shape);
        postInvalidate();
    }

    public void addShapes(List<Shape> shapes) {
        shapesList.addAll(shapes);
        postInvalidate();
    }

    public void setShapesFocus(boolean isFocus) {
        for (Shape shape : shapesList) {
            shape.setFocus(isFocus);
        }
    }

    public boolean hasShapesFocus() {
        boolean res = false;
        for (Shape shape : shapesList) {
            if (shape.isFocus())
                return true;
        }
        return res;
    }

    public void setShapesLocked(boolean isLocked) {
        for (Shape shape : shapesList) {
            shape.setLocked(isLocked);
        }
    }

    public void removeShape(Shape shape) {
        shapesList.remove(shape);
        postInvalidate();
    }

    public void removeAllShapes() {
        shapesList.clear();
    }

    public List<Shape> getShapes() {
        return new ArrayList<>(shapesList);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        for (Shape shape : shapesList) {
            shape.draw(canvas);
        }
        canvas.restore();
    }


    @Override
    protected boolean postScale(float scaleFactor, float scaleCenterX, float scaleCenterY) {
        if (super.postScale(scaleFactor, scaleCenterX, scaleCenterY)) {
            if (scaleFactor != 0)
                for (Shape shape : shapesList)
                    shape.scale(scaleFactor, scaleCenterX, scaleCenterY);
            return true;
        }
        return false;
    }

    @Override
    protected void postTranslate(float deltaX, float deltaY) {
        super.postTranslate(deltaX, deltaY);
        if (!(deltaX == 0 && deltaY == 0)) {
            for (Shape shape : shapesList) {
                shape.translate(deltaX, deltaY);
            }
        }
    }

    @NotNull
    @Override
    protected Pair<ClickedType, Shape> onClick(float xOnView, float yOnView, ShapeTouchListener shapeTouchListener) {
        if (onShapeClickListener == null) return new Pair<>(ClickedType.BACKGROUND, null);
        // 反向读取，点击是首先选取上层
        List<Shape> reverseList = new ArrayList<>(shapesList);
        Collections.reverse(reverseList);
        for (Shape shape : reverseList) {
            if (isPreview) {
                LogUtils.d(reverseList.toString());
                if (shape.inArea(xOnView, yOnView)) {
                    if (shapeTouchListener != null) shapeTouchListener.setShape(shape);
                    onShapeClickListener.onShapeClick(shape, xOnView, yOnView);
                    LogUtils.d(reverseList.size() + "-" + xOnView + "-" + yOnView);
                    return new Pair<>(ClickedType.BACKGROUND, shape);
                }
            } else {
                if (shape.inCorner(xOnView, yOnView)) {
                    setShapesFocus(false);
                    shape.setFocus(true);
                    return new Pair<>(ClickedType.CORNER, shape);
                }
                if (shape.inArea(xOnView, yOnView)) {
                    if (shapeTouchListener != null) shapeTouchListener.setShape(shape);
                    // 将选取的shape放到最后，以实现在draw时显示在最上层
                    shapesList.remove(shape);
                    shapesList.add(shape);
                    onShapeClickListener.onShapeClick(shape, xOnView, yOnView);
                    setShapesFocus(false);
                    shape.setFocus(true);
                    LogUtils.d("shape");
                    return new Pair<>(ClickedType.SHAPE, shape);
                }
            }

        }
        if (mOnBackgroundClickListener != null)
            mOnBackgroundClickListener.onBackgroundClickListener();
        return new Pair<>(ClickedType.BACKGROUND, null);
    }

    public void setPreview(Boolean preview) {
        isPreview = preview;
    }

    public Boolean isPreview() {
        return isPreview;
    }

    public void setOnBackgroundClickListener(OnBackgroundClickListener onBackgroundClickListener) {
        this.mOnBackgroundClickListener = onBackgroundClickListener;
    }

    public interface OnShapeClickListener {
        void onShapeClick(Shape shape, float xOnImage, float yOnImage);
    }

    public interface OnBackgroundClickListener {
        void onBackgroundClickListener();
    }
}
