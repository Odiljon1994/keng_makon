package com.furniture.kengmakon.ui.fragments;

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
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.furniture.kengmakon.ui.LoginActivity;
import com.google.gson.Gson;
import com.furniture.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentHomeBinding;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.furniture.kengmakon.models.CategoriesModel;
import com.furniture.kengmakon.models.FurnitureModel;
import com.furniture.kengmakon.models.LikeModel;
import com.furniture.kengmakon.models.NotificationsModel;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.models.RecentlyViewedModel;
import com.furniture.kengmakon.models.SearchModel;
import com.furniture.kengmakon.models.SetModel;
import com.furniture.kengmakon.ui.CategoryDetailActivity;
import com.furniture.kengmakon.ui.FurnitureDetailActivity;
import com.furniture.kengmakon.ui.NotificationsActivity;
import com.furniture.kengmakon.ui.SearchActivity;
import com.furniture.kengmakon.ui.SetDetailActivity;
import com.furniture.kengmakon.ui.adapters.CategoriesAdapter;
import com.furniture.kengmakon.ui.adapters.FurnitureAdapter;
import com.furniture.kengmakon.ui.adapters.RecentlyViewedAdapter;
import com.furniture.kengmakon.ui.adapters.SearchAdapter;
import com.furniture.kengmakon.ui.adapters.SetAdapter;
import com.furniture.kengmakon.ui.dialogs.BaseDialog;
import com.furniture.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.furniture.kengmakon.ui.viewmodels.FurnitureVM;
import com.furniture.kengmakon.ui.viewmodels.NotificationsVM;
import com.furniture.kengmakon.utils.PreferencesUtil;
import com.furniture.kengmakon.utils.RecentlyViewedDB;

import java.util.ArrayList;
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
    private List<SearchModel> furnitureSortedList = new ArrayList<>();
    private List<SearchModel> furnitureAllList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private NotificationsVM notificationsVM;

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
        furnitureVM.furnitureSearchModelLiveData().observe(getActivity(), this::onSuccessGetFurnitureSearch);
        furnitureVM.onFailGetFurnitureSearchLiveData().observe(getActivity(), this::onFailGetFurnitureSearchModel);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.likeModelLiveData().observe(getActivity(), this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(getActivity(), this::onFailLike);

        notificationsVM = ViewModelProviders.of(this, viewModelFactory).get(NotificationsVM.class);
        notificationsVM.notificationsModelLiveData().observe(getActivity(), this::onSuccessGetNotifications);
        notificationsVM.onFailNotificationsModelLiveData().observe(getActivity(), this::onFailGetNotifications);

        notificationsVM.getNotifications(1, 100, "uz");

        recentlyViewedDB = new RecentlyViewedDB(getActivity());
        binding.notifications.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), NotificationsActivity.class)));

        TOKEN = preferencesUtil.getTOKEN();

        binding.search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if(!TextUtils.isEmpty(binding.search.getText().toString())) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("search", binding.search.getText().toString());
                    startActivity(intent);
                }

                return true;
            }
            return false;
        });

