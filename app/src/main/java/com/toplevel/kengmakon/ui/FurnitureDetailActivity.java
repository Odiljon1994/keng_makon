package com.toplevel.kengmakon.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityFurnitureDetailBinding;
import com.toplevel.kengmakon.models.FurnitureModel;

public class FurnitureDetailActivity extends AppCompatActivity {

    private ActivityFurnitureDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_furniture_detail);

        binding.backBtn.setOnClickListener(view -> finish());

        int id = getIntent().getIntExtra("id", 0);
    }

    public void onSuccessGetFurnitureDetail(FurnitureModel.FurnitureDataItem item) {

    }
    public void onFailGetFurnitureDetail(String error) {

    }
}
