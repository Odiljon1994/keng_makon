package com.toplevel.kengmakon.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivitySetDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.SetDetailModel;
import com.toplevel.kengmakon.ui.adapters.SetAdapter;
import com.toplevel.kengmakon.ui.adapters.SetDetailAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class SetDetailActivity extends AppCompatActivity {

    ActivitySetDetailBinding binding;
    private FurnitureDetailsVM furnitureDetailsVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private SetDetailAdapter adapter;
    int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_detail);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.setModelLiveData().observe(this, this::onSuccessGetSetDetails);
        furnitureDetailsVM.onFailGetSetDetailLiveData().observe(this, this::onFailGetSetDetailModel);

        binding.backBtn.setOnClickListener(view -> finish());
        binding.furnitureRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new SetDetailAdapter(this, item -> {
            if (!TextUtils.isEmpty(item.getFurniture().getImage_url_preview())){
                Glide.with(this).load(item.getFurniture().getImage_url_preview()).centerCrop().into(binding.currentImage);
            } else {
                Toast.makeText(this, "No image url", Toast.LENGTH_SHORT).show();
            }


        });
        binding.furnitureRecycler.setAdapter(adapter);
        id = getIntent().getIntExtra("id", 1);
        furnitureDetailsVM.getSet("", id);

    }

    public void onSuccessGetSetDetails(SetDetailModel model) {
        if (model.getCode() == 200 && model.getData().size() > 0) {
            Glide.with(this).load(model.getData().get(0).getFurniture().getImage_url_preview()).centerCrop().into(binding.currentImage);
            adapter.setItems(model.getData());
        }
    }
    public void onFailGetSetDetailModel(String error) {

    }
}
