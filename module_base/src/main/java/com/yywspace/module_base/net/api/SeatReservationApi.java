package com.yywspace.module_base.net.api;

import androidx.lifecycle.LiveData;

import com.yywspace.module_base.base.BaseResponse;
import com.yywspace.module_base.bean.User;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SeatReservationApi {
    @GET("user/{id}")
    LiveData<BaseResponse<User>> getUser(@Path("id") int id);

    @POST("user/login")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<User>> login(@Body RequestBody requestBody);

    @POST("admin/login")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<User>> adminLogin(@Body RequestBody requestBody);

    @POST("user/register")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<User>> register(@Body RequestBody requestBody);

    @GET("/common/getPublicKey")
    LiveData<BaseResponse<String>> getPublicKey();
}
