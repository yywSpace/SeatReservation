package com.yywspace.module_base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.yywspace.module_base.util.DensityUtils


class MapShowDialogFragment : DialogFragment() {
    lateinit var mapView: MapView
    companion object {
        fun newInstance(lat: Double, lng: Double, message: String): MapShowDialogFragment {
            val dialogFragment = MapShowDialogFragment()
            dialogFragment.arguments = Bundle().apply {
                putDouble("lat", lat)
                putDouble("lng", lng)
                putString("message", message)
            }
            return dialogFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.base_map_popup_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lat = arguments?.get("lat") as Double
        val lng = arguments?.get("lng") as Double
        val orgName = arguments?.get("message") as String
        val latLng = LatLng(lat, lng)
        mapView = view.findViewById(R.id.map_view)
        mapView.map.apply {
            uiSettings.isZoomControlsEnabled = false
            setOnMapLoadedListener {
                //设置中心点和缩放比例
                moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                moveCamera(CameraUpdateFactory.zoomTo(18f))
                // 添加标记点
                addMarker(MarkerOptions().position(latLng).title(orgName))
            }
        }
        mapView.onCreate(savedInstanceState)
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