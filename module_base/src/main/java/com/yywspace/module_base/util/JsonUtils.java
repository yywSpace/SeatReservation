package com.yywspace.module_base.util;


import com.google.gson.Gson;


public class JsonUtils {
    private volatile static Gson gson;

    public static Gson getGson() {
            if (gson == null) {
                synchronized (JsonUtils.class) {
                    if (gson == null) {
                        gson = new Gson();
                    }
                }
            }
        return gson;
    }
}