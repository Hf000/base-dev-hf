package org.hf.boot.springboot.service.impl;

import com.alibaba.fastjson2.JSON;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.service.StreamingResponseService;
import org.hf.boot.springboot.utils.OkHttpUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 流失响应实现
 * @author HF
 */
@Slf4j
@Service
public class StreamingResponseServiceImpl implements StreamingResponseService {

    @Qualifier("customTaskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor customTaskExecutor;

    private final static String REMOTE_URL = "remoteUrl";

    @Override
    public SseEmitter streamingResponse() {
        // 请求地址
        String url = REMOTE_URL;
        // 请求参数
        Map<String, Object> remoteRequestParams = new HashMap<>();
        // 创建SSE响应对象
        SseEmitter emitter = new SseEmitter(65_000L);
        // 创建RxJava的被观察者进行接口请求
        Disposable disposable = Observable.create((ObservableEmitter<String> observableEmitter) -> {
                    okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON.toJSONString(remoteRequestParams),
                            okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_VALUE));
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                            .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .build();
                    // 通过okHttp的enqueue()异步进行网络请求
                    OkHttpUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            log.error("请求失败, url={}", url , e);
                            observableEmitter.onError(e);
                        }
                        // 获取网络请求响应, 需要注意此方法的Response对象的引用包是否正确
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) {
                            if (!response.isSuccessful() || response.body() == null) {
                                log.error("请求返回错误响应或请求体为空: code={}, message={}, url={}", response.code(), response.message(), url);
                                response.close();
                                observableEmitter.onError(new IOException("请求地址=" + url + " 返回错误响应或请求体为空"));
                                return;
                            }
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    if (StringUtils.isBlank(line)) {
                                        continue;
                                    }
                                    log.debug("Received line from {}: {}", url, line);
                                    if (line.contains("结束标识")) {
                                        log.info("结束标识收到，完成 SseEmitter, lien={}", line);
                                        observableEmitter.onNext(line);
                                        // 完成流式响应
                                        observableEmitter.onComplete();
                                        return;
                                    }
                                    // 进行内容流式输出
                                    observableEmitter.onNext(line);
                                }
                            } catch (Exception e) {
                                log.error("处理响应时发生错误", e);
                                observableEmitter.onError(e);
                            } finally {
                                response.close();
                            }
                        }
                    });
                })
                // 指定请求的线程池
                .subscribeOn(Schedulers.from(customTaskExecutor))
                // 观察者订阅被观察者通过onNext()以传递响应数据
                .subscribe(
                        line -> {
                            try {
                                log.debug("emitter send: {}", line);
                                emitter.send(line);
                            } catch (IOException e) {
                                log.error("发送数据到 SseEmitter 时发生错误", e);
                            }
                        },
                        error -> {
                            log.error("处理流式响应时发生错误", error);
                            emitter.completeWithError(error);
                        },
                        emitter::complete
                );
        return emitter;
    }

    @Override
    public String blockingResponse() {
        // 请求地址
        String url = REMOTE_URL;
        // 请求参数
        Map<String, Object> remoteRequestParams = new HashMap<>();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_VALUE), JSON.toJSONString(remoteRequestParams));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        // 进行异步网络请求, 如果需要同步返回结果, 这里要将enqueue()替换成同步方法execute()
//        OkHttpUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                log.error("采用拼接方式处理流式请求失败, url={}", url , e);
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) {
//                if(!response.isSuccessful() || response.body() == null){
//                    log.error("采用拼接方式处理流式请求返回错误响应或请求体为空: code={}, message={}, url={}", response.code(), response.message(), url);
//                    response.close();
//                    return;
//                }
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8))) {
//                    StringBuilder contentBd = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        if (StringUtils.isBlank(line)) {
//                            continue;
//                        }
//                        log.debug("Received line from {}: {}", url, line);
//                        if (line.contains("结束标识")) {
//                            contentBd.append(line);
//                        }
//                    }
//                    String contentStr = contentBd.toString();
//                    System.out.println("异步拼接好的内容信息="+ contentStr);
//                } catch (Exception e) {
//                    log.error("采用拼接方式处理流式请求时处理响应时发生错误", e);
//                } finally {
//                    response.close();
//                }
//            }
//        });
        StringBuilder contentBd = new StringBuilder();
        // 需要注意此方法的Response对象的引用包是否正确,目前引用的包下的Response对象没有实现Closeable接口,所以不确定是否能在try()中正确的关闭资源
        try (Response response = OkHttpUtil.getOkHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("采用拼接方式处理流式请求返回错误响应: code={}, message={}, url={}", response.code(), response.message(), url);
                throw new BusinessException("采用拼接方式处理流式请求异常,code=" + response.code() + ", message=" + response.message());
            }
            if (response.body() == null) {
                throw new BusinessException("采用拼接方式处理流式请求响应体为空");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    log.debug("Received line from {}: {}", url, line);
                    if (line.contains("结束标识")) {
                        contentBd.append(line);
                    }
                }
            } catch (Exception e) {
                log.error("采用拼接方式处理流式请求时处理响应时发生错误", e);
            }
        } catch (Exception e) {
            throw new BusinessException("获取流程响应结果异常", e);
        }
        return contentBd.toString();
    }

}