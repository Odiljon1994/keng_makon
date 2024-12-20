package com.furniture.kengmakon.ui;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityOrderDetailBinding;
import com.furniture.kengmakon.models.OrderDetailModel;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.ui.adapters.OrderDetailAdapter;
import com.furniture.kengmakon.ui.viewmodels.OrdersVM;
import com.furniture.kengmakon.utils.NetworkChangeListener;
import com.furniture.kengmakon.utils.PreferencesUtil;

import java.text.DecimalFormat;

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
    private int totalItems = 0;

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

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new OrderDetailAdapter(this, preferencesUtil.getLANGUAGE());
        binding.recyclerView.setAdapter(adapter);

        binding.backBtn.setOnClickListener(view -> finish());
    }

    public void onSuccessOrderDetail(OrderDetailModel model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {
            if (model.getData().getFurnitures().size() > 0) {
                adapter.setItems(model.getData().getFurnitures());

                for (int i = 0; i < model.getData().getFurnitures().size(); i++) {
                    totalItems += model.getData().getFurnitures().get(i).getInfo().getAmount();
                }
                binding.totalItems.setText(totalItems + "x");
            }
            if (!TextUtils.isEmpty(model.getData().getSeller().getName())) {
                binding.sellerName.setText(model.getData().getSeller().getName());
            }
            if (!TextUtils.isEmpty(model.getData().getStore().getName())) {

                try {
                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getData().getStore().getName(), PushNotificationModel.class);

                    if (preferencesUtil.getLANGUAGE().equals("uz")) {
                        binding.branch.setText(pushNotificationTitleModel.getUz());
                    } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                        binding.branch.setText(pushNotificationTitleModel.getRu());
                    } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                        binding.branch.setText(pushNotificationTitleModel.getEn());
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

                //  binding.branch.setText(model.getData().getStore().getName());
            }

            if (!TextUtils.isEmpty(model.getData().getOrder().getDate())) {
                String[] datePairs = model.getData().getOrder().getDate().split("T");
                binding.date.setText(datePairs[0]);
            }

            if (!TextUtils.isEmpty(model.getData().getOrder().getTime_from())) {
                binding.hour.setText(model.getData().getOrder().getTime_from());
            }

            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.totalSum.setText(df.format(model.getData().getPayment().getTotal_cost()) + "sum");
            double unpaidSum = model.getData().getPayment().getTotal_cost() - model.getData().getPayment().getTotal_payment();
            binding.unpaidSum.setText(df.format(unpaidSum) + "sum");
            binding.paidSum.setText(df.format(model.getData().getPayment().getTotal_payment()) + "sum");

            if (!TextUtils.isEmpty(model.getData().getOrder().getDoc_no())) {
                binding.docNo.setText(model.getData().getOrder().getDoc_no());
            }

            if (model.getData().getPayment().getTotal_cost() > model.getData().getPayment().getTotal_payment()) {
                binding.isPaymentCompleted.setText(getString(R.string.not_completed));
                binding.isPaymentCompleted.setBackground(getDrawable(R.drawable.payment_not_completed));
                binding.isPaymentCompleted.setTextColor(Color.parseColor("#CC0000"));
            } else {
                binding.isPaymentCompleted.setText(getString(R.string.completed));
                binding.isPaymentCompleted.setBackground(getDrawable(R.drawable.payment_completed));
                binding.isPaymentCompleted.setTextColor(Color.parseColor("#19AC4B"));
            }

            //double cashback = model.getData().getPayment().getTotal_cost() / 100;
            double cashback = model.getData().getPayment().getCashback();

            binding.cashback.setText(df.format(cashback) + "sum");

        }
    }

    public void onFailOrderDetail(String error) {
        progressDialog.dismiss();
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
