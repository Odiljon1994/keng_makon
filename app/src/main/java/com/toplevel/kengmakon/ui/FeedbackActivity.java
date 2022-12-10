package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityFeedbackBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private ProgressDialog progressDialog;
    private UserVM userVM;
    private AuthVM authVM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        userVM = ViewModelProviders.of(this, viewModelFactory).get(UserVM.class);
        userVM.onSuccessFeedbackLiveData().observe(this, this::onSuccessFeedback);
        userVM.onFailFeedbackLiveData().observe(this, this::onFailFeedback);
        authVM = ViewModelProviders.of(this, viewModelFactory).get(AuthVM.class);
        authVM.loginModelLiveData().observe(this, this::onLoginSuccess);
        authVM.onFailLoginLiveData().observe(this, this::onFailLogin);
        binding.backBtn.setOnClickListener(view -> finish());

        authVM.login(preferencesUtil.getEmail(), preferencesUtil.getPassword());

        binding.sendMessageBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.title.getText().toString())
                    && !TextUtils.isEmpty(binding.body.getText().toString())) {
                progressDialog = ProgressDialog.show(this, "", "Loading...", true);
                userVM.postFeedback(preferencesUtil.getTOKEN(), binding.title.getText().toString(), binding.body.getText().toString());
            } else if (TextUtils.isEmpty(binding.title.getText().toString())) {
                binding.title.setError("");
            } else if (TextUtils.isEmpty(binding.body.getText().toString())) {
                binding.body.setError("");
            }
        });

    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        baseDialog.setTitle("Muroja'atingiz qabul qilindi", "Rahmat");
        baseDialog.makeBtnInvisible();
        baseDialog.changeBtnText("", "ok");
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
                finish();

            }

            @Override
            public void onClickNo() {
                BaseDialog.ClickListener.super.onClickNo();
                dialog.dismiss();
            }
        };
        baseDialog.setClickListener(clickListener);
    }

    public void onSuccessFeedback(BaseResponse response) {
        progressDialog.dismiss();
        if (response.getCode() == 200) {
            showDialog();
        }


    }

    public void onFailFeedback(String error) {
        progressDialog.dismiss();
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
