package com.toplevel.kengmakon.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityCategoryDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CategoryDetailModel;
import com.toplevel.kengmakon.ui.adapters.CategoryDetailAdapter;
import com.toplevel.kengmakon.ui.adapters.SetDetailAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class CategoryDetailActivity extends AppCompatActivity {

    ActivityCategoryDetailBinding binding;
    private FurnitureDetailsVM furnitureDetailsVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private CategoryDetailAdapter adapter;
    int id;
    String name = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_detail);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.successCategoryDetailLiveData().observe(this, this::onSuccessGetCategoryDetail);
        furnitureDetailsVM.onFailGetCategoryDetailLiveData().observe(this, this::onFailGetCategoryDetail);

        binding.backBtn.setOnClickListener(view -> finish());

        name = getIntent().getStringExtra("name");
        binding.title.setText(name);
        binding.furnitureRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CategoryDetailAdapter(this, new CategoryDetailAdapter.ClickListener() {
            @Override
            public void onClick(CategoryDetailModel.CategoryDetailDataItem model) {

            }

            @Override
            public void onClickLikeBtn(CategoryDetailModel.CategoryDetailDataItem model) {

            }
        });
        binding.furnitureRecycler.setAdapter(adapter);
        id = getIntent().getIntExtra("id", 1);
        furnitureDetailsVM.getCategoryDetail("", 1, 20, id);
    }

    public void onSuccessGetCategoryDetail(CategoryDetailModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            adapter.setItems(model.getData().getItems());
        }
    }
    public void onFailGetCategoryDetail(String error) {

    }
}
