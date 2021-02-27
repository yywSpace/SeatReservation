package com.yywspace.module_base.model

import androidx.lifecycle.LiveData
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.net.ServerUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


object FileModel {
    fun uploadFile(path: String): LiveData<BaseResponse<String>> {
        // 参数添加
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val file = File(path);
        val imageBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        //添加文件(uploadfile就是你服务器中需要的文件参数)
        builder.addFormDataPart("uploadFile", file.name, imageBody)
        val parts: List<MultipartBody.Part> = builder.build().parts
        return ServerUtils.getCommonApi().uploadFile(parts)
    }
}