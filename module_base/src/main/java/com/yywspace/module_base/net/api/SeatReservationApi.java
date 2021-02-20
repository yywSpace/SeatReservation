package com.yywspace.module_base.net.api;

import androidx.lifecycle.LiveData;

import com.yywspace.module_base.base.BaseResponse;
import com.yywspace.module_base.bean.Organization;
import com.yywspace.module_base.bean.Reservation;
import com.yywspace.module_base.bean.User;
import com.yywspace.module_base.bean.scene.Floor;
import com.yywspace.module_base.bean.scene.Room;
import com.yywspace.module_base.bean.scene.Seat;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface SeatReservationApi {
    @GET("user/{id}")
    LiveData<BaseResponse<User>> getUser(@Path("id") int id);

    @POST("user/login")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<User>> login(@Body RequestBody requestBody);

    @POST("user/update")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<Object>> updateUser(@Body RequestBody requestBody);

    @POST("admin/login")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<User>> adminLogin(@Body RequestBody requestBody);

    @POST("user/register")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<User>> register(@Body RequestBody requestBody);

    @GET("/common/getPublicKey")
    LiveData<BaseResponse<String>> getPublicKey();

    @GET("/organizations")
    LiveData<BaseResponse<List<Organization>>> getOrganizationList();

    @GET("/organizations/floors/{id}")
    LiveData<BaseResponse<List<Floor>>> getFloorList(@Path("id") int id);

    @GET("/organizations/rooms/{id}")
    LiveData<BaseResponse<List<Room>>> getRoomList(@Path("id") int id);


    @GET("/organizations/seats/{id}")
    LiveData<BaseResponse<List<Seat>>> getSeatList(@Path("id") int id);

    @GET("/reservations/{id}")
    LiveData<BaseResponse<List<Reservation>>> getReservationList(@Path("id") int id);

    @POST("/reservations")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<Object>> insertReservation(@Body RequestBody requestBody);

    @POST("/organizations/favourite")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<Object>> makeOrganizationFavourite(@Body RequestBody requestBody);

    @GET("/reservations/running/{userId}")
    LiveData<BaseResponse<Reservation>> getRunningReservation(@Path("userId") int userId);

    @POST("/file/upload")
    @Multipart
    @Headers({"contentType: multipart/form-data"})
    LiveData<BaseResponse<Object>> uploadFile(@Part List<MultipartBody.Part> partList);

}
