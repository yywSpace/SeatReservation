package com.yywspace.module_base.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initARouter();
    }

    private void initARouter() {
        //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
        ARouter.openDebug();
        ARouter.openLog();
        ARouter.init(this);
    }
}
