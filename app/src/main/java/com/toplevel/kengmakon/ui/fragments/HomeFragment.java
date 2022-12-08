package com.toplevel.kengmakon.ui.fragments;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentHomeBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CategoriesModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.SetModel;
import com.toplevel.kengmakon.ui.CategoryDetailActivity;
import com.toplevel.kengmakon.ui.SetDetailActivity;
import com.toplevel.kengmakon.ui.adapters.CategoriesAdapter;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.adapters.SetAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

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

        TOKEN = preferencesUtil.getTOKEN();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SetAdapter(getContext(), item -> {
            Intent intent = new Intent(getActivity(), SetDetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("name", item.getName());
            intent.putExtra("count", item.getItem_count());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerView);

        binding.categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesAdapter = new CategoriesAdapter(getActivity(), item -> {
            Intent intent = new Intent(getActivity(), CategoryDetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("name", item.getName());
            startActivity(intent);
        });
        binding.categoryRecycler.setAdapter(categoriesAdapter);

        binding.furnitureRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        furnitureAdapter = new FurnitureAdapter(getContext(), preferencesUtil.getIsIsSignedIn(), new FurnitureAdapter.ClickListener() {
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
        binding.furnitureRecycler.setAdapter(furnitureAdapter);

        if (preferencesUtil.getTOKEN().equals("")) {

        }
        furnitureVM.getSet(1, 15);
        furnitureVM.getCategories(1, 10);
        furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 20);


        return view;
    }

    public void onSuccessGetSet(SetModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            adapter.setItems(model.getData().getItems());
        }
    }

    public void onFailGetSet(String error) {

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
}
