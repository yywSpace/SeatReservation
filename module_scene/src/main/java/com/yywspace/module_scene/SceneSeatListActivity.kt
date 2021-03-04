package com.yywspace.module_scene

import android.Manifest
import android.graphics.RectF
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.input
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.scene.Floor
import com.yywspace.module_base.bean.scene.Room
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_scene.adapter.SeatListAdapter
import com.yywspace.module_scene.databinding.SceneSeatEditDialogBinding
import com.yywspace.module_scene.databinding.SceneSeatListActivityBinding
import com.yywspace.module_scene.iview.ISeatListView
import com.yywspace.module_scene.presenter.SeatListPresenter
import com.yywspace.module_scene.view.RectShape
import kotlin.random.Random


class SceneSeatListActivity : BaseActivity<ISeatListView, SeatListPresenter>(), ISeatListView {
    lateinit var binding: SceneSeatListActivityBinding
    private lateinit var seatListAdapter: SeatListAdapter
    var seatStatus = -1
    private var roomId: Int = -1


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scene_seat_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.seat_add -> {
                presenter.insertSeat(this,
                        Seat(-1, roomId, "请修改座位名称", "正常", "天生我材必有用，千金散尽还复来。", 0))
            }
            R.id.seat_multi_add -> {
                MaterialDialog(this).show {
                    title(text = "批量添加楼层数量")
                    input(allowEmpty = false, inputType = InputType.TYPE_CLASS_NUMBER) { dialog, text ->
                        val num = text.toString().toInt()
                        for (i in 0 until num) {
                            val seat = Seat(-1, roomId, "请修改座位名称", "正常", "天生我材必有用，千金散尽还复来。", 0)
                            presenter.insertSeat(this@SceneSeatListActivity, seat)
                        }
                    }
                    positiveButton(R.string.base_dialog_confirm)
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun getLayout(inflater: LayoutInflater): View {
        binding = SceneSeatListActivityBinding.inflate(inflater)
        return binding.root
    }

    override fun createPresenter(): SeatListPresenter {
        return SeatListPresenter()
    }

    override fun createView(): ISeatListView {
        return this
    }

    override fun init() {
        roomId = intent.getIntExtra("room_id", 1)
        setSupportActionBar(binding.sceneToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar?.title = "添加座位"
        seatListAdapter = SeatListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val seat = adapter.data[position] as Seat
                seatStatus = seat.seatStatus
                val seatEditDialog = MaterialDialog(view.context).apply {
                    customView(R.layout.scene_seat_edit_dialog)
                }
                val binding = SceneSeatEditDialogBinding.bind(seatEditDialog.getCustomView())
                binding.seatName.editText?.setText(seat.seatName)
                binding.seatDesc.editText?.setText(seat.seatDesc)
                binding.seatStatus.apply {
                    setSelection(seatStatus)
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            seatStatus = position
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                }
                seatEditDialog.show {
                    positiveButton(R.string.base_dialog_confirm) {
                        seat.seatStatus = seatStatus
                        seat.seatName = binding.seatName.editText?.text.toString()
                        seat.seatDesc = binding.seatDesc.editText?.text.toString()
                        presenter.updateSeat(this@SceneSeatListActivity, seat)
                    }
                    negativeButton(R.string.base_dialog_cancel)
                }

            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SceneSeatListActivity)
            adapter = seatListAdapter
            addItemDecoration(DividerItemDecoration(this@SceneSeatListActivity, DividerItemDecoration.VERTICAL))
        }
        seatListAdapter.setEmptyView(R.layout.base_loading_view)
        presenter.getSeatList(this, roomId)
    }

    override fun getSeatListResult(seatList: List<Seat>?) {
        if (seatList == null || seatList.isEmpty())
            seatListAdapter.setEmptyView(R.layout.base_empty_view)
        else {
            seatListAdapter.setNewInstance(seatList.toMutableList())
        }
    }

    override fun updateSeatResult(response: BaseResponse<Any>, seat: Seat) {
        if (response.code == 1) {
            seatListAdapter.notifyDataSetChanged()
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun insertSeatResult(response: BaseResponse<Int>, seat: Seat) {
        if (response.code == 1) {
            seat.id = response.data
            seatListAdapter.addData(seat)
            seatListAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        }
    }

    override fun deleteSeatResult(response: BaseResponse<Any>, seat: Seat) {
        TODO("Not yet implemented")
    }


}