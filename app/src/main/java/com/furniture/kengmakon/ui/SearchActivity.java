package com.furniture.kengmakon.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.furniture.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityCategoryDetailBinding;
import com.furniture.kengmakon.models.CategoryDetailModel;
import com.furniture.kengmakon.models.FurnitureModel;
import com.furniture.kengmakon.models.LikeModel;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.models.SearchModel;
import com.furniture.kengmakon.models.SetModel;
import com.furniture.kengmakon.ui.adapters.CategoryDetailAdapter;
import com.furniture.kengmakon.ui.adapters.CategorySetDetailAdapter;
import com.furniture.kengmakon.ui.adapters.FurnitureAdapter;
import com.furniture.kengmakon.ui.dialogs.BaseDialog;
import com.furniture.kengmakon.ui.dialogs.LoadingDialog;
import com.furniture.kengmakon.ui.viewmodels.FurnitureVM;
import com.furniture.kengmakon.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity {

    private ActivityCategoryDetailBinding binding;

    private FurnitureDetailsVM furnitureDetailsVM;
    private FurnitureVM furnitureVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private CategoryDetailAdapter adapter;
    private CategorySetDetailAdapter adapterSet;
    private FurnitureAdapter furnitureAdapter;
    int id;
    private int page = 1;
    private int size = 200;
    private boolean isItemSelected = false;
    String searchKey = "";
    int totalItems = 0;
    int totalSets = 0;

    private AlertDialog dialog;

    private List<SearchModel> furnitureAllList = new ArrayList<>();

    List<FurnitureModel.FurnitureDataItem> items = new ArrayList<>();
    List<SetModel.SetDataItem> sets = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_detail);
        furnitureVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureVM.class);
        furnitureVM.furnitureSearchModelLiveData().observe(this, this::onSuccessGetFurnitureSearch);
        furnitureVM.onFailGetFurnitureSearchLiveData().observe(this, this::onFailGetFurnitureSearchModel);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.likeModelLiveData().observe(this, this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(this, this::onFailLike);
        furnitureVM.setModelLiveData().observe(this, this::onSuccessGetSet);
        furnitureVM.onFailGetSetLiveData().observe(this, this::onFailGetSet);

        searchKey = getIntent().getStringExtra("search");
        binding.backBtn.setOnClickListener(view -> finish());


        binding.title.setText(searchKey + " ...");

        binding.setLayout.setOnClickListener(view1 -> {

            isItemSelected = false;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT);
            binding.setTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#385B96"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#ffffff"));

            binding.totalItem.setText(sets.size() + " " + getString(R.string.products));

            binding.furnitureRecycler.setVisibility(View.GONE);

            if (sets.size() > 0) {
                binding.setDetailRecycler.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
            } else {
                binding.setDetailRecycler.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            }

        });

        binding.itemLayout.setOnClickListener(view1 -> {
            isItemSelected = true;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setTxtView.setTypeface(Typeface.DEFAULT);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#385B96"));

            binding.totalItem.setText(items.size() + " " + getString(R.string.products));

            binding.setDetailRecycler.setVisibility(View.GONE);

            if (items.size() > 0) {
                binding.furnitureRecycler.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
            } else {
                binding.furnitureRecycler.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            }

        });



        binding.furnitureRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        furnitureAdapter = new FurnitureAdapter(this, preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), new FurnitureAdapter.ClickListener() {
            @Override
            public void onClick(FurnitureModel.FurnitureDataItem model) {
                Intent intent = new Intent(SearchActivity.this, FurnitureDetailActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("url", model.getImage_url_preview());
                startActivity(intent);
            }

            @Override
            public void onClickLikeBtn(FurnitureModel.FurnitureDataItem model, boolean isLiked) {
                if (preferencesUtil.getIsIsSignedIn()) {
                    furnitureDetailsVM.setLikeDislike(preferencesUtil.getTOKEN(), model.getId());
                } else {
                    showDialog();
                }
            }
        });

        binding.furnitureRecycler.setAdapter(furnitureAdapter);

        binding.setDetailRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapterSet = new CategorySetDetailAdapter(this, preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), new CategorySetDetailAdapter.ClickListener() {
            @Override
            public void onClick(SetModel.SetDataItem model) {
                Intent intent = new Intent(SearchActivity.this, SetDetailActivity.class);
                intent.putExtra("id", model.getId());

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getName(),  PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                    intent.putExtra("name", pushNotificationTitleModel.getUz());
                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    intent.putExtra("name", pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())){
                    intent.putExtra("name", pushNotificationTitleModel.getRu());
                }

                //intent.putExtra("name", item.getName());
                intent.putExtra("count", model.getItem_count());
                intent.putExtra("description", model.getDescription());
                startActivity(intent);
            }

            @Override
            public void onClickLikeBtn(CategoryDetailModel.CategoryDetailDataItem model, boolean isLiked) {

            }
        });
        binding.setDetailRecycler.setAdapter(adapterSet);


        showLoadingDialog();
        furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 2000);
        furnitureVM.getSet(1, 2000);
    }

    public void onSuccessGetFurnitureSearch(FurnitureModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {

            for (int i = 0; i < model.getData().getItems().size(); i++) {

                if (model.getData().getItems().get(i).getName().toUpperCase(Locale.ROOT).contains(searchKey.toUpperCase(Locale.ROOT))) {
                    items.add(model.getData().getItems().get(i));
                }
            }

            dialog.dismiss();
            //binding.totalItem.setText(items.size() + " " + getString(R.string.products));
            if (items.size() > 0) {
                binding.furnitureRecycler.setVisibility(View.VISIBLE);
                furnitureAdapter.setItems(items);
            } else {
                binding.furnitureRecycler.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            dialog.dismiss();
            System.out.println("Furniture fail: " + model.getMessage());
        }
    }

    public void onFailGetFurnitureSearchModel(String error) {

        System.out.println("Furniture fail: " + error);
    }


    public void onSuccessLike(LikeModel likeModel) {

    }
    public void onFailLike(String error) {

    }

    public void onSuccessGetSet(SetModel model) {

        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {


            for (int i = 0; i < model.getData().getItems().size(); i++) {

                if (model.getData().getItems().get(i).getName().toUpperCase(Locale.ROOT).contains(searchKey.toUpperCase(Locale.ROOT))) {
                    sets.add(model.getData().getItems().get(i));
                }

                //setDataItems.add(model.getData().getItems().get(i));
            }
            binding.totalItem.setText(sets.size() + " " + getString(R.string.products));
            adapterSet.setItems(sets);
            //adapter.setItems(model.getData().getItems());
        }
    }

    public void onFailGetSet(String error) {

        System.out.println(error);
    }

    public void showLoadingDialog() {
        LoadingDialog loadingDialog = new LoadingDialog(this);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
        alertBuilder.setView(loadingDialog);
        dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        baseDialog.setTitle("Siz ro'yxatdan o'tmadingiz", "Ro'yxatdan o'tishni hohlaysizmi?", "");
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
                startActivity(new Intent(SearchActivity.this, LoginActivity.class));

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
