package com.yywspace.module_reserve.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yywspace.module_base.bean.area.City
import com.yywspace.module_base.bean.area.Province
import com.yywspace.module_base.model.CitiesModel
import com.yywspace.module_reserve.CityListItemDecoration
import com.yywspace.module_reserve.R
import com.yywspace.module_reserve.adapter.CityDefaultAdapter
import com.yywspace.module_reserve.adapter.CityListAdapter
import com.yywspace.module_reserve.adapter.ProvinceListAdapter
import com.yywspace.module_reserve.databinding.ReserveCitySelectActivityBinding
import java.lang.reflect.Field
import java.util.stream.Collectors

class CitySelectActivity : AppCompatActivity() {
    companion object {
        const val CITY_NAME = "city_name"
    }

    private var binding: ReserveCitySelectActivityBinding? = null
    private var cityListAdapter: CityListAdapter? = null
    private var cityDefaultAdapter: CityDefaultAdapter? = null
    var cityListRecyclerView: RecyclerView? = null
    var searchListAdapter: CityListAdapter? = null
    var provinceListAdapter: ProvinceListAdapter? = null
    var lastPos = 0
    var areaListView: View? = null
    var searchResultView: View? = null
    var provinceList: List<Province>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReserveCitySelectActivityBinding.inflate(layoutInflater);
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.reserveToolBar)
        initAdapter()
        initView()

        CitiesModel.getCities(this).observe(this, Observer {
            provinceList = it
            provinceListAdapter?.setNewInstance(it.toMutableList().apply {
                val default = Province("0", "推荐", true, listOf())
                add(0, default)
            })
        })

    }

    private fun initAdapter() {
        cityDefaultAdapter = CityDefaultAdapter(layoutInflater).apply {
            setNewInstance(mutableListOf("1"))
            onDefaultTagClickListener = {
                val intent = Intent().apply {
                    putExtra(CITY_NAME, it)
                }
                setResult(RESULT_OK, intent)
                finish()
                true
            }
            onHistoryTagClickListener = {
                val intent = Intent().apply {
                    putExtra(CITY_NAME, it)
                }
                setResult(RESULT_OK, intent)
                finish()
                true
            }
        }
        searchListAdapter = CityListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val city = adapter.data[position] as City
                val intent = Intent().apply {
                    putExtra(CITY_NAME, city.name)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        cityListAdapter = CityListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val city = adapter.data[position] as City
                val intent = Intent().apply {
                    putExtra(CITY_NAME, city.name)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        provinceListAdapter = ProvinceListAdapter(this).apply {
            setOnItemClickListener { adapter, view, position ->
                if (lastPos == position)
                    return@setOnItemClickListener
                val province = adapter.data[position] as Province
                province.isSelected = true
                adapter.run {
                    province.isSelected = true
                    setData(position, province)
                    val oldProvince = adapter.data[lastPos] as Province
                    oldProvince.isSelected = false
                    setData(lastPos, oldProvince)
                }
                if (position == 0) {
                    cityListRecyclerView?.adapter = cityDefaultAdapter
                } else {
                    cityListRecyclerView?.adapter = cityListAdapter
                    cityListAdapter?.setNewInstance(province.cityList.toMutableList())
                }
                lastPos = position
            }
        }
    }

    private fun initView() {
        areaListView = layoutInflater
                .inflate(R.layout.reserve_city_select_area_list, null)
                .apply {
                    cityListRecyclerView = findViewById<RecyclerView>(R.id.reserve_city_recycler_view).apply {
                        addItemDecoration(CityListItemDecoration(this@CitySelectActivity))
                        layoutManager = LinearLayoutManager(this@CitySelectActivity)
                        adapter = cityDefaultAdapter
                    }
                    findViewById<RecyclerView>(R.id.reserve_province_recycler_view).apply {
                        layoutManager = LinearLayoutManager(this@CitySelectActivity)
                        adapter = provinceListAdapter
                    }
                }

        searchResultView = layoutInflater
                .inflate(R.layout.reserve_city_select_search_city_list, null)
                .apply {
                    findViewById<RecyclerView>(R.id.reserve_city_recycler_view).apply {
                        addItemDecoration(CityListItemDecoration(this@CitySelectActivity))
                        layoutManager = LinearLayoutManager(this@CitySelectActivity)
                        adapter = searchListAdapter
                    }
                }
        binding!!.reverseContainer.addView(areaListView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar?.title = "选择城市与地区"

        binding!!.reserveSearchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return onQueryTextChange(query)
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.isNotEmpty()) {
                        if (provinceList == null)
                            return false
                        val resultList = provinceList!!.stream()
                                .flatMap { it.cityList.stream() }
                                .filter { it.name.contains(newText) }
                                .collect(Collectors.toList())
                        searchListAdapter?.setNewInstance(resultList)
                        binding!!.reverseContainer.apply {
                            removeAllViews()
                            addView(searchResultView)
                        }
                        if (resultList.isEmpty()) {
                            searchListAdapter?.setEmptyView(R.layout.reserve_empty_view)
                        }
                        return true
                    } else {
                        binding!!.reverseContainer.apply {
                            removeAllViews()
                            addView(areaListView)
                        }
                        return true
                    }
                }

            })
            setUnderLineTransparent(this)
            setOnClickListener {
                if (!isActivated) {
                    onActionViewExpanded()
                }
            }
        }
    }


    /**设置SearchView下划线透明 */
    private fun setUnderLineTransparent(searchView: SearchView) {
        try {
            val argClass: Class<*> = searchView.javaClass
            // mSearchPlate是SearchView父布局的名字
            val ownField: Field = argClass.getDeclaredField("mSearchPlate")
            ownField.isAccessible = true
            val mView = ownField.get(searchView) as View
            mView.setBackgroundColor(Color.TRANSPARENT)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

}