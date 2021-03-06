package com.yywspace.module_mine.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;
import com.yywspace.module_base.TestFragment;
import com.yywspace.module_base.net.ServerUtils;
import com.yywspace.module_base.net.crypto.AESUtil;
import com.yywspace.module_base.net.crypto.RSAUtil;
import com.yywspace.module_base.path.RouterPath;
import com.yywspace.module_base.util.LogUtils;
import com.yywspace.module_mine.R;
import com.yywspace.module_mine.StringAdapter;
import com.yywspace.module_mine.databinding.MineActivityBinding;
import com.yywspace.module_mine.databinding.MineRecyelerViewTestBinding;
import com.yywspace.module_mine.user.UserMineFragment;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .barColor(R.color.mineUserInfoBg)
                .fitsSystemWindows(true)  //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .init();
        MineActivityBinding  binding = MineActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ServerUtils.getCommonApi().getPublicKey().observe(this, stringBaseResponse -> {
            RSAUtil.PUBLIC_KEY = stringBaseResponse.getData();
            // 产生AES Key
            try {
                AESUtil.SECRET_KEY = AESUtil.generateSecretKey(UUID.randomUUID().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            LogUtils.d("RSAUtil.PUBLIC_KEY:"+RSAUtil.PUBLIC_KEY);
        });
        //添加fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fragment, new UserMineFragment());
        tx.commit();
    }
}
