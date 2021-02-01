package com.yywspace.module_login.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class LoginFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList;//各导航的Fragment
    private final List<String> mTitle; //导航的标题

    public LoginFragmentAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragments, List<String> title) {
        super(fm, behavior);
        mFragmentList = fragments;
        mTitle = title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
