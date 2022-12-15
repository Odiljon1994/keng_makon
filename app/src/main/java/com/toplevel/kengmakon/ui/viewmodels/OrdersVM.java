package com.toplevel.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.models.OrdersModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrdersVM extends BaseVM {
    Api api;
    Context context;

    private MutableLiveData<OrdersModel> ordersModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetOrdersMutableLiveData = new MutableLiveData<>();

    @Inject
    public OrdersVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<OrdersModel> ordersModelLiveData() {
        return ordersModelMutableLiveData;
    }

    public LiveData<String> onFailGetOrdersLiveData() {
        return onFailGetOrdersMutableLiveData;
    }

    public void getOrders(String token) {

        addToSubscribe(api.getOrders("Bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    ordersModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetOrdersMutableLiveData.postValue(error.getMessage());
                }));
    }
}
