package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
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
import com.toplevel.kengmakon.ui.dialogs.LoadingDialog;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.NetworkChangeListener;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

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

    private AlertDialog dialog;
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


        Utils.setAppLocale(this, preferencesUtil.getLANGUAGE());
        tagClickListener();
        authVM.login(preferencesUtil.getEmail(), preferencesUtil.getPassword());

        binding.sendMessageBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.title.getText().toString())
                    && !TextUtils.isEmpty(binding.body.getText().toString())) {
              //  progressDialog = ProgressDialog.show(this, "", "Loading...", true);
                showLoadingDialog();
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
        baseDialog.setTitle("Muroja'atingiz qabul qilindi", "Rahmat", "");
        baseDialog.makeBtnInvisible();
        baseDialog.changeBtnText("", "ok");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(baseDialog);
        AlertDialog dialogAlert = alertBuilder.create();
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlert.show();
        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClickOk() {
                BaseDialog.ClickListener.super.onClickOk();
                dialogAlert.dismiss();
                finish();

            }

            @Override
            public void onClickNo() {
                BaseDialog.ClickListener.super.onClickNo();
                dialogAlert.dismiss();
            }
        };
        baseDialog.setClickListener(clickListener);
    }

    public void onSuccessFeedback(BaseResponse response) {
      //  progressDialog.dismiss();
        dialog.dismiss();
        if (response.getCode() == 200) {
            showDialog();
        } else {
            binding.error.setText("Hatolik yuz berdi");
        }


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

    public void tagClickListener() {
        binding.firstTag.setOnClickListener(view -> {
            binding.firstTag.setBackground(getDrawable(R.drawable.txt_view_bgr));
            binding.secondTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.thirdTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fourthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fifthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.sixthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));

            binding.firstTag.setTextColor(Color.WHITE);
            binding.secondTag.setTextColor(Color.parseColor("#323232"));
            binding.thirdTag.setTextColor(Color.parseColor("#323232"));
            binding.fourthTag.setTextColor(Color.parseColor("#323232"));
            binding.fifthTag.setTextColor(Color.parseColor("#323232"));
            binding.sixthTag.setTextColor(Color.parseColor("#323232"));

            binding.title.setText(getString(R.string.mobile_app));
        });

        binding.secondTag.setOnClickListener(view -> {
            binding.firstTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.secondTag.setBackground(getDrawable(R.drawable.txt_view_bgr));
            binding.thirdTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fourthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fifthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.sixthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));

            binding.firstTag.setTextColor(Color.parseColor("#323232"));
            binding.secondTag.setTextColor(Color.WHITE);
            binding.thirdTag.setTextColor(Color.parseColor("#323232"));
            binding.fourthTag.setTextColor(Color.parseColor("#323232"));
            binding.fifthTag.setTextColor(Color.parseColor("#323232"));
            binding.sixthTag.setTextColor(Color.parseColor("#323232"));

            binding.title.setText(getString(R.string.delivery));

        });

        binding.thirdTag.setOnClickListener(view -> {
            binding.firstTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.secondTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.thirdTag.setBackground(getDrawable(R.drawable.txt_view_bgr));
            binding.fourthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fifthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.sixthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));

            binding.firstTag.setTextColor(Color.parseColor("#323232"));
            binding.secondTag.setTextColor(Color.parseColor("#323232"));
            binding.thirdTag.setTextColor(Color.WHITE);
            binding.fourthTag.setTextColor(Color.parseColor("#323232"));
            binding.fifthTag.setTextColor(Color.parseColor("#323232"));
            binding.sixthTag.setTextColor(Color.parseColor("#323232"));


            binding.title.setText(getString(R.string.personal));
        });

        binding.fourthTag.setOnClickListener(view -> {
            binding.firstTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.secondTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.thirdTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fourthTag.setBackground(getDrawable(R.drawable.txt_view_bgr));
            binding.fifthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.sixthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));

            binding.firstTag.setTextColor(Color.parseColor("#323232"));
            binding.secondTag.setTextColor(Color.parseColor("#323232"));
            binding.thirdTag.setTextColor(Color.parseColor("#323232"));
            binding.fourthTag.setTextColor(Color.WHITE);
            binding.fifthTag.setTextColor(Color.parseColor("#323232"));
            binding.sixthTag.setTextColor(Color.parseColor("#323232"));

            binding.title.setText(getString(R.string.service));

        });

        binding.fifthTag.setOnClickListener(view -> {
            binding.firstTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.secondTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.thirdTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fourthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fifthTag.setBackground(getDrawable(R.drawable.txt_view_bgr));
            binding.sixthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));

            binding.firstTag.setTextColor(Color.parseColor("#323232"));
            binding.secondTag.setTextColor(Color.parseColor("#323232"));
            binding.thirdTag.setTextColor(Color.parseColor("#323232"));
            binding.fourthTag.setTextColor(Color.parseColor("#323232"));
            binding.fifthTag.setTextColor(Color.WHITE);
            binding.sixthTag.setTextColor(Color.parseColor("#323232"));

            binding.title.setText(getString(R.string.call_center));


        });

        binding.sixthTag.setOnClickListener(view -> {
            binding.firstTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.secondTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.thirdTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fourthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.fifthTag.setBackground(getDrawable(R.drawable.txt_gray_bgr));
            binding.sixthTag.setBackground(getDrawable(R.drawable.txt_view_bgr));

            binding.firstTag.setTextColor(Color.parseColor("#323232"));
            binding.secondTag.setTextColor(Color.parseColor("#323232"));
            binding.thirdTag.setTextColor(Color.parseColor("#323232"));
            binding.fourthTag.setTextColor(Color.parseColor("#323232"));
            binding.fifthTag.setTextColor(Color.parseColor("#323232"));
            binding.sixthTag.setTextColor(Color.WHITE);

            binding.title.setText(getString(R.string.other));


        });
    }

    public void onFailFeedback(String error) {
       // progressDialog.dismiss();
        dialog.dismiss();
        binding.error.setText("Hatolik yuz berdi");
    }

    public void onLoginSuccess(LoginModel.LoginResModel model) {

        if (model.getCode() == 200) {

            preferencesUtil.saveTOKEN(model.getData().getToken());

            //authVM.getUserInfo(model.getData().getToken());

        }
    }

    public void onFailLogin(String error) {

    }

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
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
