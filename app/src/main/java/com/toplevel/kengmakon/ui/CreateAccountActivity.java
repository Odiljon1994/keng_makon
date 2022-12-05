package com.toplevel.kengmakon.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityCreateAccountBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private ProgressDialog progressDialog;

    AuthVM authVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);
        authVM = ViewModelProviders.of(this, viewModelFactory).get(AuthVM.class);
        authVM.signUpLiveData().observe(this, this::onSuccessSignUp);
        authVM.onFailSignUpLiveData().observe(this, this::onFailSignUp);
        authVM.loginModelLiveData().observe(this, this::onLoginSuccess);
        authVM.onFailLoginLiveData().observe(this, this::onFailLogin);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.createAccountBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.name.getText().toString())
                    && !TextUtils.isEmpty(binding.email.getText().toString())
                    && isValidEmailAddress(binding.email.getText().toString())
                    && !TextUtils.isEmpty(binding.password.getText().toString())
                    && binding.password.getText().toString().length() >= 6
                    && binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())) {
                progressDialog = ProgressDialog.show(this, "", "Loading...", true);
                authVM.signUp(binding.name.getText().toString(),
                        binding.phoneNumber.getText().toString(),
                        binding.email.getText().toString(),
                        binding.password.getText().toString());
            } else if (TextUtils.isEmpty(binding.email.getText().toString())) {
                binding.email.setError("");
            } else if (!isValidEmailAddress(binding.email.getText().toString())) {
                binding.email.setError("");
            } else if (TextUtils.isEmpty(binding.password.getText().toString())) {
                binding.password.setError("");
            } else if (TextUtils.isEmpty(binding.confirmPassword.getText().toString())) {
                binding.confirmPassword.setError("");
            } else if (binding.password.getText().toString().length() < 6) {
                binding.error.setText("Parol 6 yoki undan ko'p belgidan iborat bo'lishi kerak!");
            } else if (TextUtils.isEmpty(binding.name.getText().toString())) {
                binding.name.setError("");
            }
        });
    }

    public void onSuccessSignUp(BaseResponse response) {
    //    progressDialog.dismiss();
        if (response.getCode() == 200) {
            preferencesUtil.saveName(binding.name.getText().toString());
            preferencesUtil.saveEmail(binding.email.getText().toString());
            preferencesUtil.savePassword(binding.password.getText().toString());
            preferencesUtil.savePhoneNumber(binding.phoneNumber.getText().toString());
            preferencesUtil.saveIsSignedIn(true);
            authVM.login(preferencesUtil.getEmail(), preferencesUtil.getPassword());
//            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
        } else {
            progressDialog.dismiss();
            binding.error.setText(response.getMessage());
        }
    }

    public void onFailSignUp(String error) {
        progressDialog.dismiss();
        binding.error.setText(error);
    }

    private boolean isValidEmailAddress(String emailAddress) {
        if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            return true;
        }
        return false;
    }

    public void onLoginSuccess(LoginModel.LoginResModel model) {
        progressDialog.dismiss();
        if (model.getCode() == 200) {
            preferencesUtil.saveTOKEN(model.getData().getToken());
        }
        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onFailLogin(String error) {
        progressDialog.dismiss();
        binding.error.setText(error);
    }
}
