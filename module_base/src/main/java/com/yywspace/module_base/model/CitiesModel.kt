package com.yywspace.module_base.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.yywspace.module_base.bean.area.Province
import com.yywspace.module_base.util.JsonUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object CitiesModel {
    fun getCities(context: Context): LiveData<List<Province>> {
        val liveData = MutableLiveData<List<Province>>()
        try {
            //通过管理器打开文件并读取
            val json = getJson("city.json", context);
            Log.d("TAG", "getCities: $json")
            val provinceList: List<Province> = JsonUtils.getGson()
                    .fromJson(json,
                            object : TypeToken<List<Province>>() {}.type)
            liveData.value = provinceList;

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return liveData
    }

    private fun getJson(fileName: String, context: Context): String {
        //将json数据变成字符串
        val stringBuilder = StringBuilder()
        try {
            //通过管理器打开文件并读取
            val bf = BufferedReader(InputStreamReader(context.assets.open(fileName)))
            var line: String? = ""
            while (line != null) {
                stringBuilder.append(line)
                line = bf.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}