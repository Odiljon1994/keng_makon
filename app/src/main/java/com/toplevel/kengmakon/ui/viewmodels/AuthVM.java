package com.toplevel.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.models.PushTokenReqModel;
import com.toplevel.kengmakon.models.SignUpModel;
import com.toplevel.kengmakon.models.UserInfoModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthVM extends BaseVM {

    Api api;
    Context context;

    private MutableLiveData<BaseResponse> signUpMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailSignUpMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<LoginModel.LoginResModel> loginModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailLoginMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<UserInfoModel> userInfoModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailUserInfoMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<BaseResponse> pushTokenMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailPushTokenMutableLiveData = new MutableLiveData<>();


    @Inject
    public AuthVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<BaseResponse> signUpLiveData() {
        return signUpMutableLiveData;
    }

    public LiveData<String> onFailSignUpLiveData() {
        return onFailSignUpMutableLiveData;
    }

    public LiveData<LoginModel.LoginResModel> loginModelLiveData() {
        return loginModelMutableLiveData;
    }

    public LiveData<String> onFailLoginLiveData() {
        return onFailLoginMutableLiveData;
    }

    public LiveData<UserInfoModel> userInfoSuccessLiveData() {
        return userInfoModelMutableLiveData;
    }

    public LiveData<String> onFailUserInfoLiveData() {
        return onFailUserInfoMutableLiveData;
    }

    public LiveData<BaseResponse> onSuccessPushTokenLiveData() {
        return pushTokenMutableLiveData;
    }

    public LiveData<String> onFailPushTokenLiveData() {
        return onFailPushTokenMutableLiveData;
    }

    public void signUp(String name, String phoneNumber, String emailAddress, String password) {
        SignUpModel signUpModel = new SignUpModel(name, phoneNumber, emailAddress, password);
        addToSubscribe(api.signUp(signUpModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    signUpMutableLiveData.postValue(response);
                }, error -> {
                    onFailSignUpMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void login(String email, String password) {
        LoginModel loginModel = new LoginModel(email, password);
        addToSubscribe(api.login(loginModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    loginModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailLoginMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void getUserInfo(String token) {

        addToSubscribe(api.getUserInfo("Bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    userInfoModelMutableLiveData.postValue(response);
                }, error -> {
                    onFailUserInfoMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void pushToken(String token, String appToken) {

        PushTokenReqModel pushTokenReqModel = new PushTokenReqModel(appToken);
        addToSubscribe(api.pushToken("Bearer " + token, pushTokenReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    pushTokenMutableLiveData.postValue(response);
                }, error -> {
                    onFailPushTokenMutableLiveData.postValue(error.getMessage());
                }));
    }
}
