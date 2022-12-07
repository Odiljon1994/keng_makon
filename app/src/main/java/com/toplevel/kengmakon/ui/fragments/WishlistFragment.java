package com.toplevel.kengmakon.ui.fragments;

import android.os.Bundle;
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

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentWishlistBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class WishlistFragment extends Fragment {

    FragmentWishlistBinding binding;
    FurnitureVM furnitureVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private FurnitureAdapter furnitureAdapter;
    private FurnitureDetailsVM furnitureDetailsVM;
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

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        furnitureAdapter = new FurnitureAdapter(getContext(), new FurnitureAdapter.ClickListener() {
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
        binding.recyclerView.setAdapter(furnitureAdapter);

        furnitureVM.getWishlist(preferencesUtil.getTOKEN());
        return view;
    }

    public void onSuccessGetWishlist(FurnitureModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.INVISIBLE);
            furnitureAdapter.setItems(model.getData().getItems());
        } else {
            binding.recyclerView.setVisibility(View.INVISIBLE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        }
    }
    public void onFailGetWishlistModel(String error) {
        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }

    public void onSuccessLike(LikeModel likeModel) {

    }
    public void onFailLike(String error) {

    }
}
