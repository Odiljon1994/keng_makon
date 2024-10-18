package com.toplevel.kengmakon.di;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.ui.AboutActivity;
import com.toplevel.kengmakon.ui.ActionsDetailActivity;
import com.toplevel.kengmakon.ui.BaseActivity;
import com.toplevel.kengmakon.ui.BranchDetailActivity;
import com.toplevel.kengmakon.ui.BranchesActivity;
import com.toplevel.kengmakon.ui.CashbackDetailActivity;
import com.toplevel.kengmakon.ui.CategoryDetailActivity;
import com.toplevel.kengmakon.ui.ChooseLanguageActivity;
import com.toplevel.kengmakon.ui.CreateAccountActivity;
import com.toplevel.kengmakon.ui.DiscountDetailActivity;
import com.toplevel.kengmakon.ui.EditAccountActivity;
import com.toplevel.kengmakon.ui.FeedbackActivity;
import com.toplevel.kengmakon.ui.ForgotPasswordActivity;
import com.toplevel.kengmakon.ui.FurnitureDetailActivity;
import com.toplevel.kengmakon.ui.GetStartedActivity;
import com.toplevel.kengmakon.ui.LoginActivity;
import com.toplevel.kengmakon.ui.MainActivity;
import com.toplevel.kengmakon.ui.NotificationDetailActivity;
import com.toplevel.kengmakon.ui.NotificationsActivity;
import com.toplevel.kengmakon.ui.OrderDetailActivity;
import com.toplevel.kengmakon.ui.OrdersActivity;
import com.toplevel.kengmakon.ui.SearchActivity;
import com.toplevel.kengmakon.ui.SetDetailActivity;
import com.toplevel.kengmakon.ui.SplashActivity;
import com.toplevel.kengmakon.ui.fragments.ActionsFragment;
import com.toplevel.kengmakon.ui.fragments.CashbackFragment;
import com.toplevel.kengmakon.ui.fragments.HomeFragment;
import com.toplevel.kengmakon.ui.fragments.NewCashbackFragment;
import com.toplevel.kengmakon.ui.fragments.SettingsFragment;
import com.toplevel.kengmakon.ui.fragments.WishlistFragment;
import com.toplevel.kengmakon.utils.MyFirebaseService;

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
