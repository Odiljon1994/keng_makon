package com.toplevel.kengmakon.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.toplevel.kengmakon.models.SetModel;
import com.toplevel.kengmakon.ui.adapters.CategoriesAdapter;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.adapters.SetAdapter;
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

    FurnitureVM furnitureVM;

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

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SetAdapter(getContext(), item -> {

        });
        binding.recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerView);

        binding.categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesAdapter = new CategoriesAdapter(getActivity(), item -> {

        });
        binding.categoryRecycler.setAdapter(categoriesAdapter);

        binding.furnitureRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        furnitureAdapter = new FurnitureAdapter(getContext(), new FurnitureAdapter.ClickListener() {
            @Override
            public void onClick(FurnitureModel.FurnitureDataItem model) {

            }

            @Override
            public void onClickLikeBtn(FurnitureModel.FurnitureDataItem model) {

            }
        });
        binding.furnitureRecycler.setAdapter(furnitureAdapter);

        furnitureVM.getSet(1, 15);
        furnitureVM.getCategories(1, 10);
        furnitureVM.getFurniture("", 1, 20);


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
}
