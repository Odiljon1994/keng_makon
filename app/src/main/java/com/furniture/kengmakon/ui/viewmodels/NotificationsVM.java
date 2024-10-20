package com.furniture.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.furniture.kengmakon.api.Api;
import com.furniture.kengmakon.models.NotificationsModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotificationsVM extends BaseVM{
    Api api;
    Context context;
    private MutableLiveData<NotificationsModel> notificationsModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailNotificationsModelMutableLiveData = new MutableLiveData<>();

    @Inject
    public NotificationsVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<NotificationsModel> notificationsModelLiveData() {
        return notificationsModelMutableLiveData;
    }
    public LiveData<String> onFailNotificationsModelLiveData() {
        return onFailNotificationsModelMutableLiveData;
    }

    public void getNotifications(int page, int size, String lang) {

        addToSubscribe(api.getNotifications(page, size, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    notificationsModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailNotificationsModelMutableLiveData.postValue(error.getMessage());
                }));
    }
}
