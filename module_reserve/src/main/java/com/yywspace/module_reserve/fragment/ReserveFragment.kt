package com.yywspace.module_reserve.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.howshea.roundcornerimageview.RoundCornerImageView
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.yywspace.module_base.base.BaseFragment
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_base.util.PermissionUtils
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.activity.CitySelectActivity
import com.yywspace.module_reserve.activity.OrganizationDetailActivity
import com.yywspace.module_reserve.adapter.OrganizationListAdapter
import com.yywspace.module_reserve.databinding.ReserveFragmentMainBinding
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.presenter.OrganizationListPresenter
import java.util.*
import java.util.stream.Collectors

@Route(path = RouterPath.RESERVE_PATH)
class ReserveFragment : BaseFragment<IOrganizationListView, OrganizationListPresenter>(), IOrganizationListView {
    private var locationClient: AMapLocationClient? = null
    private var binding: ReserveFragmentMainBinding? = null
    private var organizationListAdapter: OrganizationListAdapter? = null
    private var resultListAdapter: OrganizationListAdapter? = null
    private var isReverse = false
    private var isSortByName = true
    private var isSortByPerson = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reserve_menu_organization, menu)
        val searchItem = menu.findItem(R.id.search)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding!!.recyclerView.adapter = organizationListAdapter
                return true
            }
        })
        //通过MenuItem得到SearchView
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return onQueryTextChange(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    val searchRes = organizationListAdapter!!.data
                            .stream()
                            .filter {
                                it.name.contains(newText)
                            }.collect(Collectors.toList())
                    resultListAdapter?.setNewInstance(searchRes.toMutableList())
                    binding!!.recyclerView.adapter = resultListAdapter
                }
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
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
        val adapter = binding!!.recyclerView.adapter as OrganizationListAdapter
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
        binding = ReserveFragmentMainBinding.inflate(inflater)
        return binding!!.root
    }

    override fun createPresenter(): OrganizationListPresenter {
        return OrganizationListPresenter()
    }

    override fun createView(): IOrganizationListView {
        return this
    }

    override fun init() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding!!.toolBar)
        activity.supportActionBar?.title = ""
        initAdapter()
        binding!!.swipeRefreshLayout.apply {
            setOnRefreshListener {
                PermissionUtils.requestPermission(requireContext(), {
                    initLocationClient()
                }, {
                    Toast.makeText(requireContext(), "权限未赋予", Toast.LENGTH_SHORT).show()
                }, *Permission.Group.LOCATION)
            }
        }
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.adapter = organizationListAdapter
        organizationListAdapter?.setEmptyView(R.layout.base_loading_view)

        ServerUtils.getCommonApi().getRunningReservation(1)
                .observe(this, androidx.lifecycle.Observer {
                    Toast.makeText(requireContext(), "${it.data}", Toast.LENGTH_SHORT).show();
                    Reservation.runningReservation = it.data
                })

        PermissionUtils.requestPermission(requireContext(), {
            initLocationClient()
        }, {
            Toast.makeText(requireContext(), "权限未赋予", Toast.LENGTH_SHORT).show()
        }, *Permission.Group.LOCATION)
    }

    private fun initAdapter() {
        organizationListAdapter = OrganizationListAdapter().apply {
            addChildClickViewIds(R.id.reserve_collect)
            setOnItemClickListener { adapter, view, position ->
                val shareImg = view.findViewById<RoundCornerImageView>(R.id.organization_image)
                val intent = Intent(requireContext(), OrganizationDetailActivity::class.java)
                intent.putExtra("organization", adapter.data[position] as Organization)
                val bundle: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        shareImg,
                        getString(R.string.base_organization_transition_name)
                ).toBundle()
                startActivity(intent, bundle)
                Toast.makeText(requireContext(), "点击子项：$position", Toast.LENGTH_SHORT).show();
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.reserve_collect -> {
                        val item = adapter.data[position] as Organization
                        adapter.setData(position, item.apply { isFavourite = !isFavourite })
                        val userId = if(User.currentUser == null) 1 else User.currentUser!!.id
                        if (userId != null) {
                            presenter.makeOrganizationFavourite(
                                    this@ReserveFragment,
                                    item.id, userId, item.isFavourite)
                        }
                    }
                }
            }
        }
        resultListAdapter = OrganizationListAdapter().apply {
            addChildClickViewIds(R.id.reserve_collect)
            setOnItemClickListener { adapter, view, position ->
                val shareImg = view.findViewById<RoundCornerImageView>(R.id.organization_image)
                val intent = Intent(requireContext(), OrganizationDetailActivity::class.java)
                intent.putExtra("organization", adapter.data[position] as Organization)
                val bundle: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        shareImg,
                        getString(R.string.base_organization_transition_name)
                ).toBundle()
                startActivity(intent, bundle)
                Toast.makeText(requireContext(), "点击子项：$position", Toast.LENGTH_SHORT).show();
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.reserve_collect -> {
                        val item = adapter.data[position] as Organization
                        adapter.setData(position, item.apply { isFavourite = !isFavourite })
                        presenter.makeOrganizationFavourite(
                                this@ReserveFragment,
                                item.id, 1, item.isFavourite)
                    }
                }
            }
        }
    }

    private fun initLocationClient() {
        locationClient?.onDestroy()
        //初始化定位
        locationClient = AMapLocationClient(requireActivity().applicationContext)
                .apply {
                    //设置定位回调监听
                    setLocationListener { aMapLoc ->
                        if (aMapLoc.errorCode == 0) {
                            binding!!.reserveLocation.text = aMapLoc.city
                            binding!!.reserveLocationContainer.setOnClickListener {
                                val intent = Intent(requireContext(), CitySelectActivity::class.java)
                                intent.putExtra("location", aMapLoc.city)
                                activityLauncher.launch(intent)
                            }
                            val userId = if (User.currentUser == null) 1 else User.currentUser!!.id
                            if (userId != null) {
                                presenter.getOrganizationList(this@ReserveFragment, aMapLoc.city, userId)
                            }
                        } else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError", "location Error, ErrCode:"
                                    + aMapLoc.errorCode + ", errInfo:"
                                    + aMapLoc.errorInfo);
                        }
                    }
                    val locationOption = AMapLocationClientOption().apply {
                        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
                        locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
                        //获取一次定位结果
                        isOnceLocation = true
                    }
                    setLocationOption(locationOption)
                    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                    stopLocation()
                    startLocation()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient?.onDestroy()
    }

    override fun getOrganizationListResult(organizationList: List<Organization>?) {
        LogUtils.d(organizationList.toString())
        if (organizationList != null && organizationList.isNotEmpty())
            organizationListAdapter?.setNewInstance(organizationList.toMutableList())
        else {
            organizationListAdapter?.setNewInstance(mutableListOf())
            organizationListAdapter?.setEmptyView(R.layout.base_empty_view)
        }
        binding!!.swipeRefreshLayout.isRefreshing = false;
    }

    override fun makeOrganizationFavourite(response: BaseResponse<Any>) {
        when (response.code) {
            1 -> {
                Toast.makeText(requireContext(), if (response.data == null) "成功" else "${response.data}", Toast.LENGTH_SHORT).show();
            }
            else -> {
                Toast.makeText(requireContext(), "操作失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val result = activityResult.data?.getStringExtra(CitySelectActivity.CITY_NAME)
            if (result != null) {
                val userId = if (User.currentUser == null) 1 else User.currentUser!!.id
                if (userId != null) {
                    presenter.getOrganizationList(this@ReserveFragment, result, userId)
                }
            }
            binding!!.reserveLocation.text = result
        }
    }
}