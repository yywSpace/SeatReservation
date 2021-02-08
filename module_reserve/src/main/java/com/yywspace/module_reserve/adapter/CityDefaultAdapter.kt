package com.yywspace.module_reserve.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yywspace.module_reserve.R
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout


class CityDefaultAdapter(val layoutInflater: LayoutInflater, location: String) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.reserve_city_select_default_view) {
    var onDefaultTagClickListener: ((String) -> Boolean)? = null
    var onHistoryTagClickListener: ((String) -> Boolean)? = null
    var defaultLocationList: List<String>? = null
    var historyLocationList: List<String>? = null

    init {
        defaultLocationList = listOf(location)
        historyLocationList = listOf("正轴", "开发", "河南")
    }

    /**
     * 在此方法中设置item数据
     */
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.apply {
            findViewById<TagFlowLayout>(R.id.reserve_default_location)
                    .apply {
                        setOnTagClickListener { view, position, parent ->
                            onDefaultTagClickListener?.invoke(defaultLocationList?.get(position)!!)!!
                        }
                        adapter = object : TagAdapter<String>(defaultLocationList) {
                            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                                return layoutInflater.inflate(R.layout.reserve_city_select_default_item, null)
                                        .apply {
                                            findViewById<TextView>(R.id.reserve_location).text = t
                                        }
                            }
                        }
                    }

            findViewById<TagFlowLayout>(R.id.reserve_location_history)
                    .apply {
                        setOnTagClickListener { view, position, parent ->
                            onHistoryTagClickListener?.invoke(historyLocationList?.get(position)!!)!!
                        }
                        adapter = object : TagAdapter<String>(historyLocationList) {
                            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                                return layoutInflater.inflate(R.layout.reserve_city_select_default_item, null)
                                        .apply {
                                            findViewById<TextView>(R.id.reserve_location).text = t
                                        }
                            }
                        }
                    }
        }
    }
}
