package com.toplevel.kengmakon.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityActionDetailBinding;

public class ActionsDetailActivity extends AppCompatActivity {

    private ActivityActionDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_action_detail);

        binding.backBtn.setOnClickListener(view -> finish());

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        String file_name = getIntent().getStringExtra("file_name");
        String category = getIntent().getStringExtra("type");

        if (!TextUtils.isEmpty(category)) {
            if (category.equals("EVENT")) {
                binding.title.setText(getString(R.string.events));
            }
        }

        if (!TextUtils.isEmpty(file_name)) {
            Glide.with(this).load("http://144.202.7.226:8080" + file_name).centerCrop().into(binding.image);
        }

        if (!TextUtils.isEmpty(title)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.title.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.title.setText(Html.fromHtml(title));
            }
        }

        if (!TextUtils.isEmpty(description)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.description.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.description.setText(Html.fromHtml(description));
            }
        }

        try {
            String[] datePairs = date.split("T");
            binding.date.setText(datePairs[0]);
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
