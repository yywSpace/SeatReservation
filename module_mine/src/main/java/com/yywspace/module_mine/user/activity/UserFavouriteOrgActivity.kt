package com.yywspace.module_mine.user.activity

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.howshea.roundcornerimageview.RoundCornerImageView
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.path.RouterPath.ORG_DETAIL_PATH
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineFavourtieOrgActivityBinding
import com.yywspace.module_mine.iview.IFavouriteReservationListView
import com.yywspace.module_mine.presenter.FavouriteReservationListPresenter
import com.yywspace.module_mine.user.adapter.FavouriteOrganizationListAdapter
import java.util.stream.Collectors


class UserFavouriteOrgActivity : BaseActivity<IFavouriteReservationListView, FavouriteReservationListPresenter>(), IFavouriteReservationListView {
    private var binding: MineFavourtieOrgActivityBinding? = null
    private var organizationListAdapter: FavouriteOrganizationListAdapter? = null
    private var resultListAdapter: FavouriteOrganizationListAdapter? = null
    private var userId = -1
    private var isReverse = false
    private var isSortByName = true
    private var isSortByPerson = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reserve_favourite_organization_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding!!.recyclerView.adapter = organizationListAdapter
                return true
            }
        })
        //通过MenuItem得到SearchView
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return onQueryTextChange(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    val searchRes = organizationListAdapter!!.data
                            .stream()
                            .filter {
                                it.name.contains(newText) || it.location.contains(newText)
                            }.collect(Collectors.toList())
                    resultListAdapter?.setNewInstance(searchRes.toMutableList())
                    binding!!.recyclerView.adapter = resultListAdapter
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_name -> {
                isSortByName = true
                isSortByPerson = false
                item.isChecked = true
                return organizationSort()
            }
            R.id.sort_by_person -> {
                isSortByPerson = false
                isSortByPerson = true
                item.isChecked = true
                return organizationSort()
            }
            R.id.reverse_sort -> {
                item.isChecked = !isReverse
                isReverse = item.isChecked
                return organizationSort()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun organizationSort(): Boolean {
        val adapter = binding!!.recyclerView.adapter as FavouriteOrganizationListAdapter
        adapter.data.apply {
            if (isSortByName)
                sortBy { it.name }
            if (isSortByPerson)
                sortBy { it.totalSeats - it.emptySeats }
            if (isReverse)
                reverse()
        }
        adapter.notifyDataSetChanged()
        return true
    }

    override fun getLayout(inflater: LayoutInflater): View {
        binding = MineFavourtieOrgActivityBinding.inflate(inflater)
        return binding!!.root
    }

    override fun createPresenter(): FavouriteReservationListPresenter {
        return FavouriteReservationListPresenter()
    }

    override fun createView(): IFavouriteReservationListView {
        return this
    }

    override fun init() {
        userId = intent.getIntExtra("user_id", 1)
        setSupportActionBar(binding!!.toolBar)
        supportActionBar?.title = "机构收藏"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        initAdapter()
        binding!!.swipeRefreshLayout.apply {
            setOnRefreshListener {
                presenter.getFavouriteReservationList(this@UserFavouriteOrgActivity, userId)
            }
        }

        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.recyclerView.adapter = organizationListAdapter
        organizationListAdapter?.setEmptyView(R.layout.base_loading_view)
        presenter.getFavouriteReservationList(this@UserFavouriteOrgActivity, userId)
    }

    private fun initAdapter() {
        organizationListAdapter = FavouriteOrganizationListAdapter().apply {
            addChildClickViewIds(R.id.mine_favourite_org_collect)
            setOnItemClickListener { adapter, view, position ->
                val params = Bundle()
                params.putParcelable("organization", adapter.data[position] as Organization)
                ARouter.getInstance()
                        .build(ORG_DETAIL_PATH)
                        .with(params)
                        .navigation()
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.mine_favourite_org_collect -> {
                        val item = adapter.data[position] as Organization
                        Toast.makeText(this@UserFavouriteOrgActivity, "id:${item.id}", Toast.LENGTH_SHORT).show();
                        adapter.setData(position, item.apply { isFavourite = !isFavourite })
                        presenter.makeOrganizationFavourite(
                                this@UserFavouriteOrgActivity,
                                item.id, userId, item.isFavourite)
                    }
                }
            }
        }
        resultListAdapter = FavouriteOrganizationListAdapter().apply {
            addChildClickViewIds(R.id.mine_favourite_org_collect)
            setOnItemClickListener { adapter, view, position ->
                val params = Bundle()
                params.putParcelable("organization", adapter.data[position] as Organization)
                ARouter.getInstance()
                        .build(ORG_DETAIL_PATH)
                        .with(params)
                        .navigation()
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.mine_favourite_org_collect -> {
                        val item = adapter.data[position] as Organization
                        Toast.makeText(this@UserFavouriteOrgActivity, "${item.id}", Toast.LENGTH_SHORT).show();
                        adapter.setData(position, item.apply { isFavourite = !isFavourite })

                        presenter.makeOrganizationFavourite(
                                this@UserFavouriteOrgActivity,
                                item.id, userId, item.isFavourite)
                    }
                }
            }
        }
    }

    override fun getFavouriteOrganizationListResult(organizationList: List<Organization>?) {
        if (organizationList != null && organizationList.isNotEmpty())
            organizationListAdapter?.setNewInstance(organizationList.toMutableList())
        else
            organizationListAdapter?.setEmptyView(R.layout.base_empty_view)

        binding!!.swipeRefreshLayout.isRefreshing = false;
    }

    override fun makeOrganizationFavourite(response: BaseResponse<Any>) {
        when (response.code) {
            1 -> {
                Toast.makeText(this, if (response.data == null) "成功" else "${response.data}", Toast.LENGTH_SHORT).show();
            }
            else -> {
                Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}