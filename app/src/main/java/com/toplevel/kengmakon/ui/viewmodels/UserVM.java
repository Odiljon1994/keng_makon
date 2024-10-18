package com.toplevel.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.CashbackModel;
import com.toplevel.kengmakon.models.DiscountModel;
import com.toplevel.kengmakon.models.FeedbackModel;
import com.toplevel.kengmakon.models.SetModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserVM extends BaseVM {
    Api api;
    Context context;

    private MutableLiveData<BaseResponse> onSuccessPostFeedbackMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailPostFeedbackMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<CashbackModel> cashbackModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailCashbackMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<DiscountModel> discountModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailDiscountMutableLiveData = new MutableLiveData<>();

    @Inject
    public UserVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<BaseResponse> onSuccessFeedbackLiveData() {
        return onSuccessPostFeedbackMutableLiveData;
    }

    public LiveData<String> onFailFeedbackLiveData() {
        return onFailPostFeedbackMutableLiveData;
    }

    public LiveData<CashbackModel> onSuccessCashbackLiveData() {
        return cashbackModelMutableLiveData;
    }
    public LiveData<String> onFailCashbackLiveData() {return onFailCashbackMutableLiveData;}

    public LiveData<DiscountModel> onSuccessDiscountLiveData() {
        return discountModelMutableLiveData;
    }
    public LiveData<String> onFailDiscountLiveData() {return onFailDiscountMutableLiveData;}

    public void postFeedback(String token, String subject, String message) {

        FeedbackModel feedbackModel = new FeedbackModel(1, subject, message, 1);

        addToSubscribe(api.postFeedback("Bearer " + token, feedbackModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    onSuccessPostFeedbackMutableLiveData.postValue(response);
                }, error -> {
                    onFailPostFeedbackMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getCashback(String token, int user_id) {

        addToSubscribe(api.getCashback("Bearer " + token, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    cashbackModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailCashbackMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getDiscount(String token) {

        addToSubscribe(api.getDiscount("Bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    discountModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailDiscountMutableLiveData.postValue(error.getMessage());
                }));
    }
}
