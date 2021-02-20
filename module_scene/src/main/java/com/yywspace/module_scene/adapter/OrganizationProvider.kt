package com.yywspace.module_scene.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.yywspace.module_base.GlideEngine
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_scene.MapLocationSelectDialogFragment
import com.yywspace.module_scene.MapShowDialogFragment
import com.yywspace.module_scene.R
import com.yywspace.module_scene.databinding.SceneOrgEditDialogBinding
import org.jetbrains.annotations.NotNull

class OrganizationProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.scene_tree_item_org

    override fun convert(@NotNull helper: BaseViewHolder, @NotNull item: BaseNode) {
        val organization: Organization = item as Organization
        helper.setText(R.id.org_title, organization.name)
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                convert(helper, item)
            }
        }
    }

    override fun onLongClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int): Boolean {
        val optionMenuDialog = MaterialDialog(view.context).apply {
            customView(R.layout.scene_tree_item_dialog)
        }

        val customView = optionMenuDialog.getCustomView()
        customView.findViewById<TextView>(R.id.scene_org_edit).setOnClickListener {
            orgEditDialogShow(view.context, data)
            optionMenuDialog.dismiss()
        }
        customView.findViewById<TextView>(R.id.scene_org_delete).setOnClickListener {
            getAdapter()?.remove(data)
            optionMenuDialog.dismiss()

        }
        customView.findViewById<TextView>(R.id.scene_child_add).setOnClickListener {
            getAdapter()?.nodeAddData(data, Floor(-1, "请修改楼层信息", 10, 100))
            optionMenuDialog.dismiss()
        }
        optionMenuDialog.show()
        return true
    }


    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()?.expandOrCollapse(position, animate = true, notify = true, parentPayload = NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD);
    }

    private fun orgEditDialogShow(context: Context, data: BaseNode) {
        val organization = data as Organization
        var latLon: LatLonPoint? = null
        val editDialog = MaterialDialog(context).apply {
            title(text = "机构编辑")
            customView(R.layout.scene_org_edit_dialog)
        }
        val binding = SceneOrgEditDialogBinding.bind(editDialog.getCustomView())
        binding.orgName.editText?.setText(organization.name)
        binding.orgDesc.editText?.setText(organization.desc)
        binding.orgLocation.text = organization.location.split(":")[0]
        binding.orgLocation.setOnClickListener {
            if (latLon != null) {
                MapShowDialogFragment
                        .newInstance(latLon!!.latitude, latLon!!.longitude, organization.name)
                        .show((context as AppCompatActivity).supportFragmentManager, "map")
            }
        }
        binding.orgMapIcon.setOnClickListener {
            val geocodeSearch = GeocodeSearch(context).apply {
                setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                    override fun onRegeocodeSearched(result: RegeocodeResult?, p1: Int) {
                        val formatAddress = result?.regeocodeAddress?.formatAddress ?: return
                        binding.orgLocation.text = formatAddress
                        organization.location = "${formatAddress}:${result.regeocodeQuery.point.latitude},${result.regeocodeQuery.point.longitude}"
                    }

                    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                    }
                })

            }
            val dialog = MapLocationSelectDialogFragment()
            dialog.onPositiveButtonClick = {
                latLon = LatLonPoint(it!!.latitude, it.longitude)
                val query = RegeocodeQuery(latLon, 50f, GeocodeSearch.AMAP)
                geocodeSearch.getFromLocationAsyn(query)
            }
            dialog.show((context as AppCompatActivity).supportFragmentManager, "location")
        }
        binding.orgImage.setOnClickListener {
            PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofAll())
                    .selectionMode(PictureConfig.SINGLE)
                    .isEnableCrop(true)
                    .cropImageWideHigh(200, 200)
                    .withAspectRatio(1, 1)//裁剪比例
                    .rotateEnabled(false)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: List<LocalMedia?>) {
                            // 结果回调
                            if (result.isEmpty())
                                return
                            val media = result[0]
                            val path = if (media?.isCut == true) media.cutPath else media?.path
                            Glide.with(context)
                                    .load(path)
                                    .into(binding.orgImage)
                        }

                        override fun onCancel() {
                            // 取消
                        }
                    })
        }
        editDialog.show {
            noAutoDismiss()
            positiveButton(R.string.scene_select_btn) {
                if (binding.orgName.editText?.text.toString() == "") {
                    binding.orgName.error = "机构名不可为空"
                    return@positiveButton
                }
                if (binding.orgDesc.editText?.text.toString() == "") {
                    binding.orgDesc.error = "机构描述不可为空"
                    return@positiveButton
                }
                if (binding.orgLocation.text.isEmpty()) {
                    Toast.makeText(context, "请选择机构位置", Toast.LENGTH_SHORT).show()
                    return@positiveButton
                }
                organization.name = binding.orgName.editText?.text.toString()
                organization.desc = binding.orgDesc.editText?.text.toString()
                organization.location = binding.orgLocation.text.toString()
                // TODO: 21-2-19 联网
                dismiss()
            }
            negativeButton(R.string.scene_select_cal) {
                binding.orgImage.background = null
                binding.orgLocation.text = ""
                dismiss()
            }
        }
    }
}