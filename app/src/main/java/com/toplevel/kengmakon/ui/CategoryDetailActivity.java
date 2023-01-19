package com.toplevel.kengmakon.ui;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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
import com.toplevel.kengmakon.utils.NetworkChangeListener;
import com.toplevel.kengmakon.utils.PreferencesUtil;

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
    int id;
    private int page = 1;
    private int size = 20;
    private boolean isItemSelected = true;
    String name = "";
    int totalItems = 0;
    int totalSets = 0;

    List<CategoryDetailModel.CategoryDetailDataItem> items = new ArrayList<>();
    List<CategoryDetailModel.CategoryDetailDataItem> sets = new ArrayList<>();
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

        binding.setLayout.setOnClickListener(view1 -> {

            isItemSelected = false;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT);
            binding.setTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#385B96"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#ffffff"));

            binding.totalItem.setText(String.valueOf(totalSets) + " " + getString(R.string.products));

            if (sets.size() == 0) {
                binding.furnitureRecycler.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
                // binding.swipeRefreshLayout.setRefreshing(true);
                //actionsVM.getActionsEvent(preferencesUtil.getLANGUAGE(), page, size, "EVENT");
            } else {
                binding.furnitureRecycler.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
                adapter.setItems(sets);
            }
        });

        binding.itemLayout.setOnClickListener(view1 -> {
            isItemSelected = true;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setTxtView.setTypeface(Typeface.DEFAULT);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#385B96"));

            binding.totalItem.setText(String.valueOf(totalItems) + " " + getString(R.string.products));

            if (items.size() == 0) {
                binding.furnitureRecycler.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            } else {
                binding.furnitureRecycler.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
                adapter.setItems(items);
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
        id = getIntent().getIntExtra("id", 1);
        furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), page, size, id);

        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), page, size, id);
                }
            }
        });
    }

    public void onSuccessGetCategoryDetail(CategoryDetailModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {


            if (model.getData().getTotalPages() > 1) {
                totalItems = size * model.getData().getTotalPages();
                binding.totalItem.setText(String.valueOf(totalItems) + " " + getString(R.string.products));
            } else {
                binding.totalItem.setText(String.valueOf(model.getData().getItems().size()) + " " + getString(R.string.products));
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
