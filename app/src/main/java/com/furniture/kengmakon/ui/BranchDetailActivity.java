package com.furniture.kengmakon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.ui.adapters.BranchDetailAdapter;
import com.furniture.kengmakon.utils.PreferencesUtil;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityBranchDetailBinding;

import java.util.List;

import javax.inject.Inject;

public class BranchDetailActivity extends AppCompatActivity {

    private BranchDetailAdapter adapter;
    private ActivityBranchDetailBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch_detail);

        binding.backBtn.setOnClickListener(view -> finish());



        String address = getIntent().getStringExtra("address");
        String working_hours = getIntent().getStringExtra("working_hours");
        String phone_number = getIntent().getStringExtra("phone_number");
        String landmark = getIntent().getStringExtra("landmark");

        int is_parking_exist = getIntent().getIntExtra("is_parking_exist", 0);
        String langtitude_longtitude = getIntent().getStringExtra("langtitude_longtitude");
        List<String> imageUrls = getIntent().getStringArrayListExtra("images");
        if (TextUtils.isEmpty(langtitude_longtitude)) {
            binding.showOnMapBtn.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(landmark)) {
            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(landmark,  PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz")) {
                binding.landMarkTextView.setText(pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                binding.landMarkTextView.setText(pushNotificationTitleModel.getRu());
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                binding.landMarkTextView.setText(pushNotificationTitleModel.getEn());
            }

        }

        binding.showOnMapBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(langtitude_longtitude)) {
                Intent intent = new Intent(BranchDetailActivity.this, BranchLocationActivity.class);
                intent.putExtra("langtitude_longtitude", langtitude_longtitude);
                startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(address)) {
            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(address,  PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz")) {
                binding.addressTextView.setText(pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                binding.addressTextView.setText(pushNotificationTitleModel.getRu());
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                binding.addressTextView.setText(pushNotificationTitleModel.getEn());
            }

        }
        if (!TextUtils.isEmpty(working_hours)) {
            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(working_hours,  PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz")) {
                binding.workingHoursTextView.setText(pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                binding.workingHoursTextView.setText(pushNotificationTitleModel.getRu());
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                binding.workingHoursTextView.setText(pushNotificationTitleModel.getEn());
            }
            //binding.workingHoursTextView.setText(working_hours);
        }
        if (!TextUtils.isEmpty(phone_number)) {
            binding.phoneNumberTextView.setText(phone_number);
        }
        if (is_parking_exist == 1) {
            binding.isParkingExist.setVisibility(View.VISIBLE);
        }
        adapter = new BranchDetailAdapter(this, imageUrls);
        binding.viewPager.setAdapter(adapter);
        binding.dots.setViewPager(binding.viewPager);

    }
}
