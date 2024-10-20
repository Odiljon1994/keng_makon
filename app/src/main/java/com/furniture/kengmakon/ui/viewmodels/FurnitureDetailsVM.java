package com.furniture.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.furniture.kengmakon.api.Api;
import com.furniture.kengmakon.models.CategoryDetailModel;
import com.furniture.kengmakon.models.FurnitureDetailModel;
import com.furniture.kengmakon.models.LikeModel;
import com.furniture.kengmakon.models.SetDetailModel;
import com.furniture.kengmakon.models.SetModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FurnitureDetailsVM extends BaseVM {

    private MutableLiveData<SetDetailModel> setDetailModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetSetDetailMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<CategoryDetailModel> categoryDetailModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetCategoryDetailMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<LikeModel> likeModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailSetLikeMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<FurnitureDetailModel> furnitureDetailMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailFurnitureDetailMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<SetModel> categoryDetailSetMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailCategoryDetailSetMutableLiveData = new MutableLiveData<>();

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

    public LiveData<LikeModel> likeModelLiveData() {
        return likeModelMutableLiveData;
    }

    public LiveData<String> onFailSetLikeLiveData() {
        return onFailSetLikeMutableLiveData;
    }

    public LiveData<FurnitureDetailModel> onSuccessFurnitureDetailLiveData() {
        return furnitureDetailMutableLiveData;
    }

    public LiveData<String> onFailFurnitureDetailLiveData() {
        return onFailFurnitureDetailMutableLiveData;
    }


    public LiveData<SetModel> onSuccessCategorySetDetailLiveData() {
        return categoryDetailSetMutableLiveData;
    }

    public LiveData<String> onFailCategorySetDetailLiveData() {
        return onFailCategoryDetailSetMutableLiveData;
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

    public void getFurnitureDetail(String token, int id) {

        addToSubscribe(api.getFurnitureDetail("Bearer " + token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    furnitureDetailMutableLiveData.postValue(response);
                }, error -> {
                    onFailFurnitureDetailMutableLiveData.postValue(error.getMessage());
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


    public void getCategorySetDetail(String token, int page, int size, int id) {

        addToSubscribe(api.getCategoryDetailSetList("Bearer " + token, page, size, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    categoryDetailSetMutableLiveData.postValue(response);
                }, error -> {
                    onFailCategoryDetailSetMutableLiveData.postValue(error.getMessage());
                }));
    }


    public void setLikeDislike(String token, int furniture_id) {

        LikeModel.LikeReqModel likeReqModel = new LikeModel.LikeReqModel(furniture_id);
        addToSubscribe(api.setLikeDislike("Bearer " + token, likeReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    likeModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailSetLikeMutableLiveData.postValue(error.getMessage());
                }));
    }
}