//        binding.search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                System.out.println(editable);
//                furnitureSortedList = new ArrayList<>();
//                if (TextUtils.isEmpty(binding.search.getText().toString()) == true) {
//                    furnitureSortedList = new ArrayList<>();
//                    searchAdapter.setItems(furnitureSortedList);
//                    binding.searchRecyclerView.setVisibility(View.GONE);
//                } else {
//                    for (int i = 0; i < furnitureAllList.size(); i++) {
//                        if (furnitureAllList.get(i).getName().trim().toUpperCase().contains(editable.toString().trim().toUpperCase())) {
//                            furnitureSortedList.add(furnitureAllList.get(i));
//                        }
//                    }
//                    if (furnitureSortedList.size() > 0) {
//                        searchAdapter.setItems(furnitureSortedList);
//                        binding.searchRecyclerView.setVisibility(View.VISIBLE);
//                    } else if (TextUtils.isEmpty(editable.toString())) {
//                        furnitureSortedList.clear();
//                        furnitureSortedList = new ArrayList<>();
//                        searchAdapter.setItems(furnitureSortedList);
//                        binding.searchRecyclerView.setVisibility(View.GONE);
//                    } else {
//                        furnitureSortedList.clear();
//                        searchAdapter.setItems(furnitureSortedList);
//                        binding.searchRecyclerView.setVisibility(View.GONE);
//                    }
//                }
//
//            }
//        });


        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchAdapter = new SearchAdapter(getActivity(), item -> {
            Intent intent = new Intent(getActivity(), FurnitureDetailActivity.class);
            intent.putExtra("id", item.getFurniture_id());
            intent.putExtra("url", item.getImage_url());
            startActivity(intent);
        });
        binding.searchRecyclerView.setAdapter(searchAdapter);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SetAdapter(getContext(), preferencesUtil.getLANGUAGE(), item -> {
            Intent intent = new Intent(getActivity(), SetDetailActivity.class);
            intent.putExtra("id", item.getId());

            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getName(), PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                intent.putExtra("name", pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                intent.putExtra("name", pushNotificationTitleModel.getEn());
            } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
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
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(item.getName(), PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz") && !TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                intent.putExtra("name", pushNotificationTitleModel.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("en") && !TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                intent.putExtra("name", pushNotificationTitleModel.getEn());
            } else if (preferencesUtil.getLANGUAGE().equals("ru") && !TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
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
                intent.putExtra("url", model.getImage_url());
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
        furnitureVM.getCategories(1, 2000);
        furnitureVM.getFurnitureTopList(preferencesUtil.getTOKEN());
        furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 2000);
        //furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 20);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                furnitureVM.getTopSet();
                furnitureVM.getCategories(1, 20);
                furnitureVM.getFurnitureTopList(preferencesUtil.getTOKEN());
            }
        });

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
        } else {
            System.out.println("Furniture fail: " + model.getMessage());
        }
    }

    public void onFailGetFurnitureModel(String error) {

        System.out.println("Furniture fail: " + error);
    }


    public void onSuccessGetFurnitureSearch(FurnitureModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            for (int i = 0; i < model.getData().getItems().size(); i++) {

                try {

                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getData().getItems().get(i).getName(), PushNotificationModel.class);

                    if (!TextUtils.isEmpty(pushNotificationTitleModel.getUz())) {
                        furnitureAllList.add(new SearchModel(model.getData().getItems().get(i).getId(), pushNotificationTitleModel.getUz(),
                                model.getData().getItems().get(i).getImage_url()));
                    }
                    if (!TextUtils.isEmpty(pushNotificationTitleModel.getRu())) {
                        furnitureAllList.add(new SearchModel(model.getData().getItems().get(i).getId(), pushNotificationTitleModel.getRu(),
                                model.getData().getItems().get(i).getImage_url()));
                    }
                    if (!TextUtils.isEmpty(pushNotificationTitleModel.getEn())) {
                        furnitureAllList.add(new SearchModel(model.getData().getItems().get(i).getId(), pushNotificationTitleModel.getEn(),
                                model.getData().getItems().get(i).getImage_url()));
                    }

                } catch (Exception e) {
                    continue;
                }


            }
        } else {
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

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(getActivity());
        baseDialog.setTitle(getString(R.string.not_logged_in), getString(R.string.want_to_login), "");
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
            binding.recentlyViewedText.setVisibility(View.VISIBLE);
        } else {
            binding.recentlyViewedRecyclerView.setVisibility(View.GONE);
            binding.recentlyViewedText.setVisibility(View.GONE);
        }
    }

    public String parseLang(String data) {
        try {
            String parsedData = "";

            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(data, PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz")) {
                parsedData = pushNotificationTitleModel.getUz();
            } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                parsedData = pushNotificationTitleModel.getRu();
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                parsedData = pushNotificationTitleModel.getEn();
            }

            return parsedData;
        } catch (Exception e) {
            return "";
        }

    }

    public void onSuccessGetNotifications(NotificationsModel model) {

        if (model.getCode() == 200) {

            if (model.getData().getItems().size() > 0) {
                boolean isNotSeenExist = false;
                for (int i = 0; i < model.getData().getItems().size(); i++) {
                    if (model.getData().getItems().get(i).getIs_seen() == 0) {
                        isNotSeenExist = true;
                    }
                }
                if (isNotSeenExist == true) {
                    binding.redDot.setVisibility(View.VISIBLE);
                } else {
                    binding.redDot.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void onFailGetNotifications(String error) {

    }
}
