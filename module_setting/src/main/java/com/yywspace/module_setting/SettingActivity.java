package com.yywspace.module_setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yywspace.module_base.TestFragment;
import com.yywspace.module_base.path.RouterPath;
import com.yywspace.module_setting.databinding.SettingActivityBinding;

@Route(path = RouterPath.SETTING_PATH)
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingActivityBinding binding = SettingActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.title.setText(RouterPath.SETTING_PATH);

        //添加fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fragment, TestFragment.newInstance(RouterPath.SETTING_PATH));
        tx.commit();
    }
}
