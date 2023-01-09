package com.toplevel.kengmakon.ui;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.ui.viewmodels.OrdersVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class OrdersActivity extends AppCompatActivity {

    ActivityMyOrdersBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;

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

//        preferencesUtil.saveTOKEN("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiY2YwM2RiOWEwNzc2ZWI3NWQ5MDM4OGFmMDBkNjE3ZDJkZGQwMzlhYjlhYWQyZDdiY2YyNjk4Nzg1MWE4M2IxNDJhNjY1ODUwZThhZDBhM2EiLCJpYXQiOjE2NzExOTM3NDYuNzg4MTg4LCJuYmYiOjE2NzExOTM3NDYuNzg4MTg5LCJleHAiOjE2NzEyMDA5NDYuNzg2NzUzLCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.H1ag8WKMrdKdxw9SH81ebE0_JldKe7l1XEUR_goKopRPq0KZoQ10xsY4DwWh7fShbQfhK20GK2KpFLgiTFotOJuc7zeGs_teecGI6UhAFIOCE5S12P7L2dzkTJw29ntw58g3JwJB2RQlLcmOV4K5_eJRAl4CkdZ4wasR4TKCM6GNHktWx7hM-GgXCS8JDwcNYw8F5LYd8pUweBZRcZc7vj505VrgPA3W3mspEawiLUIAP7ZXK37N_kDUGG4saAmmlinUzL4UZ3O4P-dTtAiIvpP5RLo63fMUCkR79ewxUIKRJs9qRnbo9F4OU5gckvsktFkkR-HuMvahL9MBKCBgLF12_47suHp10eBG-BtT8ylwz0RLXb9a4o_VYAWVy8C1JFte9LcLogBnUwkEe4YqRGFmKb210jJ-i6ugMofw21A1Caaq0ksvPjbpQ_D_2UNG0juP1cnp2muddIjD43BIgo5cUV1AlodWt4XTsjFjnt_mMqnCdd0Ur841Kg4FozIOmBg-Oz9HRJUYXg6mTiWpjsZpqwFGa5yzmxIPCYgpWhxfmyglfNVGCTIMCTUhRu5Omrmmjkwtm95U7BU075uwil5OHi-MFwISSvez2JQcNA9UigTZD18eTAz4TGBFTwCIxjehHY52CfADsgSihETUmoPcPx9BcsswXrZzXVUedyA");

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ordersVM.getOrders(preferencesUtil.getTOKEN());
            }
        });

        if (preferencesUtil.getIsIsSignedIn()) {
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
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

    public void onSuccessGetOrders(OrdersModel model) {
        progressDialog.dismiss();
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
        binding.swipeRefreshLayout.setRefreshing(false);
        progressDialog.dismiss();
    }
}
