package com.yywspace.module_scene

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.model.FloorModel
import com.yywspace.module_base.model.OrganizationModel
import com.yywspace.module_base.model.RoomModel
import com.yywspace.module_scene.adapter.NodeTreeAdapter
import com.yywspace.module_scene.adapter.WrapContentLinearLayoutManager
import com.yywspace.module_scene.databinding.SceneActivityCreateBinding


class SceneMainActivity : AppCompatActivity() {
    private val nodeTreeAdapter = NodeTreeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SceneActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.sceneToolBar)

        nodeTreeAdapter.addChildClickViewIds(R.id.org_setting)
        // 设置子控件点击监听
        nodeTreeAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.org_setting) {
                Toast.makeText(view.context, "org_setting", Toast.LENGTH_SHORT).show();
            }
        }

        binding.recyclerView.apply {
            adapter = nodeTreeAdapter
            layoutManager = WrapContentLinearLayoutManager(this@SceneMainActivity)
            addItemDecoration(DividerItemDecoration(this@SceneMainActivity, DividerItemDecoration.HORIZONTAL))
        }
        nodeTreeAdapter.setList(getEntity())
    }

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
                nodeTreeAdapter.addData(org)
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

    private fun getEntity(): List<BaseNode> {
        val list: MutableList<BaseExpandNode> = OrganizationModel.getLocalOrganizationList().toMutableList()
        list.forEach { org ->
            org.isExpanded = false
            org.childNode?.addAll(FloorModel.getLocalFloorList())
            org.childNode?.forEach { floor ->
                (floor as BaseExpandNode).isExpanded = false
                floor.childNode?.addAll(RoomModel.getLocalRoomList())
            }
        }
        return list
    }
}