package com.furniture.kengmakon.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.furniture.kengmakon.ui.adapters.CashbackAdapter;
import com.furniture.kengmakon.ui.viewmodels.UserVM;
import com.furniture.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentCashbackBinding;
import com.furniture.kengmakon.models.CashbackModel;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

public class CashbackDetailActivity extends AppCompatActivity {

    private FragmentCashbackBinding binding;

    private CashbackAdapter adapter;
    private UserVM userVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_cashback);
        userVM = ViewModelProviders.of(this, viewModelFactory).get(UserVM.class);
        userVM.onSuccessCashbackLiveData().observe(this, this::onSuccessCashback);
        userVM.onFailCashbackLiveData().observe(this, this::onFailCashback);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.swipeRefreshLayout.setRefreshing(true);
        userVM.getCashback(preferencesUtil.getTOKEN(), preferencesUtil.getUserId());

        if (!TextUtils.isEmpty(preferencesUtil.getName())) {
            binding.userName.setText(preferencesUtil.getName().toUpperCase(Locale.ROOT));
        }

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userVM.getCashback(preferencesUtil.getTOKEN(), preferencesUtil.getUserId());
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CashbackAdapter(this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(preferencesUtil.getName())) {
            binding.userName.setText(preferencesUtil.getName().toUpperCase(Locale.ROOT));
        }
    }

    public void onSuccessCashback(CashbackModel model) {

        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getData().getItems().size() > 0) {
            binding.cashbackHistory.setVisibility(View.VISIBLE);
            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.totalCashback.setText(df.format(model.getData().getData().getCashback().getTotal()) + "sum");
            // binding.totalCashback.setText(String.valueOf(model.getData().getData().getCashback().getTotal()));
            adapter.setItems(model.getData().getData().getItems());
        }
    }

    public void onFailCashback(String model) {
        binding.swipeRefreshLayout.setRefreshing(false);
    }
}
