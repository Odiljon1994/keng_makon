package com.toplevel.kengmakon;

import android.app.Activity;
import android.app.Application;

import com.toplevel.kengmakon.di.AppModule;
import com.toplevel.kengmakon.di.ApplicationComponent;
import com.toplevel.kengmakon.di.DaggerApplicationComponent;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MyApp extends Application implements HasActivityInjector {

    private ApplicationComponent mAppComponent;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();



        //  RxJavaPlugins.setErrorHandler(throwable -> {});
        mAppComponent = DaggerApplicationComponent.builder()
                .application(this)
                .appModule(new AppModule(this))
                .build();

        mAppComponent.inject(this);

    }

    public ApplicationComponent getAppComponent(){
        return mAppComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
