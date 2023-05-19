package com.toplevel.kengmakon.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityZoomImageBinding;

public class ZoomImageActivity extends AppCompatActivity {

    private ActivityZoomImageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_zoom_image);
        String url = getIntent().getStringExtra("url");

        binding.image.setZoom(1);

        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(binding.image);
        }

        binding.backBtn.setOnClickListener(view -> finish());
    }
}
