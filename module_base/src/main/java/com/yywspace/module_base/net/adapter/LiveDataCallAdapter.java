package com.yywspace.module_base.net.adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.yywspace.module_base.base.BaseResponse;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private Type mResponseType;
    private boolean isApiResponse;

    LiveDataCallAdapter(Type mResponseType, boolean isApiResponse) {
        this.mResponseType = mResponseType;
        this.isApiResponse = isApiResponse;
    }

    @NonNull
    @Override
    public Type responseType() {
        return mResponseType;
    }

    @NonNull
    @Override
    public LiveData<T> adapt(@NonNull final Call<T> call) {
        return new MyLiveData<>(call, isApiResponse);
    }

    private static class MyLiveData<T> extends LiveData<T> {

        private AtomicBoolean stared = new AtomicBoolean(false);
        private final Call<T> call;
        private boolean isApiResponse;

        MyLiveData(Call<T> call, boolean isApiResponse) {
            this.call = call;
            this.isApiResponse = isApiResponse;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //确保执行一次
            if (stared.compareAndSet(false, true)) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                        T body = response.body();
                        postValue(body);
                    }

                    @Override
                    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                        if (isApiResponse) {
                            //noinspection unchecked
                            postValue((T) new BaseResponse<T>(t.getMessage(), -1));
                        } else {
                            postValue(null);
                        }
                    }
                });
            }
        }
    }
}

