package com.toplevel.kengmakon.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentHomeBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CategoriesModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.SetModel;
import com.toplevel.kengmakon.ui.CategoryDetailActivity;
import com.toplevel.kengmakon.ui.FurnitureDetailActivity;
import com.toplevel.kengmakon.ui.LoginActivity;
import com.toplevel.kengmakon.ui.NotificationsActivity;
import com.toplevel.kengmakon.ui.SetDetailActivity;
import com.toplevel.kengmakon.ui.adapters.CategoriesAdapter;
import com.toplevel.kengmakon.ui.adapters.FurnitureAdapter;
import com.toplevel.kengmakon.ui.adapters.SetAdapter;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import java.util.ArrayList;
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

        binding.notifications.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), NotificationsActivity.class)));

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
                Intent intent = new Intent(getActivity(), FurnitureDetailActivity.class);
                intent.putExtra("id", model.getId());
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


        furnitureVM.getSet(page, size);
        furnitureVM.getCategories(1, 20);
        furnitureVM.getFurniture(preferencesUtil.getTOKEN(), 1, 20);

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

        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollX == v.getChildAt(0).getMeasuredWidth() - v.getMeasuredWidth()) {
                    page++;
                    furnitureVM.getSet(page, size);
                }
            }
        });

        return view;
    }

    public void onSuccessGetSet(SetModel model) {
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {

            for (int i = 0; i < model.getData().getItems().size(); i++) {
                setDataItems.add(model.getData().getItems().get(i));
            }
            adapter.setItems(setDataItems);
            //adapter.setItems(model.getData().getItems());
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
}
