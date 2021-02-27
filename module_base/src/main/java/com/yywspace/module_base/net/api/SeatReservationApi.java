package com.yywspace.module_base.net.api;

import androidx.lifecycle.LiveData;

import com.yywspace.module_base.base.BaseResponse;
import com.yywspace.module_base.bean.Organization;
import com.yywspace.module_base.bean.Reservation;
import com.yywspace.module_base.bean.User;
import com.yywspace.module_base.bean.scene.Floor;
import com.yywspace.module_base.bean.scene.Room;
import com.yywspace.module_base.bean.scene.Seat;
import com.yywspace.module_base.bean.statistic.StatisticOverview;
import com.yywspace.module_base.bean.statistic.StatisticReservation;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/organization/list")
    LiveData<BaseResponse<List<Organization>>> getOrganizationListByLocation(@Query("location") String location, @Query("userId") int userId);

    @GET("/organization/favourites/{userId}")
    LiveData<BaseResponse<List<Organization>>> getFavouriteOrganizationList(@Path("userId") Integer userId);

    @GET("/organization/list/{group}")
    LiveData<BaseResponse<List<Organization>>> getOrganizationListByGroup(@Path("group") String group);

    @POST("/organization")
    LiveData<BaseResponse<Integer>> insertOrganization(@Body RequestBody requestBody);

    @PUT("/organization")
    LiveData<BaseResponse<Object>> updateOrganization(@Body RequestBody requestBody);

    @DELETE("/organization/{orgId}")
    LiveData<BaseResponse<Object>> deleteOrganization(@Path("orgId") int orgId);

    @POST("/organization/favourite")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<Object>> makeOrganizationFavourite(@Body RequestBody requestBody);

    @GET("/floor/list/{orgId}")
    LiveData<BaseResponse<List<Floor>>> getFloorList(@Path("orgId") int orgId);

    @POST("/floor")
    LiveData<BaseResponse<Integer>> insertFloor(@Body RequestBody requestBody);

    @PUT("/floor")
    LiveData<BaseResponse<Object>> updateFloor(@Body RequestBody requestBody);

    @DELETE("/floor/{floorId}")
    LiveData<BaseResponse<Object>> deleteFloor(@Path("floorId") int floorId);

    @POST("/room")
    LiveData<BaseResponse<Integer>> insertRoom(@Body RequestBody requestBody);

    @PUT("/room")
    LiveData<BaseResponse<Object>> updateRoom(@Body RequestBody requestBody);

    @DELETE("/room/{roomId}")
    LiveData<BaseResponse<Object>> deleteRoom(@Path("roomId") int roomId);

    @GET("/room/list/{floorId}")
    LiveData<BaseResponse<List<Room>>> getRoomList(@Path("floorId") int id);

    @GET("/seats/list/{roomId}")
    LiveData<BaseResponse<List<Seat>>> getSeatList(@Path("roomId") int roomId);

    @GET("/reservation/list/{id}")
    LiveData<BaseResponse<List<Reservation>>> getReservationList(@Path("id") int id);

    @POST("/reservation")
    @Headers({"contentType: application/json;charset=UTF-8"})
    LiveData<BaseResponse<Object>> insertReservation(@Body RequestBody requestBody);

    @GET("/reservation/running/{userId}")
    LiveData<BaseResponse<Reservation>> getRunningReservation(@Path("userId") int userId);

    @POST("/file/upload")
    @Multipart
    @Headers({"contentType: multipart/form-data"})
    LiveData<BaseResponse<String>> uploadFile(@Part List<MultipartBody.Part> partList);

    @GET("/statistic/overview/{userId}")
    LiveData<BaseResponse<StatisticOverview>> getStatisticOverview(@Path("userId") int userId);

    @GET("/statistic/reservation/{userId}")
    LiveData<BaseResponse<List<StatisticReservation>>> getStatisticReservation(@Path("userId") int userId);
}
