package com.toplevel.kengmakon.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivitySplashBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;

    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;

    AuthVM authVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        authVM = ViewModelProviders.of(this, viewModelFactory).get(AuthVM.class);
        authVM.loginModelLiveData().observe(this, this::onLoginSuccess);
        authVM.onFailLoginLiveData().observe(this, this::onFailLogin);

        System.out.println("EMAIL: " + preferencesUtil.getEmail());
        if (!TextUtils.isEmpty(preferencesUtil.getEmail())
                && !TextUtils.isEmpty(preferencesUtil.getPassword())) {
            authVM.login(preferencesUtil.getEmail(), preferencesUtil.getPassword());
        }
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash_video;
        Uri uri = Uri.parse(videoPath);
        binding.videoView.setVideoURI(uri);
        binding.videoView.start();

        System.out.println("TOKEN: " + preferencesUtil.getTOKEN());
        if (preferencesUtil.getLANGUAGE().equals("") || preferencesUtil.getLANGUAGE().equals("uz")) {
            Utils.setAppLocale(this, "uz");
        } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
            Utils.setAppLocale(this, "ru");
        } else {
            Utils.setAppLocale(this, "en");
        }

        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                binding.videoView.start();
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> {


            if (preferencesUtil.getLANGUAGE().equals("")) {
                startActivity(new Intent(SplashActivity.this, ChooseLanguageActivity.class));
            } else if (!preferencesUtil.getIsGetStartedDone()) {
                startActivity(new Intent(SplashActivity.this, GetStartedActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }


            finish();

        }, 2700);
    }

    public void onLoginSuccess(LoginModel.LoginResModel model) {

        if (model.getCode() == 200) {

            preferencesUtil.saveTOKEN(model.getData().getToken());

            //authVM.getUserInfo(model.getData().getToken());

        }
    }

    public void onFailLogin(String error) {

    }
}
