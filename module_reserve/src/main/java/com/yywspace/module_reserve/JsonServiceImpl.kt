package com.yywspace.module_reserve

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.google.gson.Gson
import java.lang.reflect.Type

@Route(path = "/service/json")
class JsonServiceImpl : SerializationService {
    override fun init(context: Context?) {}
    override fun <T> json2Object(text: String, clazz: Class<T>): T {
        return Gson().fromJson(text, clazz)
    }

    override fun object2Json(instance: Any): String {
        return Gson().toJson(instance)
    }

    override fun <T> parseObject(input: String?, clazz: Type?): T {
        return Gson().fromJson(input, clazz)
    }
}