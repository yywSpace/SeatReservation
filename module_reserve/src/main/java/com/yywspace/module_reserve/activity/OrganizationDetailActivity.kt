package com.yywspace.module_reserve.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.adapter.FloorListAdapter
import com.yywspace.module_reserve.databinding.ReserveActivityOrganizationDetailBinding

class OrganizationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReserveActivityOrganizationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val organization = intent.getParcelableExtra<Organization>("organization")
        binding.reverseToolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(binding.reverseToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar?.title = organization?.name
        binding.expandOrganizationDesc.text =
                Html.fromHtml(organization?.desc ?: "" , Html.FROM_HTML_MODE_LEGACY)
        binding.organizationLocation.apply {
            text = organization?.name
            setOnClickListener {
                Toast.makeText(this@OrganizationDetailActivity, "${text}", Toast.LENGTH_SHORT).show();
            }
        }
        binding.organizationPersonCount.apply {
            text = "${organization?.totalSeats!! - organization?.emptySeats!!}/${organization?.totalSeats}"
            setOnClickListener {
                Toast.makeText(this@OrganizationDetailActivity, "${text}", Toast.LENGTH_SHORT).show();
            }
        }
        val floorListAdapter = FloorListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val intent = Intent(this@OrganizationDetailActivity, RoomListActivity::class.java)
                intent.putExtra("floor", adapter.data[position] as String)
                intent.putExtra("organization_name", organization?.name)
                startActivity(intent)
            }
        }
        binding.floorRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrganizationDetailActivity)
            adapter = floorListAdapter
            addItemDecoration(DividerItemDecoration(this@OrganizationDetailActivity, DividerItemDecoration.VERTICAL))
        }
        floorListAdapter.setNewInstance(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
    }

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
}