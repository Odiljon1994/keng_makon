package com.toplevel.kengmakon.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityNotificationDetailBinding;

public class NotificationDetailActivity extends AppCompatActivity {

    private ActivityNotificationDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_detail);

        binding.backBtn.setOnClickListener(view -> finish());

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        try {
            binding.name.setText(title);
            binding.description.setText(description);
            String datePairs[] = date.split("T");
            binding.date.setText(datePairs[0]);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.description.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.description.setText(Html.fromHtml(description));
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
