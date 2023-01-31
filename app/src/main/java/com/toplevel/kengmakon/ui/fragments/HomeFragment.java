package com.toplevel.kengmakon.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentHomeBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CategoriesModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.models.RecentlyViewedModel;
import com.toplevel.kengmakon.models.SetModel;
import com.toplevel.kengmakon.ui.CategoryDetailActivity;
import com.toplevel.kengmakon.ui.FurnitureDetailActivity;
import com.toplevel.kengmakon.ui.LoginActivity;
import com.toplevel.kengmakon.ui.NotificationsActivity;
import com.toplevel.kengmakon.ui.SetDetailActivity;
import com.toplevel.kengmakon.ui.adapters.CategoriesAdapter;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.adapters.RecentlyViewedAdapter;
import com.toplevel.kengmakon.ui.adapters.SetAdapter;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.RecentlyViewedDB;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private SetAdapter adapter;
    private CategoriesAdapter categoriesAdapter;
    private FurnitureAdapter furnitureAdapter;
    private FurnitureDetailsVM furnitureDetailsVM;
    FurnitureVM furnitureVM;
    private String TOKEN = "";
    private int page = 1;
    private int size = 15;
    private List<SetModel.SetDataItem> setDataItems = new ArrayList<>();
    public RecentlyViewedAdapter recentlyViewedAdapter;
    private RecentlyViewedDB recentlyViewedDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        furnitureVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureVM.class);
        furnitureVM.setModelLiveData().observe(getActivity(), this::onSuccessGetSet);
        furnitureVM.onFailGetSetLiveData().observe(getActivity(), this::onFailGetSet);
        furnitureVM.categoriesModelLiveData().observe(getActivity(), this::onSuccessGetCategories);
        furnitureVM.onFailGetCategoriesLiveData().observe(getActivity(), this::onFailGetCategories);
        furnitureVM.furnitureModelLiveData().observe(getActivity(), this::onSuccessGetFurniture);
        furnitureVM.onFailGetFurnitureLiveData().observe(getActivity(), this::onFailGetFurnitureModel);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.likeModelLiveData().observe(getActivity(), this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(getActivity(), this::onFailLike);

        recentlyViewedDB = new RecentlyViewedDB(getActivity());
        binding.notifications.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), NotificationsActivity.class)));

        TOKEN = preferencesUtil.getTOKEN();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SetAdapter(getContext(), preferencesUtil.getLANGUAGE(), item -> {
            Intent intent = new Intent(getActivity(), SetDetailActivity.class);
            intent.putExtra("id", item.getId());

            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getName(),  PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                intent.putExtra("name", pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                intent.putExtra("name", pushNotificationTitleModel.getEn());
            } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())){
                intent.putExtra("name", pushNotificationTitleModel.getRu());
            }

            //intent.putExtra("name", item.getName());
            intent.putExtra("count", item.getItem_count());
            intent.putExtra("description", item.getDescription());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerView);

        binding.categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesAdapter = new CategoriesAdapter(getActivity(), preferencesUtil.getLANGUAGE(), item -> {
            Intent intent = new Intent(getActivity(), CategoryDetailActivity.class);
            intent.putExtra("id", item.getId());
            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getName(),  PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                intent.putExtra("name", pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                intent.putExtra("name", pushNotificationTitleModel.getEn());
            } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())){
                intent.putExtra("name", pushNotificationTitleModel.getRu());
            }

            startActivity(intent);
        });
        binding.categoryRecycler.setAdapter(categoriesAdapter);

        binding.furnitureRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        furnitureAdapter = new FurnitureAdapter(getContext(), preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), new FurnitureAdapter.ClickListener() {
            @Override
            public void onClick(FurnitureModel.FurnitureDataItem model) {
                Intent intent = new Intent(getActivity(), FurnitureDetailActivity.class);
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


        binding.swipeRefreshLayout.setRefreshing(true);
        furnitureVM.getTopSet();
        //furnitureVM.getSet(page, size);
        furnitureVM.getCategories(1, 20);
        furnitureVM.getFurnitureTopList(preferencesUtil.getTOKEN());
        //furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 20);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                furnitureVM.getSet(page, size);
                furnitureVM.getCategories(1, 20);
                furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 20);
            }
        });

//        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                View view = (View)binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);
//
//                int diff = (view.getBottom() - (binding.scrollView.getHeight() + binding.scrollView
//                        .getScrollY()));
//
//                if (diff == 0) {
//                    page++;
//                    furnitureVM.getSet(page, size);
//                }
//
//            }
//        });

//        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollX == v.getChildAt(0).getMeasuredWidth() - v.getMeasuredWidth()) {
//                    page++;
//                    furnitureVM.getSet(page, size);
//                }
//            }
//        });

        return view;
    }

    public void onSuccessGetSet(SetModel model) {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {

            for (int i = 0; i < model.getData().getItems().size(); i++) {
                setDataItems.add(model.getData().getItems().get(i));
            }
            adapter.setItems(setDataItems);
            //adapter.setItems(model.getData().getItems());
        }
    }

    public void onFailGetSet(String error) {
        binding.swipeRefreshLayout.setRefreshing(false);
        System.out.println(error);
    }

    public void onSuccessGetCategories(CategoriesModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            categoriesAdapter.setItems(model.getData().getItems());
        }
    }

    public void onFailGetCategories(String error) {

    }

    public void onSuccessGetFurniture(FurnitureModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            furnitureAdapter.setItems(model.getData().getItems());
        }
    }

    public void onFailGetFurnitureModel(String error) {

    }

    public void onSuccessLike(LikeModel likeModel) {

    }

    public void onFailLike(String error) {

    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(getActivity());
        baseDialog.setTitle("Siz ro'yxatdan o'tmadingiz", "Ro'yxatdan o'tishni hohlaysizmi?");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClickOk() {
                BaseDialog.ClickListener.super.onClickOk();
                dialog.dismiss();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }

            @Override
            public void onClickNo() {
                BaseDialog.ClickListener.super.onClickNo();
                dialog.dismiss();
            }
        };
        baseDialog.setClickListener(clickListener);
    }

    public List<RecentlyViewedModel> getDataFromDB() {
        List<RecentlyViewedModel> list = new ArrayList<>();
        Cursor cursor = recentlyViewedDB.getData();

        while (cursor.moveToNext()) {
            list.add(new RecentlyViewedModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }

        Collections.reverse(list);

        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.recentlyViewedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        List<RecentlyViewedModel> list;
        list = getDataFromDB();
        recentlyViewedAdapter = new RecentlyViewedAdapter(getActivity(), list, item -> {
            Intent intent = new Intent(getActivity(), FurnitureDetailActivity.class);
            intent.putExtra("id", Integer.parseInt(item.getFurnitureId()));
            intent.putExtra("url", item.getUrl());
            startActivity(intent);
        });
        binding.recentlyViewedRecyclerView.setAdapter(recentlyViewedAdapter);
        if (list.size() > 0) {
            binding.recentlyViewedRecyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.recentlyViewedRecyclerView.setVisibility(View.GONE);
        }
    }


}
