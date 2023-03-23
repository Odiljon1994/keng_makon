package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityFurnitureDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CategoryDetailModel;
import com.toplevel.kengmakon.models.FurnitureDetailModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.models.RecentlyViewedModel;
import com.toplevel.kengmakon.ui.adapters.CategoryDetailAdapter;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.adapters.FurnitureDetailImgAdapter;
import com.toplevel.kengmakon.ui.adapters.SameCategoryAdapter;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.dialogs.LoadingDialog;
import com.toplevel.kengmakon.ui.fragments.HomeFragment;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.NetworkChangeListener;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.RecentlyViewedDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private RecentlyViewedDB recentlyViewedDB;
    private FurnitureDetailImgAdapter adapter;
    private SameCategoryAdapter sameCategoryAdapter;
    private AlertDialog dialog;
    int id;
    String url;

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
        furnitureDetailsVM.successCategoryDetailLiveData().observe(this, this::onSuccessGetCategoryDetail);
        furnitureDetailsVM.onFailGetCategoryDetailLiveData().observe(this, this::onFailGetCategoryDetail);


        binding.materialsLayout.setOnClickListener(view -> {
            if (binding.materialsTextView.getVisibility() == View.VISIBLE) {
                binding.materialsTextView.setVisibility(View.GONE);
            } else {
                binding.materialsTextView.setVisibility(View.VISIBLE);
            }
        });

        binding.handmadeLayout.setOnClickListener(view -> {
            if (binding.handmadeTextView.getVisibility() == View.VISIBLE) {
                binding.handmadeTextView.setVisibility(View.GONE);
            } else {
                binding.handmadeTextView.setVisibility(View.VISIBLE);
            }
        });

        binding.sizeLayout.setOnClickListener(view -> {
            if (binding.sizeTextView.getVisibility() == View.VISIBLE) {
                binding.sizeTextView.setVisibility(View.GONE);
            } else {
                binding.sizeTextView.setVisibility(View.VISIBLE);
            }
        });

        binding.deleveryLayout.setOnClickListener(view -> {
            if (binding.deliveryTextView.getVisibility() == View.VISIBLE) {
                binding.deliveryTextView.setVisibility(View.GONE);
            } else {
                binding.deliveryTextView.setVisibility(View.VISIBLE);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new FurnitureDetailImgAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.likeImage.setOnClickListener(view -> {
            if (preferencesUtil.getIsIsSignedIn()) {
                final Bitmap bitmap = ((BitmapDrawable) binding.likeImage.getDrawable()).getBitmap();
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

        id = getIntent().getIntExtra("id", 0);
        url = getIntent().getStringExtra("url");


        binding.sameCategoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        sameCategoryAdapter = new SameCategoryAdapter(this, preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), model -> {
            Intent intent = new Intent(this, FurnitureDetailActivity.class);
            intent.putExtra("id", model.getFurniture_id());
            intent.putExtra("url", model.getImage_url_preview());
            startActivity(intent);
        });
        binding.sameCategoryRecycler.setAdapter(sameCategoryAdapter);


        recentlyViewedDB = new RecentlyViewedDB(this);

        List<RecentlyViewedModel> list = getDataFromDB();

        if (list != null && list.size() > 0) {
            boolean isExist = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFurnitureId().equals(String.valueOf(id))) {
                    isExist = true;
                    recentlyViewedDB.deleteRow(list.get(i));
                }
            }

//            if (!isExist) {
            boolean ifWrittenSuccessfully = recentlyViewedDB.addData(String.valueOf(id), url);

            // }
        } else {
            boolean ifWrittenSuccessfully = recentlyViewedDB.addData(String.valueOf(id), url);
        }


       // progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        showLoadingDialog();
        furnitureDetailsVM.getFurnitureDetail(preferencesUtil.getTOKEN(), id);
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

    public void onSuccessGetFurnitureDetail(FurnitureDetailModel item) {
        //progressDialog.dismiss();
        dialog.dismiss();
        if (item.getCode() == 200 && item.getData() != null) {

            furnitureDetailsVM.getCategoryDetail(preferencesUtil.getTOKEN(), 1, 200, item.getData().getFurniture().getCategory().getId());

            if (item.getData().getImages().size() > 0) {
                adapter.setItems(item.getData().getImages());
            }
            furnitureId = item.getData().getFurniture().getId();
//            if (!TextUtils.isEmpty(item.getData().getFurniture().getImage_url_preview())) {
//                Glide.with(this).load(item.getData().getFurniture().getImage_url_preview()).centerCrop().into(binding.furnitureImage);
//            }
            if (!TextUtils.isEmpty(item.getData().getFurniture().getName())) {

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getName(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                    binding.furnitureName.setText(pushNotificationTitleModel.getUz());
                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    binding.furnitureName.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    binding.furnitureName.setText(pushNotificationTitleModel.getRu());
                }

                // binding.furnitureName.setText(item.getData().getName());
            }
            if (!TextUtils.isEmpty(item.getData().getFurniture().getCategory().getName())) {
                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getCategory().getName(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                    binding.categoryName.setText(pushNotificationTitleModel.getUz());
                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    binding.categoryName.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    binding.categoryName.setText(pushNotificationTitleModel.getRu());
                }
                //binding.categoryName.setText(item.getData().getFurniture().getCategory().getName());
            }
            if (!TextUtils.isEmpty(item.getData().getFurniture().getInfo().getDescription())) {

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getInfo().getDescription(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.description.setText(Html.fromHtml(pushNotificationTitleModel.getUz(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.description.setText(Html.fromHtml(pushNotificationTitleModel.getUz()));
                    }

                    //binding.description.setText(pushNotificationTitleModel.getUz());
                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.description.setText(Html.fromHtml(pushNotificationTitleModel.getEn(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.description.setText(Html.fromHtml(pushNotificationTitleModel.getEn()));
                    }
                    //binding.description.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.description.setText(Html.fromHtml(pushNotificationTitleModel.getRu(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.description.setText(Html.fromHtml(pushNotificationTitleModel.getRu()));
                    }
                   // binding.description.setText(pushNotificationTitleModel.getRu());
                }

                //binding.description.setText(item.getData().getFurniture().getInfo().getDescription());
            }


            if (!TextUtils.isEmpty(item.getData().getFurniture().getInfo().getMaterials())) {

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getInfo().getMaterials(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.materialsTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.materialsTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz()));
                    }

                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.materialsTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.materialsTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn()));
                    }
                    //binding.description.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.materialsTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.materialsTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu()));
                    }
                }
            }

            if (!TextUtils.isEmpty(item.getData().getFurniture().getInfo().getHandmade())) {

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getInfo().getHandmade(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.handmadeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.handmadeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz()));
                    }

                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.handmadeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.handmadeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn()));
                    }
                    //binding.description.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.handmadeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.handmadeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu()));
                    }
                }
            }


            if (!TextUtils.isEmpty(item.getData().getFurniture().getInfo().getSize())) {

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getInfo().getSize(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.sizeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.sizeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz()));
                    }

                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.sizeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.sizeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn()));
                    }
                    //binding.description.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.sizeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.sizeTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu()));
                    }
                }
            }


            if (!TextUtils.isEmpty(item.getData().getFurniture().getInfo().getDelivery())) {

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getData().getFurniture().getInfo().getDelivery(), PushNotificationModel.class);

                if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.deliveryTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.deliveryTextView.setText(Html.fromHtml(pushNotificationTitleModel.getUz()));
                    }

                } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.deliveryTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.deliveryTextView.setText(Html.fromHtml(pushNotificationTitleModel.getEn()));
                    }
                    //binding.description.setText(pushNotificationTitleModel.getEn());
                } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.deliveryTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.deliveryTextView.setText(Html.fromHtml(pushNotificationTitleModel.getRu()));
                    }
                }
            }


            if (item.getData().getFurniture().isIs_liked()) {
                binding.likeImage.setImageDrawable(getDrawable(R.drawable.red_heart_icon));
            }
        } else {
            List<RecentlyViewedModel> list = getDataFromDB();

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getFurnitureId().equals(String.valueOf(id))) {
                        recentlyViewedDB.deleteRow(list.get(i));
                    }
                }
            }
            Toast.makeText(this, "Ushbu mebel vaqtinchalik mavjud emas!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onFailGetFurnitureDetail(String error) {

        //progressDialog.dismiss();
        dialog.dismiss();
    }

    public void onSuccessLike(LikeModel likeModel) {

    }

    public void onFailLike(String error) {

    }

    public List<RecentlyViewedModel> getDataFromDB() {
        List<RecentlyViewedModel> list = new ArrayList<>();
        Cursor cursor = recentlyViewedDB.getData();

        while (cursor.moveToNext()) {
            list.add(new RecentlyViewedModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }

        return list;
    }

    public void onSuccessGetCategoryDetail(CategoryDetailModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {

            if (model.getData().getItems().size() > 3) {

                int firstItemID = 0;
                int secondItemId = 0;
                int thirdItemId = 0;

                List<CategoryDetailModel.CategoryDetailDataItem> sortedCategoryDetailItems = new ArrayList<>();

                boolean isSame = true;

                Random random = new Random();
                firstItemID = random.nextInt(model.getData().getItems().size());

                while (isSame) {
                    secondItemId = random.nextInt(model.getData().getItems().size());
                    thirdItemId = random.nextInt(model.getData().getItems().size());

                    if (firstItemID != secondItemId && secondItemId != thirdItemId) {
                        isSame = false;
                    }
                }

                sortedCategoryDetailItems.add(model.getData().getItems().get(firstItemID));
                sortedCategoryDetailItems.add(model.getData().getItems().get(secondItemId));
                sortedCategoryDetailItems.add(model.getData().getItems().get(thirdItemId));


                sameCategoryAdapter.setItems(sortedCategoryDetailItems);
            } else {
                sameCategoryAdapter.setItems(model.getData().getItems());
            }



        } else if (model.getCode() == 200 && model.getData().getItems().size() == 0) {

        }
    }
    public void onFailGetCategoryDetail(String error) {

    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        baseDialog.setTitle(getString(R.string.not_logged_in), getString(R.string.want_to_login), "");
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
