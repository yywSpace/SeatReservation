package com.yywspace.module_reserve.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.howshea.roundcornerimageview.RoundCornerImageView
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.adapter.RoomListAdapter
import com.yywspace.module_reserve.databinding.ReserveRoomListBinding
import com.yywspace.module_reserve.iview.IRoomListView
import com.yywspace.module_reserve.presenter.RoomListPresenter

class RoomListActivity : BaseActivity<IRoomListView, RoomListPresenter>(), IRoomListView {
    private lateinit var binding: ReserveRoomListBinding
    lateinit var roomListAdapter: RoomListAdapter


    override fun getLayout(inflater: LayoutInflater): View {
        binding = ReserveRoomListBinding.inflate(inflater)
        return binding.root
    }

    override fun createPresenter(): RoomListPresenter {
        return RoomListPresenter()
    }

    override fun createView(): IRoomListView {
        return this
    }

    override fun init() {
        val floorName = intent.getStringExtra("floor")
        val organizationName = intent.getStringExtra("organization_name")
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar?.title = "$organizationName-$floorName"
        roomListAdapter = RoomListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val shareImg = view.findViewById<RoundCornerImageView>(R.id.room_image)
                val intent = Intent(this@RoomListActivity, RoomDetailActivity::class.java)
                intent.putExtra("room", adapter.data[position] as Room)
                val bundle: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@RoomListActivity,
                        shareImg,
                        getString(R.string.reserve_room_transition_name)
                ).toBundle()
                startActivity(intent, bundle)
                Toast.makeText(this@RoomListActivity, "${adapter.data[position]}", Toast.LENGTH_SHORT).show();
            }
        }
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                presenter.getRoomList(this@RoomListActivity)
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoomListActivity)
            adapter = roomListAdapter
            addItemDecoration(DividerItemDecoration(this@RoomListActivity, DividerItemDecoration.VERTICAL))
        }
        roomListAdapter.setEmptyView(R.layout.reserve_loading_view)
        presenter.getRoomList(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reserve_menu_room, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            R.id.plan_view -> {
                Toast.makeText(baseContext, "plan_view", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun getRoomListResult(roomList: List<Room>?) {
        Handler().postDelayed({
            if (roomList == null)
                roomListAdapter.setEmptyView(R.layout.reserve_empty_view)
            else {
                roomListAdapter.setNewInstance(roomList.toMutableList())
                Toast.makeText(baseContext, "${roomList.size}", Toast.LENGTH_SHORT).show();
            }

            binding.swipeRefreshLayout.isRefreshing = false;
        }, 500)
    }
}
