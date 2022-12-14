package com.toplevel.kengmakon.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentCashbackBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CashbackModel;
import com.toplevel.kengmakon.ui.adapters.CashbackAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class CashbackFragment extends Fragment {

    FragmentCashbackBinding binding;
    private CashbackAdapter adapter;
    private UserVM userVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashback, container, false);
        View view = binding.getRoot();
        userVM = ViewModelProviders.of(this, viewModelFactory).get(UserVM.class);
        userVM.onSuccessCashbackLiveData().observe(getActivity(), this::onSuccessCashback);
        userVM.onFailCashbackLiveData().observe(getActivity(), this::onFailCashback);

        binding.swipeRefreshLayout.setRefreshing(true);
        userVM.getCashback(preferencesUtil.getUserId());

        if (!TextUtils.isEmpty(preferencesUtil.getName())) {
            binding.userName.setText(preferencesUtil.getName());
        }

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userVM.getCashback(preferencesUtil.getUserId());
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new CashbackAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        return view;
    }

    public void onSuccessCashback(CashbackModel model) {

        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getData().getItems().size() > 0) {
            binding.cashbackHistory.setVisibility(View.VISIBLE);
            binding.totalCashback.setText(String.valueOf(model.getData().getData().getCashback().getTotal()));
            adapter.setItems(model.getData().getData().getItems());
        }
    }

    public void onFailCashback(String model) {
        binding.swipeRefreshLayout.setRefreshing(false);
    }
}
