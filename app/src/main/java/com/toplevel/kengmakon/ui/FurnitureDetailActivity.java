package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityFurnitureDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.FurnitureDetailModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class FurnitureDetailActivity extends AppCompatActivity {

    private ActivityFurnitureDetailBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private FurnitureDetailsVM furnitureDetailsVM;
    private ProgressDialog progressDialog;
    private int furnitureId = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_furniture_detail);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.onSuccessFurnitureDetailLiveData().observe(this, this::onSuccessGetFurnitureDetail);
        furnitureDetailsVM.onFailFurnitureDetailLiveData().observe(this, this::onFailGetFurnitureDetail);
        furnitureDetailsVM.likeModelLiveData().observe(this, this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(this, this::onFailLike);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.likeImage.setOnClickListener(view -> {
            if (preferencesUtil.getIsIsSignedIn()) {
                final Bitmap bitmap = ((BitmapDrawable)binding.likeImage.getDrawable()).getBitmap();
                final Drawable likeDrawable = this.getResources().getDrawable(R.drawable.red_heart_icon);
                final Bitmap likeBitmap = ((BitmapDrawable) likeDrawable).getBitmap();

                if (bitmap.sameAs(likeBitmap)) {
                    binding.likeImage.setImageDrawable(this.getDrawable(R.drawable.gray_heart_icon));

                } else {
                    binding.likeImage.setImageDrawable(this.getDrawable(R.drawable.red_heart_icon));

                }
                furnitureDetailsVM.setLikeDislike(preferencesUtil.getTOKEN(), furnitureId);
            } else {
                showDialog();
            }
        });

        int id = getIntent().getIntExtra("id", 0);
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        furnitureDetailsVM.getFurnitureDetail(preferencesUtil.getTOKEN(), id);
    }

    public void onSuccessGetFurnitureDetail(FurnitureDetailModel item) {
        progressDialog.dismiss();
        if (item.getCode() == 200) {
            furnitureId = item.getData().getId();
            if (!TextUtils.isEmpty(item.getData().getImage_url_preview())) {
                Glide.with(this).load(item.getData().getImage_url_preview()).centerCrop().into(binding.furnitureImage);
            }
            if (!TextUtils.isEmpty(item.getData().getName())) {
                binding.furnitureName.setText(item.getData().getName());
            }
            if (!TextUtils.isEmpty(item.getData().getCategory().getName())) {
                binding.furnitureName.setText(item.getData().getCategory().getName());
            }
            if (!TextUtils.isEmpty(item.getData().getDescription())) {
                binding.description.setText(item.getData().getDescription());
            }
            if (item.getData().isIs_liked()) {
                binding.likeImage.setImageDrawable(getDrawable(R.drawable.red_heart_icon));
            }
        }
    }
    public void onFailGetFurnitureDetail(String error) {

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
                startActivity(new Intent(FurnitureDetailActivity.this, LoginActivity.class));

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
