package com.yywspace.module_base.net.interceptor;

import com.yywspace.module_base.AppConfig;
import com.yywspace.module_base.net.crypto.AESUtil;
import com.yywspace.module_base.net.crypto.RSAUtil;
import com.yywspace.module_base.util.JsonUtils;
import com.yywspace.module_base.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 对请求数据进行加密处理
 */
public class RequestEncryptInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Charset charset = StandardCharsets.UTF_8;
        String method = request.method().toLowerCase().trim();

        HttpUrl url = request.url();
        /*本次请求的接口地址*/
        String apiPath = String.format(Locale.getDefault(), "%s://%s:%d%s", url.scheme(), url.host(), url.port(), url.encodedPath()).trim();
        /*服务端的接口地址*/
        String serverPath = String.format(Locale.getDefault(), "%s://%s:%d/", url.scheme(), url.host(), url.port()).trim();

        /*如果请求的不是服务端的接口，不加密*/
        if (!serverPath.startsWith(AppConfig.BASE_URL)) {
            return chain.proceed(request);
        }
        if (!apiPath.contains("user") && !apiPath.contains("admin"))
            return chain.proceed(request);
        LogUtils.d("requestData: " + method);
        LogUtils.d("apiPath: " + apiPath);

        if (method.equals("get") || method.equals("delete")) {
            /*如果有请求数据 则加密*/
            if (url.encodedQuery() != null) {
                try {
                    String queryParamNames = request.url().encodedQuery();
                    // 加密
                    String encryptQueryParamNames = "这里调用加密的方法，自行修改";
                    // TODO: 20-11-12

                    //拼接加密后的url，参数字段自己跟后台商量，这里我用param，后台拿到数据先对param进行解密，解密后的数据就是请求的数据
                    String newUrl = String.format(Locale.getDefault(), "%s?param=%s", apiPath, encryptQueryParamNames).trim();

                    //构建新的请求
//                    request = request.newBuilder().url(newUrl).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    return chain.proceed(request);
                }
            }
        } else {
            //不是Get和Delete请求时，则请求数据在请求体中
            RequestBody requestBody = request.body();

            /*判断请求体是否为空  不为空则执行以下操作*/
            if (requestBody != null) {
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                    /*如果是二进制上传  则不进行加密*/
                    if (contentType.type().toLowerCase().equals("multipart")) {
                        return chain.proceed(request);
                    }
                }
                charset = charset == null ? StandardCharsets.UTF_8 : charset;

                /*获取请求的数据*/
                try {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String requestData = URLDecoder.decode(buffer.readString(charset).trim(), "utf-8");
                    // 加密
                    // 利用aeskey加密明文json数据
                    String encryptData = AESUtil.encrypt(requestData, AESUtil.SECRET_KEY);
                    // 使用RSA公钥加密AES密钥aeskey，
                    LogUtils.d(AESUtil.SECRET_KEY);
                    LogUtils.d(RSAUtil.PUBLIC_KEY);
                    String encryptAesData = RSAUtil.encrypt(AESUtil.SECRET_KEY, RSAUtil.getPublicKey(RSAUtil.PUBLIC_KEY));
                    long timestamp = System.currentTimeMillis();
                    Map<String, Object> map = new HashMap<>();
                    map.put("encryptData", encryptData);
                    map.put("encryptAesData", encryptAesData);
                    map.put("timestamp", timestamp);
                    /*构建新的请求体*/
                    RequestBody newRequestBody = RequestBody.create(JsonUtils.getGson().toJson(map), contentType);
                    LogUtils.d(JsonUtils.getGson().toJson(map));
                    LogUtils.d(AESUtil.SECRET_KEY);

                    /*构建新的requestBuilder*/
                    Request.Builder newRequestBuilder = request.newBuilder();
                    //根据请求方式构建相应的请求
                    switch (method) {
                        case "post":
                            newRequestBuilder.post(newRequestBody);
                            break;
                        case "put":
                            newRequestBuilder.put(newRequestBody);
                            break;
                    }
                    request = newRequestBuilder.build();
                } catch (Exception e) {
                    LogUtils.e("加密异常");
                    e.printStackTrace();
                    return chain.proceed(request);
                }
            }
        }
        return chain.proceed(request);
    }
}
