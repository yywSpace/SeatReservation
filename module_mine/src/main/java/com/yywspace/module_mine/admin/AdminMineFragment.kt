package com.yywspace.module_mine.admin

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.base.BaseFragment
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineAdminFragmentLayoutBinding
import com.yywspace.module_mine.databinding.MineUserFragmentLayoutBinding
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.presenter.UserInfoPresenter
import com.yywspace.module_mine.user.UserInfoItem
import com.yywspace.module_mine.user.activity.*
import com.yywspace.module_mine.user.adapter.UserInfItemListAdapter

@Route(path = RouterPath.MINE_ADMIN_PATH)
class AdminMineFragment : BaseFragment<IUserInfoView, UserInfoPresenter>(), IUserInfoView {
    var user: User? = null
    lateinit var binding: MineAdminFragmentLayoutBinding
    override fun getLayout(inflater: LayoutInflater): View {
        binding = MineAdminFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun createPresenter(): UserInfoPresenter {
        return UserInfoPresenter()
    }

    override fun createView(): IUserInfoView {
        return this
    }

    override fun init() {
        user = User.currentUser
        initView(user!!)
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .commit()
    }

    private fun initView(user: User) {
        this.user = user
        LogUtils.d(AppConfig.BASE_URL + "upload/" + user.avatarPath)
        Glide.with(requireContext())
                .load(AppConfig.BASE_URL + "upload/" + user.avatarPath)
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .into(binding.userAvatar)
        binding.userName.text = user.username
    }

    override fun getUserInfoResult(user: User?) {
    }

}