package com.toplevel.kengmakon.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivitySplashBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

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

//        if (!TextUtils.isEmpty(preferencesUtil.getEmail())
//                && !TextUtils.isEmpty(preferencesUtil.getPassword())) {
//            authVM.login(preferencesUtil.getEmail(), preferencesUtil.getPassword());
//        }
        //preferencesUtil.saveTOKEN("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNzI5NjQxOWZlYzZlYzMyNWM5NWZiMTg0MGUyNDBlOTkxNThiZmExYTMzMDYxYTU2Y2ViMzY5OGExNTdhNWU1Nzg3ZWEzMzUzYTlmMzhjNmUiLCJpYXQiOjE2NzIxNjI4ODAuMzg0OTg4LCJuYmYiOjE2NzIxNjI4ODAuMzg0OTg5LCJleHAiOjE2NzIxNzAwODAuMzgzNzk0LCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.MWaMiv9tftY9b9UyV7Tet4FB6GV7GSMvA4r75uLJDn6pT4HgBwOyrc2Z4Vm5VekKX8untBPFLKbSkdllBzPX1_ib0_FYIatdna38NUF_O6wasAvhtBApn0GFM5DDD_SBOeh0utPx0G3jiKiiYG7Ag5KyYm4MNv--13wfvLMPeXGlZSqBXctatESNG8-Jy4Foc8ivUWxJyE44NlteqPyAZKvDIY3WL735CSSSJE4PpPndTyylv-gOqDpASfUiNOzq7ol3_yMA4PFu-8_6vNfMniGSp8S86k-ybJQ4HSU3WNfK4yKJqRCbyphgbvkmQljX1XBa3tgqWRtoBFi8Oti7NxiIWSegRbZtYWcgVcK81DItfTdOuiHhvA5Xj-Wfx7Q0uqPfh-DdRjoSAx7M5V3cdn8MPg_1kpkfVYJRqXQCJAalBT653uCAH_THWeo2yd06nu9iHCh2Gq4H2OxtL73EGtrr1NLlaRjA7VPDMe-QPRYRP7UFsABoAY7YwzzpCi0DKFb1jkmYJn_purIIpHqYtI0CNbGkmPzwcClryxZ0G5NQ23VTt8uj01OeJyNCqyXQfT0fpmqF6IhVa4KokDjNy7O8HxgofshkLIxIMFc-Sy_-TxtGoEdtiVKZNghE0aTddmXgfn00e6DBzJfxS7a17RFD2ZMekkvckndfvfgi2Vc");
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
