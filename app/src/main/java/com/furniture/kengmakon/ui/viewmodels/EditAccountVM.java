package com.furniture.kengmakon.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.furniture.kengmakon.api.Api;
import com.furniture.kengmakon.models.BaseResponse;
import com.furniture.kengmakon.models.ForgotPasswordReqModel;
import com.furniture.kengmakon.models.UpdatePasswordReqModel;
import com.furniture.kengmakon.models.UpdateUsernameReqModel;

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

    private MutableLiveData<BaseResponse> updateUsernameMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailUploadUsernameMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<BaseResponse> updatePasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onFailUpdatePasswordMutableLiveData = new MutableLiveData<>();

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

    public LiveData<BaseResponse> onSuccessUploadUserImageLiveData() {
        return uploadUserImageMutableLiveData;
    }

    public LiveData<String> onFailUploadUserImageLiveData() {
        return onFailUploadUserImageMutableLiveData;
    }


    public LiveData<BaseResponse> onSuccessUpdateUsernameLiveData() {
        return updateUsernameMutableLiveData;
    }

    public LiveData<String> onFailUpdateUsernameLiveData() {
        return onFailUploadUsernameMutableLiveData;
    }


    public LiveData<BaseResponse> onSuccessUpdatePasswordLiveData() {
        return updatePasswordMutableLiveData;
    }

    public LiveData<String> onFailUpdatePasswordLiveData() {
        return onFailUpdatePasswordMutableLiveData;
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

    public void postUpdateUsername(String token, String name) {

        UpdateUsernameReqModel updateUsernameReqModel = new UpdateUsernameReqModel(name);
        addToSubscribe(api.updateUsername("Bearer " + token, updateUsernameReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    updateUsernameMutableLiveData.postValue(response);
                }, error -> {
                    onFailUploadUsernameMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void postUpdatePassword(String token, String oldPassword, String newPassword) {

        UpdatePasswordReqModel updatePasswordReqModel = new UpdatePasswordReqModel(oldPassword, newPassword);
        addToSubscribe(api.updatePassword("Bearer " + token, updatePasswordReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    updatePasswordMutableLiveData.postValue(response);
                }, error -> {
                    onFailUpdatePasswordMutableLiveData.postValue(error.getMessage());
                }));
    }

    public void uploadUserImagePassword(File file,
                                        String name,
                                        String token) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

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
