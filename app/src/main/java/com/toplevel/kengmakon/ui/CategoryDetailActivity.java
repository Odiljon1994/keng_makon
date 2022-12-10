package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.ui.adapters.CategoryDetailAdapter;
import com.toplevel.kengmakon.ui.adapters.SetDetailAdapter;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
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
        furnitureDetailsVM.likeModelLiveData().observe(this, this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(this, this::onFailLike);

        binding.backBtn.setOnClickListener(view -> finish());

        name = getIntent().getStringExtra("name");
        binding.title.setText(name);
        binding.furnitureRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CategoryDetailAdapter(this, preferencesUtil.getIsIsSignedIn(), new CategoryDetailAdapter.ClickListener() {
            @Override
            public void onClick(CategoryDetailModel.CategoryDetailDataItem model) {

            }

            @Override
            public void onClickLikeBtn(CategoryDetailModel.CategoryDetailDataItem model, boolean isLiked) {
                if (preferencesUtil.getIsIsSignedIn()) {
                    furnitureDetailsVM.setLikeDislike(preferencesUtil.getTOKEN(), model.getFurniture_id());
                } else {
                    showDialog();
                }
            }
        });
        binding.furnitureRecycler.setAdapter(adapter);
        id = getIntent().getIntExtra("id", 1);
        furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), 1, 20, id);
    }

    public void onSuccessGetCategoryDetail(CategoryDetailModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            adapter.setItems(model.getData().getItems());
        }
    }
    public void onFailGetCategoryDetail(String error) {

    }

    public void onSuccessLike(LikeModel likeModel) {

    }
    public void onFailLike(String error) {

    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        baseDialog.setTitle("Siz ro'yxatdan o'tmadingiz", "Ro'yxatdan o'tishni hohlaysizmi?");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClickOk() {
                BaseDialog.ClickListener.super.onClickOk();
                dialog.dismiss();
                startActivity(new Intent(CategoryDetailActivity.this, LoginActivity.class));

            }

            @Override
            public void onClickNo() {
                BaseDialog.ClickListener.super.onClickNo();
                dialog.dismiss();
            }
        };
        baseDialog.setClickListener(clickListener);
    }
}
