package com.yywspace.module_base.net.interceptor;


import com.yywspace.module_base.net.crypto.AESUtil;
import com.yywspace.module_base.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 对响应数据进行解密
 */
public class ResponseDecryptInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        int a;
        if (response.isSuccessful()) {
            LogUtils.d("ResponseDecryptInterceptor");
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                try {
                    BufferedSource source = responseBody.source();
                    source.request(java.lang.Long.MAX_VALUE);
                    Buffer buffer = source.getBuffer();
                    Charset charset = StandardCharsets.UTF_8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null)
                        charset = contentType.charset(charset);
                    charset = charset == null ? StandardCharsets.UTF_8 : charset;
                    // 获取数据
                    String bodyString = buffer.clone().readString(charset)
                            .replace("\"", "")
                            .replace("\\u003d","=");
                    LogUtils.d(bodyString);
                    LogUtils.d(AESUtil.SECRET_KEY);
                    String responseData = AESUtil.decrypt(bodyString.trim(), AESUtil.SECRET_KEY);
                    LogUtils.d(responseData);
                    /*将解密后的明文返回*/
                    ResponseBody newResponseBody = ResponseBody.create(responseData, contentType);
                    response = response.newBuilder().body(newResponseBody).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e("解密异常");
                    return response;
                }
            }
        }
        return response;
    }
}
