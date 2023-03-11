package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityForgotPasswordBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.dialogs.LoadingDialog;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.ui.viewmodels.EditAccountVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private EditAccountVM editAccountVM;
    private ProgressDialog progressDialog;
    private AlertDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        editAccountVM = ViewModelProviders.of(this, viewModelFactory).get(EditAccountVM.class);
        editAccountVM.forgetPasswordLiveData().observe(this, this::onSuccessReqPassword);
        editAccountVM.onFailForgetPasswordLiveData().observe(this, this::onFailReqPassword);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.reqNewPwdBtn.setOnClickListener(view -> {
            if (isValidEmailAddress(binding.email.getText().toString())) {
              //  progressDialog = ProgressDialog.show(this, "", "Loading...", true);
                showLoadingDialog();
                editAccountVM.postForgetPassword(binding.email.getText().toString());
            } else {
                binding.email.setError("");
            }
        });

    }

    public void showDialog(String title, String message) {
        BaseDialog baseDialog = new BaseDialog(this);
        baseDialog.setTitle(title, "", message);
        baseDialog.makeBtnInvisible();
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

    public void showLoadingDialog() {
        LoadingDialog loadingDialog = new LoadingDialog(this);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
        alertBuilder.setView(loadingDialog);
        dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private boolean isValidEmailAddress(String emailAddress) {
        if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            return true;
        }
        return false;
    }
    public void onSuccessReqPassword(BaseResponse response) {
       // progressDialog.dismiss();
        dialog.dismiss();
        if (response.getCode() == 200) {
            showDialog(getString(R.string.success_change_pwd), getString(R.string.new_pwd_has_sent));
        } else if (response.getCode() == 1002) {
            showDialog(getString(R.string.not_registered_email), "");
        }
        else {
            binding.error.setText("Tizimda hatolik yuz berdi. " + response.getMessage());
        }
    }

    public void onFailReqPassword(String error) {
      //  progressDialog.dismiss();
        dialog.dismiss();
        binding.error.setText("Tizimda hatolik yuz berdi. " + error);
    }
}
