package com.toplevel.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.models.CategoryDetailModel;
import com.toplevel.kengmakon.models.SetDetailModel;
import com.toplevel.kengmakon.models.SetModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FurnitureDetailsVM extends BaseVM {

    private MutableLiveData<SetDetailModel> setDetailModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetSetDetailMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<CategoryDetailModel> categoryDetailModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetCategoryDetailMutableLiveData = new MutableLiveData<>();

    private Api api;
    private Context context;

    @Inject
    public FurnitureDetailsVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<SetDetailModel> setModelLiveData() {
        return setDetailModelMutableLiveData;
    }

    public LiveData<String> onFailGetSetDetailLiveData() {
        return onFailGetSetDetailMutableLiveData;
    }

    public LiveData<CategoryDetailModel> successCategoryDetailLiveData() {
        return categoryDetailModelMutableLiveData;
    }

    public LiveData<String> onFailGetCategoryDetailLiveData() {
        return onFailGetCategoryDetailMutableLiveData;
    }

    public void getSet(String token, int id) {

        addToSubscribe(api.getSetDetailList("Bearer " + token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setDetailModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetSetDetailMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getCategoryDetail(String token, int page, int size, int id) {

        addToSubscribe(api.getCategoryDetailList("Bearer " + token, page, size, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    categoryDetailModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetCategoryDetailMutableLiveData.postValue(error.getMessage());
                }));
    }
}
