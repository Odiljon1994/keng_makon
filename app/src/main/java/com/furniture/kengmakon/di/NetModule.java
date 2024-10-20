package com.furniture.kengmakon.di;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.furniture.kengmakon.api.Api;
import com.furniture.kengmakon.api.ApiUtils;
import com.furniture.kengmakon.models.LoginModel;
import com.furniture.kengmakon.utils.PreferencesUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Api provideBaseApi(@Named("baseAPI") Retrofit retrofit) {
        return retrofit.create(Api.class);
    }


    // BaseServer API

    @Provides
    @Singleton
    @Named("baseAPI")
    OkHttpClient provideOkHttpClient2(PreferencesUtil preferencesUtil, Gson gson) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                System.out.println("Token: " + preferencesUtil.getTOKEN());
                if (!preferencesUtil.getTOKEN().equals("")) {

                    builder.addHeader("Authorization", "Token " + preferencesUtil.getTOKEN());

                }
                request = builder.build();
                Response response = chain.proceed(request);
//                BaseResponse baseResponse = gson.fromJson(response.peekBody(Long.MAX_VALUE).string(), BaseResponse.class);
                if (response.code() == 401) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl(ApiUtils.getBaseUrl())
                            .build();
                    Api api = retrofit.create(Api.class);
                    LoginModel.LoginResModel loginResModel = api.login(new LoginModel(preferencesUtil.getEmail(), preferencesUtil.getPassword())).blockingGet();
                    preferencesUtil.saveTOKEN(loginResModel.getData().getToken());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Request newRequest = recreateRequestWithNewAccessToken(chain, preferencesUtil);
                    return chain.proceed(newRequest);
                }

                return response;
            }
        };

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(loggingInterceptor);
        client.addInterceptor(interceptor);

        return client.build();
    }


    private Request recreateRequestWithNewAccessToken(Interceptor.Chain chain, PreferencesUtil preferencesUtil) {
        String freshAccessToken = preferencesUtil.getTOKEN();
        return chain.request().newBuilder()
                .header("Authorization", "Token " + freshAccessToken)
                .build();
    }

    @Provides
    @Singleton
    @Named("baseAPI")
    Retrofit provideBaseRetrofit2(Gson gson, @Named("baseAPI") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiUtils.getBaseUrl())
                .client(okHttpClient)
                .build();
    }

    // BaseServer API
//
//
//    @Provides
//    @Singleton
//    @Named("baseAPI")
//    OkHttpClient provideOkHttpClient3(PreferencesUtil preferencesUtil) {
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//
//                .connectTimeout(1, TimeUnit.MINUTES)
//                .readTimeout(5, TimeUnit.MINUTES)
//                .writeTimeout(5, TimeUnit.MINUTES)
//                .build();
//
//        return okHttpClient;
//    }


}
