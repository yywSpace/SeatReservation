package com.yywspace.module_base.net;

import com.yywspace.module_base.net.adapter.LiveDataCallAdapterFactory;
import com.yywspace.module_base.net.api.SeatReservationApi;
import com.yywspace.module_base.net.interceptor.RequestEncryptInterceptor;
import com.yywspace.module_base.net.interceptor.ResponseDecryptInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yywspace.module_base.AppConfig.BASE_URL;

/**
 * 网络请求 工具
 */
public class ServerUtils {
    private static final int TIME_OUT = 5 * 1000;//链接超时时间
    private volatile static SeatReservationApi mSeatReservationApi;

    public static SeatReservationApi getCommonApi() {
        try {
            if (mSeatReservationApi == null) {
                synchronized (ServerUtils.class) {
                    if (mSeatReservationApi == null) {
                        mSeatReservationApi = createService(SeatReservationApi.class, BASE_URL);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSeatReservationApi;
    }

    private static <S> S createService(Class<S> serviceClass, String url) throws Exception {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(url)
                        .client(httpClient.build())
                        .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }


    private static final OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    //设置超时
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor(new RequestEncryptInterceptor())
                    .addInterceptor(new ResponseDecryptInterceptor());

}
