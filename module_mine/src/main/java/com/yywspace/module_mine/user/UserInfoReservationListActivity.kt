package com.yywspace.module_mine.user

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_mine.databinding.MineReservationListActivityBinding
import com.yywspace.module_mine.iview.IReservationListView
import com.yywspace.module_mine.presenter.ReservationListPresenter

class UserInfoReservationListActivity : BaseActivity<IReservationListView, ReservationListPresenter>(), IReservationListView {
    lateinit var binding: MineReservationListActivityBinding
    lateinit var userInfoReservationList: UserReservationListAdapter
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
        userInfoReservationList = UserReservationListAdapter().apply {

        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@UserInfoReservationListActivity)
            adapter = userInfoReservationList
        }
        presenter.getReservationList(this)
    }

    override fun getReservationListResult(reservationList: List<Reservation>?) {
        userInfoReservationList.setNewInstance(reservationList?.toMutableList())
    }
}