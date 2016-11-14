package org.xielipeng.retrofitokhttprxjavademo.net.retrofit;

import org.xielipeng.retrofitokhttprxjavademo.BuildConfig;
import org.xielipeng.retrofitokhttprxjavademo.MyApplication;
import org.xielipeng.retrofitokhttprxjavademo.net.helper.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xielipeng on 2016/5/19.
 */
public class RetrofitClient {

    public static final String BASE_URL = "";

    private static Retrofit mRetrofit = null;

    public <T> T createApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    private static class SingletonHolder {
        private static final RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // Log信息拦截器
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }

            // 公共参数
            /*Interceptor addQueryParameterInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request request;
                    String method = originalRequest.method();
                    Headers headers = originalRequest.headers();
                    HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                            // Provide your custom parameter here
                            .addQueryParameter("platform", "android")
                            .addQueryParameter("version", "1.0.0")
                            .build();
                    request = originalRequest.newBuilder().url(modifiedUrl).build();
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(addQueryParameterInterceptor);*/

            // 设置头
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
//                            .header("token", token)
//                            .header("AppType", "TPOS")
//                            .header("Content-Type", "application/json")
//                            .header("Accept", "application/json")
                            .method(originalRequest.method(), originalRequest.body());
                    Request request = requestBuilder.build();
//                    Log.i("RetrofitClient", "intercept: token:" + token);
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(headerInterceptor);
            // 设置cookie


            // 设置超时和重连
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);
            // 设置缓存
            File cacheFile = new File(MyApplication.getInstance().getExternalCacheDir(), "xielip");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetUtil.isNetworkAvailable(MyApplication.getInstance())) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (NetUtil.isNetworkAvailable(MyApplication.getInstance())) {
                        int maxAge = 2;
                        // 有网络时 设置缓存超时时间0个小时
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("nyn")
                                .build();
                    }
                    return response;
                }
            };
            builder.cache(cache).addInterceptor(cacheInterceptor);

            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

}
