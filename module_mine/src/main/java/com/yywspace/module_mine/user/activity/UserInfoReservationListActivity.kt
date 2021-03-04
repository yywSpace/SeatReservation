package com.yywspace.module_mine.user.activity

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.util.TimeUtils
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineReservationListActivityBinding
import com.yywspace.module_mine.databinding.MineUserInfoReservationItemDialogBinding
import com.yywspace.module_mine.iview.IReservationListView
import com.yywspace.module_mine.presenter.ReservationListPresenter
import com.yywspace.module_mine.user.adapter.UserReservationListAdapter
import java.util.stream.Collectors

class UserInfoReservationListActivity : BaseActivity<IReservationListView, ReservationListPresenter>(), IReservationListView {
    lateinit var binding: MineReservationListActivityBinding
    lateinit var userInfoReservationList: UserReservationListAdapter
    lateinit var resultReservationList: UserReservationListAdapter
    var userId = 1

    override fun getLayout(inflater: LayoutInflater): View {
        binding = MineReservationListActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createPresenter(): ReservationListPresenter {
        return ReservationListPresenter()
    }

    override fun createView(): IReservationListView {
        return this
    }

    override fun init() {
        userId = intent.getIntExtra("user_id", 1)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "预约记录"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        initAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@UserInfoReservationListActivity)
            adapter = userInfoReservationList
        }
        presenter.getReservationList(this, userId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mine_reservation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_now -> {
                item.isChecked = true
                val result = userInfoReservationList.data.stream().filter { it.status == 0 }.collect(Collectors.toList())
                resultReservationList.setNewInstance(result.toMutableList())
                binding.recyclerView.adapter = resultReservationList
                true
            }
            R.id.filter_none -> {
                item.isChecked = true
                binding.recyclerView.adapter = userInfoReservationList
                true
            }
            R.id.filter_success -> {
                item.isChecked = true
                val result = userInfoReservationList.data.stream().filter { it.status == 1 }.collect(Collectors.toList())
                resultReservationList.setNewInstance(result.toMutableList())
                binding.recyclerView.adapter = resultReservationList
                true
            }
            R.id.filter_breach -> {
                item.isChecked = true
                val result = userInfoReservationList.data.stream().filter { it.status > 1 }.collect(Collectors.toList())
                resultReservationList.setNewInstance(result.toMutableList())
                binding.recyclerView.adapter = resultReservationList
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initAdapter() {
        userInfoReservationList = UserReservationListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val reservation = adapter.data[position] as Reservation
                MaterialDialog(this@UserInfoReservationListActivity).show {
                    title(text = "预约详情")
                    customView(R.layout.mine_user_info_reservation_item_dialog)
                    val binding = MineUserInfoReservationItemDialogBinding.bind(getCustomView())
                    binding.mineReservationEndTime.text = TimeUtils.longToString(reservation.endTime, TimeUtils.formatPattern)
                    binding.mineReservationStartTime.text = TimeUtils.longToString(reservation.startTime, TimeUtils.formatPattern)
                    binding.mineReservationOrg.text = reservation.location.split("-")[0]
                    binding.mineReservationOrgLoc.text = reservation.location
                    binding.mineReservationSeat.text = reservation.seatName
                    binding.mineReservationStatus.text = when (reservation.status) {
                        0 -> "执行中"
                        1 -> "成功"
                        else -> "违约"
                    }
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
        }
        resultReservationList = UserReservationListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                Toast.makeText(this@UserInfoReservationListActivity, "${adapter.data[position]}", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun getReservationListResult(reservationList: List<Reservation>?) {
        userInfoReservationList.setNewInstance(reservationList?.toMutableList())
    }
}