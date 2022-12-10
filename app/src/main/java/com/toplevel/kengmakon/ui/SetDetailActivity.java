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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivitySetDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.SetDetailModel;
import com.toplevel.kengmakon.ui.adapters.SetAdapter;
import com.toplevel.kengmakon.ui.adapters.SetDetailAdapter;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
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
        furnitureDetailsVM.likeModelLiveData().observe(this, this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(this, this::onFailLike);

        try {
            binding.name.setText(getIntent().getStringExtra("name"));
            binding.productCount.setText(String.valueOf(getIntent().getIntExtra("count", 1)) + " " + getString(R.string.products));
        } catch (Exception e) {
            System.out.println(e);
        }
        binding.backBtn.setOnClickListener(view -> finish());
        binding.furnitureRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new SetDetailAdapter(this, preferencesUtil.getIsIsSignedIn(), new SetDetailAdapter.ClickListener() {
            @Override
            public void onClick(SetDetailModel.SetDetailData model) {
                if (!TextUtils.isEmpty(model.getFurniture().getImage_url_preview())){
                    Glide.with(SetDetailActivity.this).load(model.getFurniture().getImage_url_preview()).centerCrop().into(binding.currentImage);
                } else {
                    Toast.makeText(SetDetailActivity.this, "No image url", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onClickLikeBtn(SetDetailModel.SetDetailData model, boolean isLiked) {
                if (preferencesUtil.getIsIsSignedIn()) {
                    furnitureDetailsVM.setLikeDislike(preferencesUtil.getTOKEN(), model.getFurniture().getId());
                } else {
                    showDialog();
                }

            }
        });

        binding.furnitureRecycler.setAdapter(adapter);
        id = getIntent().getIntExtra("id", 1);

        furnitureDetailsVM.getSet(preferencesUtil.getTOKEN(), id);

    }

    public void onSuccessGetSetDetails(SetDetailModel model) {
        if (model.getCode() == 200 && model.getData().size() > 0) {
            try {
                binding.category.setText(model.getData().get(0).getFurniture().getName());
            } catch (Exception e) {
                System.out.println(e);
            }
            Glide.with(this).load(model.getData().get(0).getFurniture().getImage_url_preview()).centerCrop().into(binding.currentImage);
            adapter.setItems(model.getData());
        }
    }
    public void onFailGetSetDetailModel(String error) {

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
                startActivity(new Intent(SetDetailActivity.this, LoginActivity.class));

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
