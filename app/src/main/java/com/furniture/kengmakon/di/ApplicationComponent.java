package com.furniture.kengmakon.di;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.ui.ChooseLanguageActivity;
import com.furniture.kengmakon.ui.CreateAccountActivity;
import com.furniture.kengmakon.ui.GetStartedActivity;
import com.furniture.kengmakon.ui.LoginActivity;
import com.furniture.kengmakon.ui.fragments.ActionsFragment;
import com.furniture.kengmakon.ui.fragments.HomeFragment;
import com.furniture.kengmakon.ui.fragments.NewCashbackFragment;
import com.furniture.kengmakon.ui.fragments.SettingsFragment;
import com.furniture.kengmakon.ui.AboutActivity;
import com.furniture.kengmakon.ui.ActionsDetailActivity;
import com.furniture.kengmakon.ui.BaseActivity;
import com.furniture.kengmakon.ui.BranchDetailActivity;
import com.furniture.kengmakon.ui.BranchesActivity;
import com.furniture.kengmakon.ui.CashbackDetailActivity;
import com.furniture.kengmakon.ui.CategoryDetailActivity;
import com.furniture.kengmakon.ui.DiscountDetailActivity;
import com.furniture.kengmakon.ui.EditAccountActivity;
import com.furniture.kengmakon.ui.FeedbackActivity;
import com.furniture.kengmakon.ui.ForgotPasswordActivity;
import com.furniture.kengmakon.ui.FurnitureDetailActivity;
import com.furniture.kengmakon.ui.MainActivity;
import com.furniture.kengmakon.ui.NotificationDetailActivity;
import com.furniture.kengmakon.ui.NotificationsActivity;
import com.furniture.kengmakon.ui.OrderDetailActivity;
import com.furniture.kengmakon.ui.OrdersActivity;
import com.furniture.kengmakon.ui.SearchActivity;
import com.furniture.kengmakon.ui.SetDetailActivity;
import com.furniture.kengmakon.ui.SplashActivity;
import com.furniture.kengmakon.ui.fragments.CashbackFragment;
import com.furniture.kengmakon.ui.fragments.WishlistFragment;
import com.furniture.kengmakon.utils.MyFirebaseService;

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
    void inject(CashbackFragment fragment);
    void inject(CashbackDetailActivity activity);
    void inject(DiscountDetailActivity activity);
    void inject(NewCashbackFragment fragment);
    void inject(ActionsFragment fragment);
    void inject(OrdersActivity activity);
    void inject(OrderDetailActivity activity);
    void inject(BranchesActivity activity);
    void inject(FurnitureDetailActivity activity);
    void inject(NotificationsActivity activity);
    void inject(MyFirebaseService activity);
    void inject(AboutActivity activity);
    void inject(ActionsDetailActivity activity);
    void inject(BranchDetailActivity activity);
    void inject(ForgotPasswordActivity activity);
    void inject(EditAccountActivity activity);
    void inject(NotificationDetailActivity activity);
    void inject(SearchActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
