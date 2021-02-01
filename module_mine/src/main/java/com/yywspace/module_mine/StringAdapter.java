package com.yywspace.module_mine;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
 * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
 */

public class StringAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public StringAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.mine_text, s);
    }
}
