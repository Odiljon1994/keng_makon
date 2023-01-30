package com.toplevel.kengmakon.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentWishlistBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.ui.FurnitureDetailActivity;
import com.toplevel.kengmakon.ui.SetDetailActivity;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.adapters.WishlistAdapter;
import com.toplevel.kengmakon.ui.adapters.WishlistCategoriesAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WishlistFragment extends Fragment {

    FragmentWishlistBinding binding;
    FurnitureVM furnitureVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private WishlistAdapter wishlistAdapter;
    private FurnitureDetailsVM furnitureDetailsVM;
    private WishlistCategoriesAdapter wishlistCategoriesAdapter;
    private List<FurnitureModel.FurnitureDataItem> allItems;
    private List<String> wishlistCategories;
    private boolean isItemSelected = true;
    private List<FurnitureModel.FurnitureDataItem> items;
    private List<FurnitureModel.FurnitureDataItem> sets;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false);
        View view = binding.getRoot();
        furnitureVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureVM.class);
        furnitureVM.onSuccessGetWishlistLiveData().observe(getActivity(), this::onSuccessGetWishlist);
        furnitureVM.onFailGetWishlistLiveData().observe(getActivity(), this::onFailGetWishlistModel);
        furnitureDetailsVM = ViewModelProviders.of(this, viewModelFactory).get(FurnitureDetailsVM.class);
        furnitureDetailsVM.likeModelLiveData().observe(getActivity(), this::onSuccessLike);
        furnitureDetailsVM.onFailSetLikeLiveData().observe(getActivity(), this::onFailLike);

        items = new ArrayList<>();
        sets = new ArrayList<>();

        binding.setLayout.setOnClickListener(view1 -> {

            isItemSelected = false;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT);
            binding.setTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#385B96"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#ffffff"));

            if (sets.size() == 0) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
               // binding.swipeRefreshLayout.setRefreshing(true);
                //actionsVM.getActionsEvent(preferencesUtil.getLANGUAGE(), page, size, "EVENT");
            } else {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
                wishlistAdapter.setItems(sets);
            }
        });

        binding.itemLayout.setOnClickListener(view1 -> {
            isItemSelected = true;
            binding.itemTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.setTxtView.setTypeface(Typeface.DEFAULT);
            binding.setBottomView.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.itemBottomView.setBackgroundColor(Color.parseColor("#385B96"));

            if (items.size() == 0) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
                wishlistAdapter.setItems(items);
            }
        });


        binding.categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        wishlistCategoriesAdapter = new WishlistCategoriesAdapter(getContext(), (item, position)-> {
            if (position == 0) {
                wishlistAdapter.setItems(allItems);
            } else {
                List<FurnitureModel.FurnitureDataItem> sortedItems = new ArrayList<>();
                for (int i = 0; i < allItems.size(); i++) {

                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(allItems.get(i).getCategory().getName(),  PushNotificationModel.class);
                    String currentItemCategoryName = "";

                    if (preferencesUtil.getLANGUAGE().equals("uz")) {
                        currentItemCategoryName = pushNotificationTitleModel.getUz();
                    } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                        currentItemCategoryName = pushNotificationTitleModel.getRu();
                    } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                        currentItemCategoryName = pushNotificationTitleModel.getEn();
                    }


                    if (!TextUtils.isEmpty(currentItemCategoryName) && !TextUtils.isEmpty(item)) {
                        if (currentItemCategoryName.equals(item)) {
                            sortedItems.add(allItems.get(i));
                        }
                    }

                }
                wishlistAdapter.setItems(sortedItems);
            }
            wishlistCategoriesAdapter.setItems(wishlistCategories, position);
        });
        binding.categoriesRecyclerView.setAdapter(wishlistCategoriesAdapter);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        wishlistAdapter = new WishlistAdapter(getContext(), preferencesUtil.getLANGUAGE(), preferencesUtil.getIsIsSignedIn(), new WishlistAdapter.ClickListener() {
            @Override
            public void onClick(FurnitureModel.FurnitureDataItem model) {
                Intent intent = new Intent(getContext(), FurnitureDetailActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("url", model.getImage_url_preview());
                startActivity(intent);
            }

            @Override
            public void onClickLikeBtn(FurnitureModel.FurnitureDataItem model, boolean isLiked) {
                if (preferencesUtil.getIsIsSignedIn()) {
                    furnitureDetailsVM.setLikeDislike(preferencesUtil.getTOKEN(), model.getId());
                } else {
                    Toast.makeText(getContext(), "Not registered yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.recyclerView.setAdapter(wishlistAdapter);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                furnitureVM.getWishlist(preferencesUtil.getTOKEN());
            }
        });

        binding.swipeRefreshLayout.setRefreshing(true);
        furnitureVM.getWishlist(preferencesUtil.getTOKEN());
        return view;
    }

    public void onSuccessGetWishlist(FurnitureModel model) {
        items = new ArrayList<>();
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {

            allItems = new ArrayList<>();
            allItems.addAll(model.getData().getItems());
            wishlistCategories = new ArrayList<>();
            wishlistCategories.add(getString(R.string.all));


          //  wishlistCategories.add(model.getData().getItems().get(0).getCategory().getName());

            Gson parser1 = new Gson();
            PushNotificationModel pushNotificationTitleModel1 = parser1.fromJson(model.getData().getItems().get(0).getCategory().getName(),  PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz")) {

                wishlistCategories.add(pushNotificationTitleModel1.getUz());
            } else if (preferencesUtil.getLANGUAGE().equals("ru")) {

                wishlistCategories.add(pushNotificationTitleModel1.getRu());
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {

                wishlistCategories.add(pushNotificationTitleModel1.getEn());
            }

            for (int i = 0; i < model.getData().getItems().size(); i++) {

                if (i > 0) {
                    boolean isEqual = false;
                    for (int j = 0; j < i; j++) {

                        if (model.getData().getItems().get(j).getCategory().getName().equals(model.getData().getItems().get(i).getCategory().getName())) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual) {
                        Gson parser = new Gson();
                        PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getData().getItems().get(i).getCategory().getName(),  PushNotificationModel.class);

                        if (preferencesUtil.getLANGUAGE().equals("uz")) {
                            wishlistCategories.add(pushNotificationTitleModel.getUz());
                        } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                            wishlistCategories.add(pushNotificationTitleModel.getRu());
                        } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                            wishlistCategories.add(pushNotificationTitleModel.getEn());
                        }
                    }
                }

            }








//            for (int i = 0; i < model.getData().getItems().size(); i++) {
//                if (!TextUtils.isEmpty(model.getData().getItems().get(i).getCategory().getName())) {
//                    wishlistCategories.add(model.getData().getItems().get(i).getCategory().getName());
//                }
//            }
            wishlistCategoriesAdapter.setItems(wishlistCategories, 0);


            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.INVISIBLE);

            items = model.getData().getItems();
            wishlistAdapter.setItems(items);
        } else {
            binding.recyclerView.setVisibility(View.INVISIBLE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        }
    }
    public void onFailGetWishlistModel(String error) {
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }

    public void onSuccessLike(LikeModel likeModel) {

    }
    public void onFailLike(String error) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            binding.swipeRefreshLayout.setRefreshing(true);
            furnitureVM.getWishlist(preferencesUtil.getTOKEN());
        }
    }
}
