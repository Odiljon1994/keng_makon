package com.toplevel.kengmakon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityBranchDetailBinding;
import com.toplevel.kengmakon.ui.adapters.BranchDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class BranchDetailActivity extends AppCompatActivity {

    private BranchDetailAdapter adapter;
    private ActivityBranchDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch_detail);

        binding.backBtn.setOnClickListener(view -> finish());



        String address = getIntent().getStringExtra("address");
        String working_hours = getIntent().getStringExtra("working_hours");
        String phone_number = getIntent().getStringExtra("phone_number");
        int is_parking_exist = getIntent().getIntExtra("is_parking_exist", 0);
        String langtitude_longtitude = getIntent().getStringExtra("langtitude_longtitude");
        List<String> imageUrls = getIntent().getStringArrayListExtra("images");
        if (TextUtils.isEmpty(langtitude_longtitude)) {
            binding.showOnMapBtn.setVisibility(View.GONE);
        }

        binding.showOnMapBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(langtitude_longtitude)) {
                Intent intent = new Intent(BranchDetailActivity.this, BranchLocationActivity.class);
                intent.putExtra("langtitude_longtitude", langtitude_longtitude);
                startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(address)) {
            binding.addressTextView.setText(address);
        }
        if (!TextUtils.isEmpty(working_hours)) {
            binding.workingHoursTextView.setText(working_hours);
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
