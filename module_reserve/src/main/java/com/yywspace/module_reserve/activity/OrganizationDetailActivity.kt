package com.yywspace.module_reserve.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.adapter.FloorListAdapter
import com.yywspace.module_reserve.databinding.ReserveActivityOrganizationDetailBinding
import com.yywspace.module_reserve.iview.IFloorListView
import com.yywspace.module_reserve.presenter.FloorListPresenter

@Route(path = RouterPath.ORG_DETAIL_PATH)
class OrganizationDetailActivity : BaseActivity<IFloorListView, FloorListPresenter>(), IFloorListView {
    lateinit var binding: ReserveActivityOrganizationDetailBinding
    lateinit var floorListAdapter:FloorListAdapter

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun getLayout(inflater: LayoutInflater): View {
        binding = ReserveActivityOrganizationDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createPresenter(): FloorListPresenter {
        return FloorListPresenter()
    }

    override fun createView(): IFloorListView {
        return this
    }

    override fun init() {
        val organization = intent.getParcelableExtra<Organization>("organization") ?: return
        binding.reverseToolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(binding.reverseToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar?.title = organization.name

        Glide.with(this)
                .load(AppConfig.BASE_URL + "upload/" + organization.imagePath)
                .placeholder(R.drawable.ic_bg)
                .error(R.drawable.ic_bg)
                .into(binding.organizationImage)

        binding.expandOrganizationDesc.text =
                Html.fromHtml(organization.desc ?: "", Html.FROM_HTML_MODE_LEGACY)
        binding.organizationLocation.apply {
            text = organization.name
            setOnClickListener {
                Toast.makeText(this@OrganizationDetailActivity, "${text}", Toast.LENGTH_SHORT).show();
            }
        }
        binding.organizationPersonCount.apply {
            text = "${organization.totalSeats!! - organization.emptySeats!!}/${organization.totalSeats}"
            setOnClickListener {
                Toast.makeText(this@OrganizationDetailActivity, "${text}", Toast.LENGTH_SHORT).show();
            }
        }
         floorListAdapter = FloorListAdapter().apply {
             setEmptyView(R.layout.base_loading_view)
             setOnItemClickListener { adapter, view, position ->
                val intent = Intent(this@OrganizationDetailActivity, RoomListActivity::class.java)
                intent.putExtra("floor", adapter.data[position] as Floor)
                intent.putExtra("organization_name", organization.name)
                startActivity(intent)
            }
        }
        binding.floorRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrganizationDetailActivity)
            adapter = floorListAdapter
            addItemDecoration(DividerItemDecoration(this@OrganizationDetailActivity, DividerItemDecoration.VERTICAL))
        }

        presenter.getFloorList(this, organization.id)
    }

    override fun getFloorListResult(floorList: List<Floor>?) {
        if (floorList == null)
            floorListAdapter.setEmptyView(R.layout.base_empty_view)
        else
            floorListAdapter.setNewInstance(floorList.toMutableList())
    }
}