package com.furniture.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.furniture.kengmakon.api.Api;
import com.furniture.kengmakon.models.CategoriesModel;
import com.furniture.kengmakon.models.FurnitureModel;
import com.furniture.kengmakon.models.SetModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FurnitureVM extends BaseVM {

    Api api;
    Context context;

    private MutableLiveData<SetModel> setModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetSetMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<CategoriesModel> categoriesModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetCategoriesMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<FurnitureModel> furnitureModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetFurnitureMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<FurnitureModel> furnitureSearchModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetFurnitureSearchMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<FurnitureModel> onSuccessGetWishlistMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailGetWishlistMutableLiveData = new MutableLiveData<>();

    @Inject
    public FurnitureVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<SetModel> setModelLiveData() {
        return setModelMutableLiveData;
    }

    public LiveData<String> onFailGetSetLiveData() {
        return onFailGetSetMutableLiveData;
    }

    public LiveData<CategoriesModel> categoriesModelLiveData() {
        return categoriesModelMutableLiveData;
    }

    public LiveData<String> onFailGetCategoriesLiveData() {
        return onFailGetCategoriesMutableLiveData;
    }

    public LiveData<FurnitureModel> furnitureModelLiveData() {
        return furnitureModelMutableLiveData;
    }

    public LiveData<String> onFailGetFurnitureLiveData() {
        return onFailGetFurnitureMutableLiveData;
    }

    public LiveData<FurnitureModel> furnitureSearchModelLiveData() {
        return furnitureSearchModelMutableLiveData;
    }

    public LiveData<String> onFailGetFurnitureSearchLiveData() {
        return onFailGetFurnitureSearchMutableLiveData;
    }

    public LiveData<FurnitureModel> onSuccessGetWishlistLiveData() {
        return onSuccessGetWishlistMutableLiveData;
    }

    public LiveData<String> onFailGetWishlistLiveData() {
        return onFailGetWishlistMutableLiveData;
    }


    public void getSet(int page, int size) {

        addToSubscribe(api.getSet(page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetSetMutableLiveData.postValue(error.getMessage());
                }));
    }


    public void getTopSet() {

        addToSubscribe(api.getTopSet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetSetMutableLiveData.postValue(error.getMessage());
                }));
    }


    public void getCategories(int page, int size) {

        addToSubscribe(api.getCategories(page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    categoriesModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetCategoriesMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getFurniture(String token, int page, int size) {

        addToSubscribe(api.getFurniture("Bearer " + token, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    furnitureSearchModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetFurnitureSearchMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getFurnitureTopList(String token) {

        addToSubscribe(api.getFurnitureTopList("Bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    furnitureModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetFurnitureMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getWishlist(String token) {

        addToSubscribe(api.getWishlist("Bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    onSuccessGetWishlistMutableLiveData.postValue(response);
                }, error -> {
                    onFailGetWishlistMutableLiveData.postValue(error.getMessage());
                }));
    }
}
