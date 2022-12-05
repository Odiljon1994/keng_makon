package com.toplevel.kengmakon.di;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.api.ApiUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
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


    // BaseServer API

    @Provides
    @Singleton
    @Named("baseAPI")
    OkHttpClient provideOkHttpClient2() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(loggingInterceptor);
        return client.build();
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

    @Provides
    @Singleton
    Api provideBaseApi(@Named("baseAPI") Retrofit retrofit) {
        return retrofit.create(Api.class);
    }


}
