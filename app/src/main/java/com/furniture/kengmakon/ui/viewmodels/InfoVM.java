package com.furniture.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.furniture.kengmakon.api.Api;
import com.furniture.kengmakon.models.BranchesModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InfoVM extends BaseVM {

    Api api;
    Context context;

    private MutableLiveData<BranchesModel> branchesModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetBranchesMutableLiveData = new MutableLiveData<>();


    @Inject
    public InfoVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<BranchesModel> branchesModelLiveData() {
        return branchesModelMutableLiveData;
    }

    public LiveData<String> onFailGetBranchesLiveData() {
        return onFailGetBranchesMutableLiveData;
    }

    public void getBranches(String lang) {
        addToSubscribe(api.getBranches(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    branchesModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetBranchesMutableLiveData.postValue(error.getMessage());
                }));
    }
}
