package com.yywspace.module_base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yywspace.module_base.databinding.BaseFragmentBinding;
import com.yywspace.module_base.path.RouterPath;


public class TestFragment extends Fragment {
    private static final String CUR_MODULE = "CUR_MODULE";
    public String mCurModule;
    private BaseFragmentBinding mBinding;

    public static TestFragment newInstance(String curModule) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(CUR_MODULE, curModule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurModule = getArguments().getString(CUR_MODULE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = BaseFragmentBinding.inflate(inflater, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.feedback.setOnClickListener((v) -> {
            if (!mCurModule.equals(RouterPath.FEEDBACK_PATH))
                ARouter.getInstance().build(RouterPath.FEEDBACK_PATH).navigation();
        });
        mBinding.login.setOnClickListener((v) -> {
            if (!mCurModule.equals(RouterPath.LOGIN_PATH))
                ARouter.getInstance().build(RouterPath.LOGIN_PATH).navigation();
        });
        mBinding.mine.setOnClickListener((v) -> {
            if (!mCurModule.equals(RouterPath.MINE_PATH))
                ARouter.getInstance().build(RouterPath.MINE_PATH).navigation();
        });
        mBinding.reserve.setOnClickListener((v) -> {
            if (!mCurModule.equals(RouterPath.RESERVE_PATH))
                ARouter.getInstance().build(RouterPath.RESERVE_PATH).navigation();
        });
        mBinding.scene.setOnClickListener((v) -> {
            if (!mCurModule.equals(RouterPath.SCENE_PATH))
                ARouter.getInstance().build(RouterPath.SCENE_PATH).navigation();
        });
        mBinding.setting.setOnClickListener((v) -> {
            if (!mCurModule.equals(RouterPath.SETTING_PATH))
                ARouter.getInstance().build(RouterPath.SETTING_PATH).navigation();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}