package com.yywspace.module_home.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.services.core.LatLonPoint
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionFragment
import com.yywspace.module_base.MapShowDialogFragment
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.Setting
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.model.SettingModel
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_base.util.MapUtil
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.statePattern.ReservationContext
import com.yywspace.module_home.IReservationView


@Route(path = RouterPath.HOME_PATH)
class HomeFragment : ImmersionFragment(), IReservationView {
    companion object {
        var latLon: LatLonPoint? = null
        var orgLatLon: LatLonPoint? = null
    }

    private var binding: HomeFragmentMainBinding? = null
    private var locationClient: AMapLocationClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        initLocationClient()
        binding = HomeFragmentMainBinding.inflate(inflater)
        ServerUtils.getCommonApi().getRunningReservation(1)
                .observe(requireActivity(), Observer { reservation ->
                    if (reservation == null || reservation.data == null) {
                        Toast.makeText(requireContext(), "当前无任何预约", Toast.LENGTH_SHORT).show()
                        refreshLayout()
                        return@Observer
                    }
                    Reservation.runningReservation = reservation.data
                    ServerUtils.getCommonApi().getOrganizationBySeatId(reservation.data.seatId)
                            .observe(requireActivity(), Observer { seatRes ->
                                Reservation.runningOrganization = seatRes.data
                                if (seatRes.data.location.isNotEmpty()) {
                                    val locationArray = seatRes.data.location.split(":")
                                    if (locationArray.size > 1) {
                                        val latLonArray = locationArray[1].split(",")
                                        orgLatLon = LatLonPoint(latLonArray[0].toDouble(), latLonArray[1].toDouble())
                                    }
                                }
                                LogUtils.d("getOrganizationBySeatId")
                                SettingModel.getSettingByUserName(seatRes?.data?.group.toString()).observe(
                                        requireActivity(), Observer {
                                    Setting.setting = it.data
                                    when (reservation.data.status) {  // 0 running 1 success 2 fal 3 签到 4 暂离
                                        0 -> {
                                            ReservationContext.currentState = ReservationContext.reservationState
                                        }
                                        3 -> {
                                            ReservationContext.currentState = ReservationContext.signInReservationState
                                        }
                                        4 -> {
                                            ReservationContext.currentState = ReservationContext.afkReservationState
                                        }
                                    }
                                    refreshLayout()
                                })


                            })

                })
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
        ReservationContext.handleView(binding!!, requireActivity().layoutInflater, this, requireActivity())
    }

    private fun initLocationClient() {
        locationClient?.onDestroy()
        //初始化定位
        locationClient = AMapLocationClient(requireActivity().applicationContext)
                .apply {
                    //设置定位回调监听
                    setLocationListener { aMapLoc ->
                        if (aMapLoc.errorCode == 0) {
                            LogUtils.d(aMapLoc.toStr())
                            latLon = LatLonPoint(aMapLoc.latitude, aMapLoc.longitude)
                            if (orgLatLon != null) {
                                if (MapUtil.distance(latLon!!.latitude, latLon!!.longitude, orgLatLon!!.latitude, orgLatLon!!.longitude) > 100) {
                                    binding?.homeWarnText?.setTextColor(Color.RED)
                                    binding?.homeWarnText?.text = "超出范围"
                                } else {
                                    binding?.homeWarnText?.setTextColor(Color.GREEN)
                                    binding?.homeWarnText?.text = "当前状态正常"
                                }
                            }
                        } else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError", "location Error, ErrCode:"
                                    + aMapLoc.errorCode + ", errInfo:"
                                    + aMapLoc.errorInfo)
                        }
                    }
                    val locationOption = AMapLocationClientOption().apply {
                        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
                        locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Sport
                    }
                    setLocationOption(locationOption)
                    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                    stopLocation()
                    startLocation()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient?.onDestroy()
    }
}

