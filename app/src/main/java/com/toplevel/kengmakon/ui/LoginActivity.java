package com.toplevel.kengmakon.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

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
import com.toplevel.kengmakon.databinding.ActivityLoginBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.models.UserInfoModel;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private ProgressDialog progressDialog;
    private String appUniqueToken = "";

    AuthVM authVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        authVM = ViewModelProviders.of(this, viewModelFactory).get(AuthVM.class);
        authVM.loginModelLiveData().observe(this, this::onLoginSuccess);
        authVM.onFailLoginLiveData().observe(this, this::onFailLogin);
        authVM.userInfoSuccessLiveData().observe(this, this::onSuccessUserInfo);
        authVM.onFailUserInfoLiveData().observe(this, this::onFailUserInfo);
        authVM.onSuccessPushTokenLiveData().observe(this, this::onSuccessPushToken);
        authVM.onFailPushTokenLiveData().observe(this, this::onFailPushToken);

        binding.signUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        });
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video_llop;
        Uri uri = Uri.parse(videoPath);
        binding.videoView.setVideoURI(uri);
        binding.videoView.start();

        binding.forgotPasswordBtn.setOnClickListener(view -> {

        });

        binding.loginBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.email.getText().toString())
                    && !TextUtils.isEmpty(binding.password.getText().toString())) {
                progressDialog = ProgressDialog.show(this, "", "Loading...", true);
                authVM.login(binding.email.getText().toString(), binding.password.getText().toString());
            } else if (TextUtils.isEmpty(binding.email.getText().toString())) {
                binding.email.setError("");
            } else if (TextUtils.isEmpty(binding.email.getText().toString())) {
                binding.password.setError("");
            }
        });

        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video_llop;
        Uri uri = Uri.parse(videoPath);
        binding.videoView.setVideoURI(uri);
        binding.videoView.start();

    }

    public void onLoginSuccess(LoginModel.LoginResModel model) {

        if (model.getCode() == 200) {

            preferencesUtil.saveTOKEN(model.getData().getToken());
            preferencesUtil.saveIsSignedIn(true);
            preferencesUtil.saveEmail(binding.email.getText().toString());
            preferencesUtil.savePassword(binding.password.getText().toString());
            preferencesUtil.saveUserId(model.getData().getUser().getId());
            preferencesUtil.saveName(model.getData().getUser().getName());

            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }.start();

//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
            //authVM.getUserInfo(model.getData().getToken());

        } else if (model.getCode() == 1000) {
            progressDialog.dismiss();
            binding.error.setText(getResources().getString(R.string.incorrect_login_details));
        }
        else {
            progressDialog.dismiss();
            binding.error.setText(model.getMessage());
        }
    }

    public void onFailLogin(String error) {
        progressDialog.dismiss();
        binding.error.setText(error);
    }

    public void onSuccessUserInfo(UserInfoModel model) {
        progressDialog.dismiss();
        if (model.getCode() == 200) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            binding.error.setText(model.getMessage());
        }
    }
    public void onFailUserInfo(String error) {
        progressDialog.dismiss();
        binding.error.setText(error);
    }

    public void onSuccessPushToken(BaseResponse model) {

    }
    public void onFailPushToken(String error) {

    }
}
