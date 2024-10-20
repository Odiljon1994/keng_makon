package com.furniture.kengmakon.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.furniture.kengmakon.ui.adapters.NotificationsAdapter;
import com.furniture.kengmakon.ui.viewmodels.NotificationsVM;
import com.furniture.kengmakon.utils.NetworkChangeListener;
import com.furniture.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityNotificationsBinding;
import com.furniture.kengmakon.models.NotificationsModel;

import javax.inject.Inject;

public class NotificationsActivity extends AppCompatActivity {

    private ActivityNotificationsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private NotificationsVM notificationsVM;
    private ProgressDialog progressDialog;
    private int page = 1;
    private int size = 50;
    private NotificationsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        notificationsVM = ViewModelProviders.of(this, viewModelFactory).get(NotificationsVM.class);
        notificationsVM.notificationsModelLiveData().observe(this, this::onSuccessGetNotifications);
        notificationsVM.onFailNotificationsModelLiveData().observe(this, this::onFailGetNotifications);

        binding.swipeRefreshLayout.setRefreshing(true);
        notificationsVM.getNotifications(page, size, preferencesUtil.getLANGUAGE());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationsAdapter(this, preferencesUtil.getLANGUAGE(), item -> {
            Intent intent = new Intent(this, NotificationDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("date", item.getCreated_at());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

        binding.backBtn.setOnClickListener(view -> finish());
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            notificationsVM.getNotifications(page, size, preferencesUtil.getLANGUAGE());
        });
    }

    public void onSuccessGetNotifications(NotificationsModel model) {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200) {
            adapter.setItems(model.getData().getItems());
        }
    }
    public void onFailGetNotifications(String error) {
        binding.swipeRefreshLayout.setRefreshing(false);
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
