package com.yywspace.module_reserve.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.howshea.roundcornerimageview.RoundCornerImageView
import com.yywspace.module_base.base.BaseFragment
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.path.RouterPath
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.activity.CitySelectActivity
import com.yywspace.module_reserve.activity.OrganizationDetailActivity
import com.yywspace.module_reserve.adapter.OrganizationListAdapter
import com.yywspace.module_reserve.databinding.ReserveFragmentMainBinding
import com.yywspace.module_reserve.iview.IOrganizationListView
import com.yywspace.module_reserve.presenter.OrganizationListPresenter
import java.util.stream.Collectors

@Route(path = RouterPath.RESERVE_PATH)
class ReserveFragment : BaseFragment<IOrganizationListView, OrganizationListPresenter>(), IOrganizationListView {
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
                                it.name.contains(newText) || it.location.contains(newText)
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
                presenter.getOrganizationList(this@ReserveFragment)
            }
        }
        // TODO: 20-11-27 定位读取
        binding!!.reserveLocation.text = "郑州"
        binding!!.reserveLocationContainer.setOnClickListener {
            activityLauncher.launch(Intent(requireContext(), CitySelectActivity::class.java))
        }
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.adapter = organizationListAdapter
        organizationListAdapter?.setEmptyView(R.layout.base_loading_view)
        presenter.getOrganizationList(this)
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
                        Toast.makeText(requireContext(), "collect$position", Toast.LENGTH_SHORT).show();
                        val item = adapter.data[position] as Organization
                        adapter.setData(position, item.apply { isFavourite = !isFavourite })
                    }
                }
            }
        }
        resultListAdapter = OrganizationListAdapter().apply {
            addChildClickViewIds(R.id.reserve_collect)
            setOnItemClickListener { adapter, view, position ->
                Toast.makeText(requireContext(), "点击子项：$position", Toast.LENGTH_SHORT).show();
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.reserve_collect -> {
                        Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show();
                        val item = adapter.data[position] as Organization
                        adapter.setData(position, item.apply { isFavourite = !isFavourite })
                    }
                }
            }
        }
    }

    override fun getOrganizationListResult(organizationList: List<Organization>?) {
        LogUtils.d(organizationList.toString())
        if (organizationList == null)
            organizationListAdapter?.setEmptyView(R.layout.base_empty_view)
        else
            organizationListAdapter?.setNewInstance(organizationList.toMutableList())

        binding!!.swipeRefreshLayout.isRefreshing = false;
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val result = activityResult.data?.getStringExtra(CitySelectActivity.CITY_NAME)
            binding!!.reserveLocation.text = result
        }
    }
}