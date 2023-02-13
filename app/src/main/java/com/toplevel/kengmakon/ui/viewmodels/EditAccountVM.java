package com.toplevel.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.ForgotPasswordReqModel;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditAccountVM extends BaseVM{

    Api api;
    Context context;

    private MutableLiveData<BaseResponse> forgetPasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailForgetPasswordMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<BaseResponse> uploadUserImageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailUploadUserImageMutableLiveData = new MutableLiveData<>();

    @Inject
    public EditAccountVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<BaseResponse> forgetPasswordLiveData() {
        return forgetPasswordMutableLiveData;
    }

    public LiveData<String> onFailForgetPasswordLiveData() {
        return onFailForgetPasswordMutableLiveData;
    }

    public LiveData<BaseResponse> onSuccessUploadUserImagePasswordLiveData() {
        return uploadUserImageMutableLiveData;
    }

    public LiveData<String> onFailUploadUserImageLiveData() {
        return onFailUploadUserImageMutableLiveData;
    }

    public void postForgetPassword(String email) {

        ForgotPasswordReqModel forgotPasswordReqModel = new ForgotPasswordReqModel(email);
        addToSubscribe(api.forgotPassword(forgotPasswordReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    forgetPasswordMutableLiveData.postValue(response);
                }, error -> {
                    onFailForgetPasswordMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void uploadUserImagePassword(File file,
                                        String name,
                                        String token) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody fullNameUpload =
                RequestBody.create(MediaType.parse("multipart/form-data"), name);


        addToSubscribe(api.uploadUserImage("Bearer " + token, fullNameUpload, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    uploadUserImageMutableLiveData.postValue(response);
                }, error -> {
                    onFailUploadUserImageMutableLiveData.postValue(error.getMessage());
                }));
    }
}
