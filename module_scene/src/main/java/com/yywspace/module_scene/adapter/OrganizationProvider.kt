package com.yywspace.module_scene.adapter

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.input
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
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.GlideEngine
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_scene.MapLocationSelectDialogFragment
import com.yywspace.module_scene.MapShowDialogFragment
import com.yywspace.module_scene.R
import com.yywspace.module_scene.databinding.SceneOrgEditDialogBinding
import com.yywspace.module_scene.iview.IOrganizationProviderView
import com.yywspace.module_scene.presenter.OrganizationProviderPresenter
import org.jetbrains.annotations.NotNull

class OrganizationProvider(private val activity: AppCompatActivity) : BaseNodeProvider(), IOrganizationProviderView {
    private val presenter: OrganizationProviderPresenter = OrganizationProviderPresenter()
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.scene_tree_item_org

    init {
        presenter.attachView(this)
    }

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
        val org = data as Organization
        val optionMenuDialog = MaterialDialog(view.context).apply {
            customView(R.layout.scene_tree_item_dialog)
        }

        val customView = optionMenuDialog.getCustomView()
        customView.findViewById<TextView>(R.id.scene_org_edit).setOnClickListener {
            orgEditDialogShow(view.context, data, position)
            optionMenuDialog.dismiss()
        }
        customView.findViewById<TextView>(R.id.scene_org_delete).setOnClickListener {
            presenter.deleteOrganization(activity, org)
            optionMenuDialog.dismiss()
        }

        customView.findViewById<TextView>(R.id.scene_child_add).setOnClickListener {
            val floor = Floor(-1, org.id, "请修改楼层信息", 0, 0)
            presenter.insertFloor(activity, floor, org)
            optionMenuDialog.dismiss()
        }
        customView.findViewById<TextView>(R.id.scene_child_multi_add).setOnClickListener {
            MaterialDialog(context).show {
                title(text = "批量添加楼层数量")
                input(allowEmpty = false, inputType = InputType.TYPE_CLASS_NUMBER) { dialog, text ->
                    val num = text.toString().toInt()
                    for (i in 0 until num) {
                        val floor = Floor(-1, org.id, "请修改楼层信息", 0, 0)
                        presenter.insertFloor(activity, floor, data)
                    }
                    optionMenuDialog.dismiss()
                }
                positiveButton(R.string.base_dialog_confirm)
                negativeButton(R.string.base_dialog_cancel)
            }
        }
        optionMenuDialog.show()
        return true
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()?.expandOrCollapse(position, animate = true, notify = true, parentPayload = NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD);
    }

    private fun orgEditDialogShow(context: Context, data: BaseNode, position: Int) {
        val organization = data as Organization
        var latLon: LatLonPoint? = null
        if (organization.location.isNotEmpty()) {
            val locationArray = organization.location.split(":")
            if (locationArray.size > 1) {
                val latLonArray = locationArray[1].split(",")
                latLon = LatLonPoint(latLonArray[0].toDouble(), latLonArray[1].toDouble())
            }
        }
        var imagePath: String? = null
        var location: String? = null
        val editDialog = MaterialDialog(context).apply {
            title(text = "机构编辑")
            customView(R.layout.scene_org_edit_dialog)
        }
        val binding = SceneOrgEditDialogBinding.bind(editDialog.getCustomView())
        Glide.with(context)
                .load(AppConfig.BASE_URL + "upload/" + organization.imagePath)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(binding.orgImage)
        binding.orgName.editText?.setText(organization.name)
        binding.orgDesc.editText?.setText(organization.desc)
        binding.orgLocation.text = organization.location.split(":")[0]
        binding.orgLocation.setOnClickListener {
            if (latLon != null) {
                MapShowDialogFragment
                        .newInstance(latLon!!.latitude, latLon!!.longitude, organization.name)
                        .show(activity.supportFragmentManager, "map")
            }
        }
        binding.orgMapIcon.setOnClickListener {
            val geocodeSearch = GeocodeSearch(context).apply {
                setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                    override fun onRegeocodeSearched(result: RegeocodeResult?, p1: Int) {
                        val formatAddress = result?.regeocodeAddress?.formatAddress ?: return
                        binding.orgLocation.text = formatAddress
                        location = "${formatAddress}:${result.regeocodeQuery.point.latitude},${result.regeocodeQuery.point.longitude}"
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
            dialog.show(activity.supportFragmentManager, "location")
        }

        binding.orgImage.setOnClickListener {
            PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofAll())
                    .selectionMode(PictureConfig.SINGLE)
                    .isEnableCrop(true)
                    .cropImageWideHigh(200, 200)
                    .withAspectRatio(1, 1) //裁剪比例
                    .rotateEnabled(false)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: List<LocalMedia?>) {
                            // 结果回调
                            if (result.isEmpty()) return
                            val media = result[0]
                            val path = if (media?.isCut == true) media.cutPath else media?.path
                            imagePath = path
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
                organization.location = location.toString()
                if (imagePath != null) {
                    presenter.uploadFile(activity, imagePath!!, organization)
                }
                presenter.updateOrganization(activity, organization)
                getAdapter()?.notifyItemChanged(position)
                dismiss()
            }
            negativeButton(R.string.scene_select_cal) {
                binding.orgImage.background = null
                binding.orgLocation.text = ""
                dismiss()
            }
        }
    }

    override fun insertFloorResult(response: BaseResponse<Int>, floor: Floor, organization: Organization) {
        if (response.code == 1) {
            floor.id = response.data
            getAdapter()?.nodeAddData(organization, floor)
        } else {
            Toast.makeText(context, "插入失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun updateOrganizationResult(response: BaseResponse<Any>) {
        if (response.code == 1) {
            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun deleteOrganizationResult(response: BaseResponse<Any>, organization: Organization) {
        if (response.code == 1) {
            getAdapter()?.remove(organization)
        } else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun uploadFileResult(response: BaseResponse<String>, organization: Organization) {
        if (response.code == 1) {
            organization.imagePath = response.data
            presenter.updateOrganization(activity, organization)
        } else
            Toast.makeText(context, "图像上传失败", Toast.LENGTH_SHORT).show();
    }
}