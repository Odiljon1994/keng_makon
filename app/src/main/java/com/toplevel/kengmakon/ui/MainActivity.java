package com.toplevel.kengmakon.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
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
import com.toplevel.kengmakon.ui.fragments.HomeFragment;
import com.toplevel.kengmakon.ui.fragments.WishlistFragment;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    private HomeFragment homeFragment;
    private WishlistFragment wishlistFragment;
    private String appUniqueToken = "";
    private AuthVM authVM;
    @Inject
    ViewModelFactory viewModelFactory;
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
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, wishlistFragment).commit();

        if (!preferencesUtil.getIsPushTokenDone()) {
            getAppUniqueToken();
        }
        changeState(1);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    changeState(1);
                    break;
                case R.id.wishlist:
                    changeState(2);
                    break;
                case R.id.chat:

                    break;
                case R.id.profile:

                    break;
            }
            return true;
        });
        binding.cashbackBtn.setOnClickListener(view -> {
            Toast.makeText(this, "Cashback", Toast.LENGTH_SHORT).show();
        });
    }

    public void changeState(int position) {
        if (position == 1) {
            getSupportFragmentManager().beginTransaction().hide(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().show(homeFragment).commit();

        } else if (position == 2) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().show(wishlistFragment).commit();
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
}