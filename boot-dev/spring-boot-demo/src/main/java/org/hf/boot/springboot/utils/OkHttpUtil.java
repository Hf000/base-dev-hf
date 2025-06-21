package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtil {

    private static final OkHttpClient OK_HTTP_CLIENT;

    static {
        // 设置 OkHttpClient 的超时时间
        OK_HTTP_CLIENT = new OkHttpClient.Builder()
                // 连接超时时间
                .connectTimeout(30, TimeUnit.SECONDS)
                // 读取超时时间
                .readTimeout(90, TimeUnit.SECONDS)
                // 写入超时时间
                .writeTimeout(90, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }

}