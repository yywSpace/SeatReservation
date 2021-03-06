package com.yywspace.module_base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.MyLocationStyle
import com.yywspace.module_base.util.DensityUtils


class MapLocationSelectDialogFragment : DialogFragment() {
    lateinit var mapView: MapView
    var onPositiveButtonClick: ((LatLng?) -> Unit)? = null
    var marker: Marker? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.base_map_popup_window, null, false)
        mapView = dialogView.findViewById(R.id.map_view)
        mapView.map.apply {
            isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
            MyLocationStyle().apply {
                showMyLocation(true) // 显示圆点
                radiusFillColor(Color.argb(0, 0, 0, 0))
                strokeColor(Color.argb(0, 0, 0, 0))
                myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
                setMyLocationStyle(this) //设置定位蓝点的Style
            }
            uiSettings.isZoomControlsEnabled = false

            setOnMapClickListener {
                marker?.destroy()
                marker = addMarker(MarkerOptions().position(it))
            }
            moveCamera(CameraUpdateFactory.zoomTo(18f))
        }
        mapView.onCreate(savedInstanceState)
        return AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .setTitle("请选择地点")
                .setPositiveButton("确认") { dialogInterface: DialogInterface, whichButton: Int ->
                    onPositiveButtonClick?.invoke(marker?.position)
                }
                .setNegativeButton("取消", null)
                .create()
    }

    override fun onStart() {
        dialog?.window?.attributes?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = DensityUtils.dip2px(context, 600f)
        }
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState);
    }
}