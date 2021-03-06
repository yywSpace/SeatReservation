package com.yywspace.module_mine.user

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
import com.yywspace.module_mine.databinding.MineUserFragmentLayoutBinding
import com.yywspace.module_mine.iview.IUserInfoView
import com.yywspace.module_mine.presenter.UserInfoPresenter
import com.yywspace.module_mine.user.activity.*
import com.yywspace.module_mine.user.adapter.UserInfItemListAdapter

@Route(path = RouterPath.MINE_PATH)
class UserMineFragment : BaseFragment<IUserInfoView, UserInfoPresenter>(), IUserInfoView {
    var user: User? = null
    lateinit var binding: MineUserFragmentLayoutBinding
    lateinit var itemListAdapter: UserInfItemListAdapter
    override fun getLayout(inflater: LayoutInflater): View {
        binding = MineUserFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun createPresenter(): UserInfoPresenter {
        return UserInfoPresenter()
    }

    override fun createView(): IUserInfoView {
        return this
    }

    override fun init() {
        itemListAdapter = UserInfItemListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->

                when ((adapter.data[position] as UserInfoItem).name) {
                    "机构收藏" -> {
                        startActivity(Intent(requireContext(), UserFavouriteOrgActivity::class.java).apply {
                            putExtra("user_id", user?.id)
                        })
                        Toast.makeText(requireContext(), "机构收藏", Toast.LENGTH_SHORT).show();
                    }
                    "预约记录" -> {
                        startActivity(Intent(requireContext(), UserInfoReservationListActivity::class.java).apply {
                            putExtra("user_id", user?.id)
                        })
                        Toast.makeText(requireContext(), "预约记录", Toast.LENGTH_SHORT).show();
                    }
                    "违约记录" -> {
                        Toast.makeText(requireContext(), "违约记录", Toast.LENGTH_SHORT).show();
                    }
                    "统计概况" -> {
                        startActivity(Intent(requireContext(), UserInfoStatisticAnalysisActivity::class.java).apply {
                            putExtra("user_id", user?.id)
                        })
                        Toast.makeText(requireContext(), "统计概况", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            val itemList = listOf(
                    UserInfoItem(R.drawable.ic_favourite, "机构收藏"),
                    UserInfoItem(R.drawable.ic_time, "预约记录"),
                    UserInfoItem(R.drawable.ic_over_time, "违约记录"),
                    UserInfoItem(R.drawable.ic_statistics, "统计概况"))
            setNewInstance(itemList.toMutableList())
        }
        binding.mineRuleLayout.setOnClickListener {
            startActivity(Intent(requireContext(), ReservationRuleActivity::class.java).apply {
                putExtra("title", "预约规则")
                putExtra("url", "file:///android_asset/预约规则.html")
            })
        }
        binding.mineNotifyLayout.setOnClickListener {
            startActivity(Intent(requireContext(), ReservationRuleActivity::class.java).apply {
                putExtra("title", "通知")
                putExtra("url", "file:///android_asset/版本更新.html")
            })
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemListAdapter
        }
        binding.userInfoLayout.setOnClickListener {
            infoDetailLauncher.launch(
                    Intent(requireContext(), UserInfoDetailActivity::class.java)
                            .apply {
                                putExtra("user", user)
                            })
        }
        presenter.getUserInfo(this)
    }

    private fun initView(user: User?) {
        if (user != null) {
            this.user = user
            LogUtils.d(AppConfig.BASE_URL + "upload/" + user.avatarPath)
            Glide.with(requireContext())
                    .load(AppConfig.BASE_URL + "upload/" + user.avatarPath)
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(binding.userAvatar)
            binding.userName.text = user.username
            binding.userSex.setImageResource(if (user.sex == 0) R.drawable.ic_woman else R.drawable.ic_man)
            binding.userDesc.text = if (user.message == null || user.message == "")
                getString(R.string.mine_user_desc_default)
            else user.message
        } else {
            binding.userName.text = getString(R.string.mine_user_name_not_login)
            binding.userSex.visibility = View.GONE
            binding.userDesc.text = getString(R.string.mine_user_desc_not_login)
            binding.userAvatar.setImageResource(R.drawable.ic_avatar)
        }
    }

    override fun getUserInfoResult(user: User?) {
        initView(user)
    }

    private val infoDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        if (it.resultCode == Activity.RESULT_OK) {
            val user = it.data?.getParcelableExtra<User>("user")
            initView(user)
        }
    }

}