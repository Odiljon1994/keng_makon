package com.toplevel.kengmakon.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityMainBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.fragments.ActionsFragment;
import com.toplevel.kengmakon.ui.fragments.CashbackFragment;
import com.toplevel.kengmakon.ui.fragments.HomeFragment;
import com.toplevel.kengmakon.ui.fragments.NewCashbackFragment;
import com.toplevel.kengmakon.ui.fragments.SettingsFragment;
import com.toplevel.kengmakon.ui.fragments.WishlistFragment;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.LanguageChangeListener;
import com.toplevel.kengmakon.utils.NetworkChangeListener;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    private HomeFragment homeFragment;
    private WishlistFragment wishlistFragment;
    private SettingsFragment settingsFragment;
    private NewCashbackFragment cashbackFragment;
    private ActionsFragment actionsFragment;
    private String appUniqueToken = "";
    private AuthVM authVM;
    private int state = 1;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Inject
    ViewModelFactory viewModelFactory;
    private LanguageChangeListener languageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        authVM = ViewModelProviders.of(this, viewModelFactory).get(AuthVM.class);
        authVM.onSuccessPushTokenLiveData().observe(this, this::onSuccessPushToken);
        authVM.onFailPushTokenLiveData().observe(this, this::onFailPushToken);

        Utils.setAppLocale(this, preferencesUtil.getLANGUAGE());
        homeFragment = new HomeFragment();
        wishlistFragment = new WishlistFragment();
        cashbackFragment = new NewCashbackFragment();
        settingsFragment = new SettingsFragment();
        actionsFragment = new ActionsFragment();

        subscribeTopics();

        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, wishlistFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, cashbackFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, settingsFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, actionsFragment).commit();


        binding.bottomNavigationView.setItemIconTintList(null);
        if (!preferencesUtil.getIsPushTokenDone() && preferencesUtil.getIsIsSignedIn()) {
            getAppUniqueToken();
        }
        changeState(1);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    changeState(1);
                    state = 1;
                    break;
                case R.id.wishlist:

                    if (preferencesUtil.getIsIsSignedIn()) {
                        changeState(2);
                        state = 2;
                    } else {
                        showDialog();
                    }
                    break;
                case R.id.cashback:
                    if (preferencesUtil.getIsIsSignedIn()) {
                        changeState(3);
                        state = 3;
                    } else {
                        showDialog();
                    }

                    break;
                case R.id.chat:
                    changeState(4);
                    state = 5;
                    break;
                case R.id.profile:
                    changeState(5);
                    state = 5;
                    break;
            }
            return true;
        });

        languageChangeListener = () -> {
            Utils.setAppLocale(MainActivity.this, preferencesUtil.getLANGUAGE());
            getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(settingsFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(actionsFragment).commit();
            homeFragment = new HomeFragment();
            wishlistFragment = new WishlistFragment();
            cashbackFragment = new NewCashbackFragment();
            settingsFragment = new SettingsFragment();
            actionsFragment = new ActionsFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, homeFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, settingsFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, actionsFragment).commit();


            settingsFragment.setLanguageChangeListener(languageChangeListener);
            changeState(5);
        };
        settingsFragment.setLanguageChangeListener(languageChangeListener);
//        binding.cashbackBtn.setOnClickListener(view -> {
//            Toast.makeText(this, "Cashback", Toast.LENGTH_SHORT).show();
//        });
    }

    public void changeState(int position) {
        if (position == 1) {
            getSupportFragmentManager().beginTransaction().hide(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(actionsFragment).commit();
            getSupportFragmentManager().beginTransaction().show(homeFragment).commit();

        } else if (position == 2) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(actionsFragment).commit();
            getSupportFragmentManager().beginTransaction().show(wishlistFragment).commit();
        } else if (position == 3) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().show(cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(actionsFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();
        } else if (position == 4) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().show(actionsFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();
        } else if (position == 5) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(cashbackFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(actionsFragment).commit();
            getSupportFragmentManager().beginTransaction().show(settingsFragment).commit();
        }
    }

    public void onSuccessPushToken(BaseResponse model) {
        if (model.getCode() == 200) {
            System.out.println("Success");
            preferencesUtil.saveIsPushTokenDone(true);
        }
    }

    public void onFailPushToken(String error) {

    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        baseDialog.setTitle(getString(R.string.not_logged_in), getString(R.string.want_to_login), "");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClickOk() {
                BaseDialog.ClickListener.super.onClickOk();
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }

            @Override
            public void onClickNo() {
                BaseDialog.ClickListener.super.onClickNo();
                dialog.dismiss();
                if (state == 1) {
                    binding.bottomNavigationView.setSelectedItemId(R.id.home);
                } else if (state == 2) {
                    binding.bottomNavigationView.setSelectedItemId(R.id.wishlist);
                } else if (state == 3) {
                    binding.bottomNavigationView.setSelectedItemId(R.id.chat);
                } else if (state == 4) {
                    binding.bottomNavigationView.setSelectedItemId(R.id.profile);
                }
            }
        };
        baseDialog.setClickListener(clickListener);
    }

    public void getAppUniqueToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println("** ** Token: " + token);

                        appUniqueToken = token;
                        authVM.pushToken(preferencesUtil.getTOKEN(), appUniqueToken);

                        // Log and toast
//                        String msg = getString("Token", token);
//                        Log.d("TAG", msg);

                    }
                });
    }

    private void subscribeTopics() {
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d("TAG", msg);
                    }
                });
        // [END subscribe_topics]
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}