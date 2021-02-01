package com.yywspace.module_base.bean.area

/**
 * 省份
 * @author jx on 2018/4/12.
 */
public data class Province(
        var code: String,
        var name: String,
        var isSelected: Boolean,
        var cityList: List<City>)
