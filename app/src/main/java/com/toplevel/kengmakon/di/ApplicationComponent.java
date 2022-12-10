package com.toplevel.kengmakon.di;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.ui.BaseActivity;
import com.toplevel.kengmakon.ui.CategoryDetailActivity;
import com.toplevel.kengmakon.ui.ChooseLanguageActivity;
import com.toplevel.kengmakon.ui.CreateAccountActivity;
import com.toplevel.kengmakon.ui.FeedbackActivity;
import com.toplevel.kengmakon.ui.GetStartedActivity;
import com.toplevel.kengmakon.ui.LoginActivity;
import com.toplevel.kengmakon.ui.MainActivity;
import com.toplevel.kengmakon.ui.SetDetailActivity;
import com.toplevel.kengmakon.ui.SplashActivity;
import com.toplevel.kengmakon.ui.fragments.HomeFragment;
import com.toplevel.kengmakon.ui.fragments.SettingsFragment;
import com.toplevel.kengmakon.ui.fragments.WishlistFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        NetModule.class,
        ViewModelModule.class})
public interface ApplicationComponent {
    void inject(MyApp activity);
    void inject(SplashActivity activity);
    void inject(ChooseLanguageActivity activity);
    void inject(LoginActivity activity);
    void inject(GetStartedActivity activity);
    void inject(MainActivity activity);
    void inject(CreateAccountActivity activity);
    void inject(SetDetailActivity activity);
    void inject(CategoryDetailActivity activity);
    void inject(HomeFragment fragment);
    void inject(WishlistFragment fragment);
    void inject(BaseActivity activity);
    void inject(SettingsFragment fragment);
    void inject(FeedbackActivity activity);


    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
