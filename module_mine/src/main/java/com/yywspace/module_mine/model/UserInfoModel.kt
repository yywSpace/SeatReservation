package com.yywspace.module_mine.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.JsonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object UserInfoModel {
    fun getUserInfo(): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        liveData.value = User.currentUser ?: User("yywSpace", "", 1)
        return liveData
    }

    fun updateUserInfo(user: User?): LiveData<BaseResponse<Any>> {
        val body = JsonUtils.getGson().toJson(user)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return ServerUtils.getCommonApi().updateUser(body)
    }
}