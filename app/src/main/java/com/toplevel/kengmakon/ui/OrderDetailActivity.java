package com.toplevel.kengmakon.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityOrderDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.OrderDetailModel;
import com.toplevel.kengmakon.ui.adapters.OrderDetailAdapter;
import com.toplevel.kengmakon.ui.viewmodels.OrdersVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private OrdersVM ordersVM;
    private ProgressDialog progressDialog;
    private OrderDetailAdapter adapter;
    private int order_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        ordersVM = ViewModelProviders.of(this, viewModelFactory).get(OrdersVM.class);
        ordersVM.orderDetailModelLiveData().observe(this, this::onSuccessOrderDetail);
        ordersVM.onFailGetOrderDetailLiveData().observe(this, this::onFailOrderDetail);

        order_id = getIntent().getIntExtra("order_id", 0);

        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        ordersVM.getOrderDetail(preferencesUtil.getTOKEN(), order_id);

        binding.backBtn.setOnClickListener(view -> finish());
    }

    public void onSuccessOrderDetail(OrderDetailModel model) {

        progressDialog.dismiss();
    }
    public void onFailOrderDetail(String error) {
        progressDialog.dismiss();
    }
}
