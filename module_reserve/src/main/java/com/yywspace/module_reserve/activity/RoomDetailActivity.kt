package com.yywspace.module_reserve.activity

import android.graphics.Color
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.adapter.SeatListAdapter
import com.yywspace.module_reserve.databinding.ReserveRoomDetailBinding
import com.yywspace.module_reserve.iview.ISeatListView
import com.yywspace.module_reserve.presenter.SeatListPresenter
import java.util.stream.Collectors

class RoomDetailActivity : BaseActivity<ISeatListView, SeatListPresenter>(), ISeatListView {
    private lateinit var binding: ReserveRoomDetailBinding
    private lateinit var seatListAdapter: SeatListAdapter
    lateinit var filterListAdapter: SeatListAdapter

    override fun getLayout(inflater: LayoutInflater): View {
        binding = ReserveRoomDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createPresenter(): SeatListPresenter {
        return SeatListPresenter()
    }

    override fun createView(): ISeatListView {
        return this
    }

    override fun init() {
        setContentView(binding.root)
        val room = intent.getParcelableExtra<Room>("room") ?: return
        binding.reverseToolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(binding.reverseToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar?.title = room.roomName
        binding.expandRoomDesc.text =
                Html.fromHtml(room.roomDesc, Html.FROM_HTML_MODE_LEGACY)
        initAdapter()
        binding.floorLocation.apply {
            text = room.roomName
            setOnClickListener {
                Toast.makeText(this@RoomDetailActivity, "${text}", Toast.LENGTH_SHORT).show();
            }
        }
        binding.roomPersonCount.apply {
            text = "${room.totalSeats - room.emptySeats}/${room.totalSeats}"
            setOnClickListener {
                Toast.makeText(this@RoomDetailActivity, "${text}", Toast.LENGTH_SHORT).show();
            }
        }
//        binding.swipeRefreshLayout.apply {
//            setOnRefreshListener {
//                presenter.getSeatList(this@RoomDetailActivity)
//            }
//        }

        binding.roomRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoomDetailActivity)
            adapter = seatListAdapter
            addItemDecoration(DividerItemDecoration(this@RoomDetailActivity, DividerItemDecoration.VERTICAL))
        }
        presenter.getSeatList(this@RoomDetailActivity, room.id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reserve_menu_seat, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
            }
            R.id.filter_busy -> {
                item.isChecked = true
                val busySeats = seatListAdapter.data.stream().filter {
                    it.seatStatus == 1
                }.collect(Collectors.toList()).toMutableList()
                filterListAdapter.setNewInstance(busySeats)
                binding.roomRecyclerView.adapter = filterListAdapter
            }
            R.id.filter_empty -> {
                item.isChecked = true
                val emptySeats = seatListAdapter.data.stream().filter {
                    it.seatStatus == 0
                }.collect(Collectors.toList()).toMutableList()
                filterListAdapter.setNewInstance(emptySeats)
                binding.roomRecyclerView.adapter = filterListAdapter
            }
            R.id.filter_none -> {
                item.isChecked = true
                binding.roomRecyclerView.adapter = seatListAdapter
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        binding.roomRecyclerView.adapter?.notifyDataSetChanged()
        return true;
    }

    private fun showSeatInfoDialog(seat: Seat) {
        val dialog = MaterialDialog(this).apply {
            title(text = "座位信息")
            customView(R.layout.reserve_room_seat_info)
        }
        val customView = dialog.getCustomView()
        customView.findViewById<TextView>(R.id.seat_name).text = seat.seatName
        customView.findViewById<TextView>(R.id.seat_desc).text = seat.seatDesc
        customView.findViewById<TextView>(R.id.seat_status).apply {
            if (seat.seatStatus == 1) {
                text = "繁忙"
                setTextColor(Color.RED)
            } else if (seat.seatStatus == 0) {
                text = "空闲"
                setTextColor(Color.BLUE)
            }
        }
        customView.findViewById<TextView>(R.id.seat_message).text = seat.seatMsg
        dialog.show {
            noAutoDismiss()
            if (seat.seatStatus != 1) {
                positiveButton(R.string.reserve_select_seat_btn) {
                    // TODO: 21-1-31 联网 
                    dismiss()
                }
            }
            negativeButton(R.string.reserve_select_cal) { dismiss() }
        }
    }

    private fun initAdapter() {
        seatListAdapter = SeatListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                seatListAdapter.setEmptyView(R.layout.base_loading_view)
                showSeatInfoDialog(adapter.data[position] as Seat)
            }
        }
        filterListAdapter = SeatListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                showSeatInfoDialog(adapter.data[position] as Seat)
            }
        }
    }

    override fun getSeatListResult(seatList: List<Seat>?) {
        if (seatList == null)
            seatListAdapter.setEmptyView(R.layout.base_empty_view)
        else {
            seatListAdapter.setNewInstance(seatList.toMutableList())
            Toast.makeText(baseContext, "${seatList.size}", Toast.LENGTH_SHORT).show();
        }
//            binding.swipeRefreshLayout.isRefreshing = false;
    }
}