package com.furniture.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.furniture.kengmakon.ui.adapters.BranchesImageAdapter;
import com.furniture.kengmakon.models.BranchesModel;
import com.furniture.kengmakon.ui.adapters.BranchesCitiesAdapter;
import com.furniture.kengmakon.ui.dialogs.LoadingDialog;
import com.furniture.kengmakon.ui.viewmodels.InfoVM;
import com.furniture.kengmakon.utils.NetworkChangeListener;
import com.furniture.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityBranchesBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BranchesActivity extends AppCompatActivity {

    private ActivityBranchesBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private ProgressDialog progressDialog;
    InfoVM infoVM;
    private List<String> cities;
    private List<BranchesModel.BranchesData> imageItems;

    private BranchesCitiesAdapter adapter;
    private BranchesImageAdapter imageAdapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branches);
        infoVM = ViewModelProviders.of(this, viewModelFactory).get(InfoVM.class);
        infoVM.branchesModelLiveData().observe(this, this::onSuccessGetBranches);
        infoVM.onFailGetBranchesLiveData().observe(this, this::onFailGetBranches);

        cities = new ArrayList<>();
        imageItems = new ArrayList<>();
        binding.backBtn.setOnClickListener(view -> finish());

        imageAdapter = new BranchesImageAdapter(this, preferencesUtil.getLANGUAGE(), binding.viewPager, item -> {
            Intent intent = new Intent(BranchesActivity.this, BranchDetailActivity.class);
            intent.putStringArrayListExtra("images", (ArrayList<String>) item.getStore().getImages());
            intent.putExtra("address", item.getStore().getAddress());
            intent.putExtra("landmark", item.getStore().getLandmark());
            intent.putExtra("working_hours", item.getStore().getOpen_hours());
            intent.putExtra("phone_number", item.getStore().getPhone());
            intent.putExtra("is_parking_exist", item.getStore().getHas_parking());
            intent.putExtra("langtitude_longtitude", item.getStore().getLat_long());
            startActivity(intent);
        });
        binding.viewPager.setAdapter(imageAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setClipChildren(false);
        binding.viewPager.setClipToPadding(false);
        binding.viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        binding.recyclerViewCities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new BranchesCitiesAdapter(this, preferencesUtil.getLANGUAGE(), (item, position) -> {
            if (position == 0) {
                adapter.setItems(cities, position);
                imageAdapter.setItems(imageItems);
            } else {
                adapter.setItems(cities, position);
                List<BranchesModel.BranchesData> list = new ArrayList<>();
                for (int i = 0; i< imageItems.size(); i++) {
                    if (imageItems.get(i).getRegion().getName().equals(item)) {
                        list.add(imageItems.get(i));
                    }
                }
                imageAdapter.setItems(list);
            }


        });
        binding.recyclerViewCities.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.14f);
        });
        binding.viewPager.setPageTransformer(transformer);

      //  progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        showLoadingDialog();
        infoVM.getBranches(preferencesUtil.getLANGUAGE());
        binding.indicator.setViewPager(binding.viewPager);
        imageAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());

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

    public void onSuccessGetBranches(BranchesModel model) {
      //  progressDialog.dismiss();
        dialog.dismiss();
        cities = new ArrayList<>();
        imageItems = new ArrayList<>();

        if (model.getCode() == 200) {

            String city = model.getData().get(0).getRegion().getName();
            List<BranchesModel.BranchesData> firstItem = new ArrayList<>();
            for (int i = 0; i< model.getData().size(); i++) {
                if (city.equals(model.getData().get(i).getRegion().getName())) {
                    firstItem.add(model.getData().get(i));
                }
            }
            imageAdapter.setItems(model.getData());
           // imageAdapter.setItems(firstItem);
            if (model.getData().size() > 0) {

                imageItems = model.getData();
                cities.add(getString(R.string.all));
                cities.add(model.getData().get(0).getRegion().getName());

                for (int i = 1; i < model.getData().size(); i++) {

                    if (i > 0) {
                        boolean isEqual = false;
                        for (int j = 0; j < i; j++) {
                            if (model.getData().get(j).getRegion().getName().equals(model.getData().get(i).getRegion().getName())) {
                                isEqual = true;
                            }
                        }
                        if (!isEqual) {
                            cities.add(model.getData().get(i).getRegion().getName());
                        }
                    }

                }
            }



            adapter.setItems(cities, 0);
        }
    }
    public void onFailGetBranches(String error) {
        dialog.dismiss();
     //   progressDialog.dismiss();
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
