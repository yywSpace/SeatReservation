package com.yywspace.module_scene

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_scene.adapter.NodeTreeAdapter
import com.yywspace.module_scene.adapter.WrapContentLinearLayoutManager
import com.yywspace.module_scene.databinding.SceneActivityCreateBinding
import com.yywspace.module_scene.iview.IOrganizationMainView
import com.yywspace.module_scene.presenter.OrganizationMainPresenter


class SceneMainActivity : BaseActivity<IOrganizationMainView, OrganizationMainPresenter>(), IOrganizationMainView {
    lateinit var binding: SceneActivityCreateBinding
    private lateinit var nodeTreeAdapter: NodeTreeAdapter
    private val organizationList: MutableList<Organization> = mutableListOf()
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scene_org_create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.scene_add -> {
                val org = Organization(-1, "请修改机构名称", "请修改机构位置",
                        "请修改机构描述",
                        "", 0, 0)
                presenter.insertOrganization(this, org)
                true
            }
            R.id.scene_export -> {

                true
            }
            R.id.scene_import -> {

                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun getLayout(inflater: LayoutInflater): View {
        binding = SceneActivityCreateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createPresenter(): OrganizationMainPresenter {
        return OrganizationMainPresenter()
    }

    override fun createView(): IOrganizationMainView {
        return this
    }

    override fun init() {
        setSupportActionBar(binding.sceneToolBar)
        nodeTreeAdapter = NodeTreeAdapter(this)
        nodeTreeAdapter.addChildClickViewIds(R.id.org_setting)
        // 设置子控件点击监听
        nodeTreeAdapter.setOnItemChildClickListener { adapter, view, position ->
            val organization = adapter.data[position] as Organization
            LogUtils.d(organization.toString())
            if (view.id == R.id.org_setting) {
                MaterialDialog(this).show {
                    title(text = "请选择机构状态")
                    val initialSelection = if (organization.isActivate) 0 else 1
                    listItemsSingleChoice(R.array.scene_org_status_array, initialSelection = initialSelection) { dialog, index, text ->
                        organization.isActivate = index == 0
                        presenter.updateOrganization(this@SceneMainActivity, organization)
                        Toast.makeText(view.context, text, Toast.LENGTH_SHORT).show()
                    }
                    positiveButton(R.string.base_dialog_confirm)
                }
            }
        }
        binding.recyclerView.apply {
            adapter = nodeTreeAdapter
            layoutManager = WrapContentLinearLayoutManager(this@SceneMainActivity)
            addItemDecoration(DividerItemDecoration(this@SceneMainActivity, DividerItemDecoration.HORIZONTAL))
        }
        nodeTreeAdapter.setEmptyView(R.layout.base_loading_view)

        presenter.getOrganizationListByGroup(this, "group")
    }

    override fun getOrganizationListResult(organizationList: List<Organization>?) {
        if (organizationList == null)
            nodeTreeAdapter.setEmptyView(R.layout.base_empty_view)
        else {
            organizationList.forEach {
                val newOrg = Organization(it.id, it.name, it.location, it.desc, it.imagePath, 0, 0, it.isActivate)
                LogUtils.d(newOrg.toString())
                newOrg.isExpanded = false
                this.organizationList.add(newOrg)
                presenter.getFloorList(this, newOrg)
            }
        }
    }

    override fun getFloorListResult(floorList: List<Floor>?, organization: Organization) {
        if (floorList != null) {
            LogUtils.d("floorList ${floorList.size}")
            floorList.forEach {
                val floor = Floor(it.id, it.organizationId, it.floorName)
                floor.isExpanded = false
                organization.childNode.add(floor)
                LogUtils.d(floor.toString())
                presenter.getRoomList(this, floor)
            }
        }
    }

    override fun getRoomListResult(roomList: List<Room>?, floor: Floor) {
        if (roomList != null) {
            LogUtils.d(roomList.toString())
            roomList.forEach {
                val room = Room(it.id, it.floorId, it.roomName, it.roomDesc, it.roomLocation,
                        it.totalSeats, it.emptySeats, it.roomImagePath)
                floor.childNode.add(room)
            }
        }
        nodeTreeAdapter.setList(this.organizationList)
    }

    override fun updateOrganizationResult(response: BaseResponse<Any>) {
        if (response.code == 1) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun insertOrganizationResult(response: BaseResponse<Int>, organization: Organization) {
        if (response.code == 1) {
            organization.id = response.data
            nodeTreeAdapter.addData(organization)
        } else {
            Toast.makeText(baseContext, "插入失败", Toast.LENGTH_SHORT).show();
        }
    }

}