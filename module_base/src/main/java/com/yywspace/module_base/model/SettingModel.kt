package com.yywspace.module_base.model

import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.Setting
import com.yywspace.module_base.bean.scene.Seat
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import com.yywspace.module_base.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

object SettingModel {

    fun insertSetting(setting: Setting): LiveData<BaseResponse<Any>> {
        val body: RequestBody = JsonUtils.getGson().toJson(setting)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().insertSetting(body)
    }

    fun getSettingByUserName(userName: String): LiveData<BaseResponse<Setting>> {
        return ServerUtils.getCommonApi().getSettingByUserName(userName)
    }

}