package com.furniture.kengmakon.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
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
import com.furniture.kengmakon.models.LikeModel;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.models.SetModel;
import com.furniture.kengmakon.ui.adapters.CategoryDetailAdapter;
import com.furniture.kengmakon.ui.adapters.CategorySetDetailAdapter;
import com.furniture.kengmakon.ui.dialogs.BaseDialog;
import com.furniture.kengmakon.ui.dialogs.LoadingDialog;
import com.furniture.kengmakon.utils.NetworkChangeListener;
import com.furniture.kengmakon.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CategoryDetailActivity extends AppCompatActivity {

    ActivityCategoryDetailBinding binding;
    private FurnitureDetailsVM furnitureDetailsVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private CategoryDetailAdapter adapter;
    private CategorySetDetailAdapter adapterSet;
    int id;
    private int page = 1;
    private int size = 200;
    private boolean isItemSelected = false;
    String name = "";
    int totalItems = 0;
    int totalSets = 0;

    private AlertDialog dialog;

    List<CategoryDetailModel.CategoryDetailDataItem> items = new ArrayList<>();
    List<SetModel.SetDataItem> sets = new ArrayList<>();
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
        furnitureDetailsVM.onSuccessCategorySetDetailLiveData().observe(this, this::onSuccessGetCategorySetDetail);
        furnitureDetailsVM.onFailCategorySetDetailLiveData().observe(this, this::onFailGetCategorySetDetail);

        binding.setLayout.setOnClickListener(view1 -> {

            isItemSelected = false;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT);
            binding.setTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#385B96"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#ffffff"));

            binding.totalItem.setText(String.valueOf(totalSets) + " " + getString(R.string.products));

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

            binding.totalItem.setText(String.valueOf(totalItems) + " " + getString(R.string.products));

            binding.setDetailRecycler.setVisibility(View.GONE);

            if (items.size() > 0) {
                binding.furnitureRecycler.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
            } else {
                binding.furnitureRecycler.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            }

        });

        binding.backBtn.setOnClickListener(view -> finish());

        try {
            name = getIntent().getStringExtra("name");
            binding.categoryName.setText(name);
        } catch (Exception e) {

        }

        binding.furnitureRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CategoryDetailAdapter(this, preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), new CategoryDetailAdapter.ClickListener() {
            @Override
            public void onClick(CategoryDetailModel.CategoryDetailDataItem model) {
                Intent intent = new Intent(CategoryDetailActivity.this, FurnitureDetailActivity.class);
                intent.putExtra("id", model.getFurniture_id());
                intent.putExtra("url", model.getImage_url_preview());
                startActivity(intent);
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

        binding.setDetailRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapterSet = new CategorySetDetailAdapter(this, preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), new CategorySetDetailAdapter.ClickListener() {
            @Override
            public void onClick(SetModel.SetDataItem model) {
                Intent intent = new Intent(CategoryDetailActivity.this, SetDetailActivity.class);
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

        id = getIntent().getIntExtra("id", 1);
//        furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), page, size, id);
//        furnitureDetailsVM.getCategorySetDetail(preferencesUtil.getTOKEN(), page, size, id);
        showLoadingDialog();
        furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), 1, 200, id);
        furnitureDetailsVM.getCategorySetDetail(preferencesUtil.getTOKEN(), 1, 200, id);

//        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
////                    page++;
////                    furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), page, size, id);
////                    furnitureDetailsVM.getCategorySetDetail(preferencesUtil.getTOKEN(), page, size, id);
//                    furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), 1, 200, id);
//                    furnitureDetailsVM.getCategorySetDetail(preferencesUtil.getTOKEN(), 1, 200, id);
//                }
//            }
//        });
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

    public void onSuccessGetCategorySetDetail(SetModel model) {
        dialog.dismiss();
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            adapterSet.setItems(model.getData().getItems());
            sets = model.getData().getItems();
            totalSets = model.getData().getItems().size();
            binding.totalItem.setText(totalSets + " " + getString(R.string.products));
        }
    }
    public void onFailGetCategorySetDetail(String error) {
        dialog.dismiss();
    }

    public void onSuccessGetCategoryDetail(CategoryDetailModel model) {
        dialog.dismiss();
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {


            if (model.getData().getTotalPages() > 1) {
                totalItems = size * model.getData().getTotalPages();
                binding.totalItem.setText(String.valueOf(totalItems) + " " + getString(R.string.products));
            } else {
                totalItems = model.getData().getItems().size();
                //binding.totalItem.setText(String.valueOf(model.getData().getItems().size()) + " " + getString(R.string.products));
            }

            for (int i = 0; i < model.getData().getItems().size(); i++) {
                items.add(model.getData().getItems().get(i));
            }
            adapter.setItems(items);
        } else if (model.getCode() == 200 && model.getData().getItems().size() == 0) {
            binding.furnitureRecycler.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        }
    }
    public void onFailGetCategoryDetail(String error) {
        dialog.dismiss();
    }

    public void onSuccessLike(LikeModel likeModel) {

    }
    public void onFailLike(String error) {

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

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
