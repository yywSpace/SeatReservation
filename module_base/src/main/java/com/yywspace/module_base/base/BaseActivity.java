package com.yywspace.module_base.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.yywspace.module_base.R;

/**
 * 父类->基类->动态指定类型->泛型设计（通过泛型指定动态类型->由子类指定，父类只需要规定范围即可）
 */
public abstract class BaseActivity<V extends BaseViewImp,P extends BasePresenter<V>> extends AppCompatActivity {

    //引用V层和P层
    private P presenter;
    private V view;
    
    public P getPresenter(){
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止所有的activity横屏
        setContentView(getLayout(getLayoutInflater()));
        ARouter.getInstance().inject(this);
        if(presenter == null){
            presenter = createPresenter();
        }
        if(view == null){
            view = createView();
        }
        if(presenter != null && view != null){
            presenter.attachView(view);
        }
        init();
    }

    //由子类指定具体类型
    public abstract View getLayout(@NonNull LayoutInflater inflater);
    public abstract P createPresenter();
    public abstract V createView();
    public abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null){
            presenter.detachView();
        }
    }
}
