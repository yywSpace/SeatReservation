package com.yywspace.seatreservation;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yywspace.module_base.net.ServerUtils;
import com.yywspace.module_base.net.crypto.AESUtil;
import com.yywspace.module_base.net.crypto.RSAUtil;
import com.yywspace.module_base.path.RouterPath;
import com.yywspace.module_base.util.LogUtils;


import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ServerUtils.getCommonApi().getPublicKey().observe(this, stringBaseResponse -> {
            new Handler().postDelayed(() -> {
                RSAUtil.PUBLIC_KEY = stringBaseResponse.getData();
                // 产生AES Key
                try {
                    AESUtil.SECRET_KEY = AESUtil.generateSecretKey(UUID.randomUUID().toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                LogUtils.d("RSAUtil.PUBLIC_KEY:" + RSAUtil.PUBLIC_KEY);
                // 打开界面
                ARouter.getInstance().build(RouterPath.LOGIN_PATH).navigation();
                // 取消界面动画
                overridePendingTransition(0, 0);
                finish();
            }, 500);
        });
    }
}
