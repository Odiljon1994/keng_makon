package com.furniture.kengmakon.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.furniture.kengmakon.entity.User;
import com.furniture.kengmakon.utils.PreferencesUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApplication;
    }

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    PreferencesUtil providePreferenceUtil(Context context, SharedPreferences sharedPreferences) {
        return new PreferencesUtil(context, sharedPreferences);
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences("KengMakonPrefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    User providesUser(Gson gson, Context context, PreferencesUtil preferenceUtil) {
        return new User(context, gson, preferenceUtil);
    }


}
