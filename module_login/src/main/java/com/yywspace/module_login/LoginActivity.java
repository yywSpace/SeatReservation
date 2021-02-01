package com.yywspace.module_login;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;
import com.yywspace.module_base.net.ServerUtils;
import com.yywspace.module_base.net.crypto.AESUtil;
import com.yywspace.module_base.net.crypto.RSAUtil;
import com.yywspace.module_base.path.RouterPath;
import com.yywspace.module_base.util.LogUtils;
import com.yywspace.module_login.adapter.LoginFragmentAdapter;
import com.yywspace.module_login.databinding.LoginActivityBinding;
import com.yywspace.module_login.fragment.AdminLoginFragment;
import com.yywspace.module_login.fragment.UserLoginFragment;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Route(path = RouterPath.LOGIN_PATH)
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivityBinding binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .init();
        // TODO: 20-11-12 迁移到入口点
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
        binding.page.setAdapter(new LoginFragmentAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                new ArrayList<>(Arrays.asList(new UserLoginFragment(), new AdminLoginFragment())),
                new ArrayList<>(Arrays.asList("用户登录", "管理员登录"))));
        binding.tabLayout.setupWithViewPager(binding.page);
        // tool bar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
