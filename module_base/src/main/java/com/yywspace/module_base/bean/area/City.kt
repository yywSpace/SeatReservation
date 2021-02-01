package com.yywspace.module_base.bean.area

/**
 * 地级市
 * @author jx on 2018/4/12.
 */
public data class City(
        var code: String,
        var name: String,
        var areaList: List<Area>)