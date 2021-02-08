package com.yywspace.module_reserve.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Organization
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import com.yywspace.module_base.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

object OrganizationModel {
    fun makeOrganizationFavourite(organizationId: Int, userId: Int, favouriteStatus: Boolean): LiveData<BaseResponse<Any>> {
        val body = """
          {
              "organizationId" :$organizationId,
              "userId": $userId,
              "favouriteStatus" : $favouriteStatus
          } """.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().makeOrganizationFavourite(body)
    }

    fun getOrganizationList(): LiveData<BaseResponse<List<Organization>>> {
        return ServerUtils.getCommonApi().organizationList
    }

    fun getLocalOrganizationList(): List<Organization> {
        val list: MutableList<Organization> = mutableListOf()
        for (i in 0 until Random.Default.nextInt(5, 9)) {
            list.add(Organization(-1, "河南大学$i", "河南大学-金明校区",
                    "<p >\n" +
                            "                河南大学图书馆是<b>第一批全国古籍重点保护单位</b>创建于1912年，时为河南留学欧美预备学校图书室。\n" +
                            "            </p>\n" +
                            "            <p>\n" +
                            "                之后随着学校的发展壮大。\n" +
                            "                河南大学图书馆伴随着学校的发展壮大，已经走过了近百年的风雨历程，建国前的38年，历经战乱磨难、步履维艰，直到建国初期，藏书仅15万余册。\n" +
                            "                建国后的半个世纪，特别是改革开放以来，随着党和国家对高等教育的重视，图书馆也遇到了发展的大好时机。在学校领导的关心和支持下，经过图书馆人的不懈努力，图书馆发生了巨大的变化。\n" +
                            "                图书馆文献资源总量：411万册。图书馆现设有读者服务窗口43个，其中各类阅览室、书库34余个，阅览座位6647席。\n" +
                            "</p>\n",
                    100, Random.Default.nextInt(10, 100), Random.Default.nextBoolean()))
        }
        return list
    }
}