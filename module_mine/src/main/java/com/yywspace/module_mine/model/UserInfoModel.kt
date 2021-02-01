package com.yywspace.module_mine.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yywspace.module_base.bean.User
import com.yywspace.module_base.bean.scene.Room
import kotlin.random.Random

object UserInfoModel {
    fun getUserInfo(): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        liveData.value = if (Random.nextBoolean()) User("yywSpace", "", 1) else null
        return liveData
    }
}