package com.toplevel.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.models.ActionsModel;
import com.toplevel.kengmakon.models.SetModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActionsVM extends BaseVM{

    Api api;
    Context context;

    private MutableLiveData<ActionsModel> actionsModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetActionsMutableLiveData = new MutableLiveData<>();

    @Inject
    public ActionsVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<ActionsModel> actionsModelLiveData() {
        return actionsModelMutableLiveData;
    }
    public LiveData<String> onFailGetActions() {
        return onFailGetActionsMutableLiveData;
    }

    public void getActions(String lang, int page, int size) {

        addToSubscribe(api.getActions(page, size, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    actionsModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetActionsMutableLiveData.postValue(error.getMessage());
                }));
    }

}
