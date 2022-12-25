package com.toplevel.kengmakon.ui.fragments;

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

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentWishlistBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
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


        binding.categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        wishlistCategoriesAdapter = new WishlistCategoriesAdapter(getContext(), (item, position)-> {
            if (position == 0) {
                wishlistAdapter.setItems(allItems);
            } else {
                List<FurnitureModel.FurnitureDataItem> sortedItems = new ArrayList<>();
                for (int i = 0; i < allItems.size(); i++) {
                    if (allItems.get(i).getCategory().getName().equals(item)) {
                        sortedItems.add(allItems.get(i));
                    }
                }
                wishlistAdapter.setItems(sortedItems);
            }
            wishlistCategoriesAdapter.setItems(wishlistCategories, position);
        });
        binding.categoriesRecyclerView.setAdapter(wishlistCategoriesAdapter);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        wishlistAdapter = new WishlistAdapter(getContext(), preferencesUtil.getIsIsSignedIn(), new WishlistAdapter.ClickListener() {
            @Override
            public void onClick(FurnitureModel.FurnitureDataItem model) {

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
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {

            allItems = new ArrayList<>();
            allItems.addAll(model.getData().getItems());
            wishlistCategories = new ArrayList<>();
            wishlistCategories.add(getString(R.string.all));


            wishlistCategories.add(model.getData().getItems().get(0).getCategory().getName());

            for (int i = 0; i < model.getData().getItems().size(); i++) {

                if (i > 0) {
                    boolean isEqual = false;
                    for (int j = 0; j < i; j++) {
                        if (model.getData().getItems().get(j).getCategory().getName().equals(model.getData().getItems().get(i).getCategory().getName())) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual) {
                        wishlistCategories.add(model.getData().getItems().get(i).getCategory().getName());
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
            wishlistAdapter.setItems(model.getData().getItems());
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
