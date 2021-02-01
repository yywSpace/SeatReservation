package com.yywspace.module_home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionFragment
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.statePattern.ReservationContext
import com.yywspace.module_home.IReservationView


@Route(path = RouterPath.HOME_PATH)
class HomeFragment : ImmersionFragment(), IReservationView {
    private var binding: HomeFragmentMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = HomeFragmentMainBinding.inflate(inflater)
        refreshLayout()
        return binding!!.root
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true) //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
                .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .init()
    }

    override fun refreshLayout() {
        ReservationContext.handleView(binding!!, requireActivity().layoutInflater, this, requireContext())
    }
}