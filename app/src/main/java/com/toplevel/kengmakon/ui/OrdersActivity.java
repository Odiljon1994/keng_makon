package com.toplevel.kengmakon.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityMyOrdersBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.OrdersModel;
import com.toplevel.kengmakon.ui.adapters.MyOrdersAdapter;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.dialogs.LoadingDialog;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.ui.viewmodels.OrdersVM;
import com.toplevel.kengmakon.utils.NetworkChangeListener;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class OrdersActivity extends AppCompatActivity {

    ActivityMyOrdersBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    AlertDialog dialog;
    private OrdersVM ordersVM;
    private ProgressDialog progressDialog;
    private MyOrdersAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
        ordersVM = ViewModelProviders.of(this, viewModelFactory).get(OrdersVM.class);
        ordersVM.ordersModelLiveData().observe(this, this::onSuccessGetOrders);
        ordersVM.onFailGetOrdersLiveData().observe(this, this::onFailGetOrders);



        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ordersVM.getOrders(preferencesUtil.getTOKEN());
            }
        });

        if (preferencesUtil.getIsIsSignedIn()) {
            showLoadingDialog();
            //progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            ordersVM.getOrders(preferencesUtil.getTOKEN());
        }


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyOrdersAdapter(this, model -> {
            Intent intent = new Intent(OrdersActivity.this, OrderDetailActivity.class);
            intent.putExtra("order_id", model.getOrder().getId());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

        binding.backBtn.setOnClickListener(view -> finish());
    }

    public void showLoadingDialog() {
        LoadingDialog loadingDialog = new LoadingDialog(this);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
        alertBuilder.setView(loadingDialog);
        dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    public void onSuccessGetOrders(OrdersModel model) {
        dialog.dismiss();
       // progressDialog.dismiss();
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
            adapter.setItems(model.getData().getItems());
        } else {
            binding.swipeRefreshLayout.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        }
    }
    public void onFailGetOrders(String error) {
        dialog.dismiss();
        binding.swipeRefreshLayout.setRefreshing(false);
     //   progressDialog.dismiss();
    }

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
