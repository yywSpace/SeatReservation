package com.yywspace.module_feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yywspace.module_base.TestFragment;
import com.yywspace.module_base.path.RouterPath;
import com.yywspace.module_feedback.databinding.FeedbackActivityBinding;

@Route(path = RouterPath.FEEDBACK_PATH)
public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedbackActivityBinding  binding = FeedbackActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //添加fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fragment, TestFragment.newInstance(RouterPath.FEEDBACK_PATH));
        tx.commit();
    }
}
