package com.toplevel.kengmakon.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        binding.backBtn.setOnClickListener(view -> finish());
    }
}
