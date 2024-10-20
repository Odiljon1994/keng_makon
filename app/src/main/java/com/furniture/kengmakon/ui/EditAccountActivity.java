package com.furniture.kengmakon.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.furniture.kengmakon.MyApp;
import com.furniture.kengmakon.di.ViewModelFactory;
import com.furniture.kengmakon.models.BaseResponse;
import com.furniture.kengmakon.ui.dialogs.ChangePasswordDialog;
import com.furniture.kengmakon.ui.viewmodels.EditAccountVM;
import com.furniture.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.R;
import com.furniture.kengmakon.api.ApiUtils;
import com.toplevel.kengmakon.databinding.ActivityEditAccountBinding;

import java.io.File;

import javax.inject.Inject;

public class EditAccountActivity extends AppCompatActivity {
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private ActivityEditAccountBinding binding;
    private ProgressDialog progressDialog;
    String path = "";
    public static final int PICK_IMAGE = 1;
    private final int permissionRequestCode = 1000;
    private Bitmap bitmap;
    private final int PICTURE_RESULT = 111;
    private Uri imageUri;
    private EditAccountVM editAccountVM;
    private boolean isAccountInfoChanged = false;
    private String enteredNewPassword = "";
    AlertDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account);
        editAccountVM = ViewModelProviders.of(this, viewModelFactory).get(EditAccountVM.class);
        editAccountVM.onSuccessUploadUserImageLiveData().observe(this, this::onSuccessUploadUserImage);
        editAccountVM.onFailUploadUserImageLiveData().observe(this, this::onFailUploadUserImage);
        editAccountVM.onSuccessUpdateUsernameLiveData().observe(this, this::onSuccessUpdateUsername);
        editAccountVM.onFailUpdateUsernameLiveData().observe(this, this::onFailUpdateUsername);
        editAccountVM.onSuccessUpdatePasswordLiveData().observe(this, this::onSuccessUpdatePassword);
        editAccountVM.onFailUpdatePasswordLiveData().observe(this, this::onFailUpdatePassword);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.name.setText(preferencesUtil.getName());
        binding.phoneNumber.setText(preferencesUtil.getPhoneNumber());
        binding.email.setText(preferencesUtil.getEmail());

        binding.changePassword.setOnClickListener(view -> {
            showDialog();
        });

        if (!preferencesUtil.getImageUrl().equals("")) {
            Glide.with(this).load(ApiUtils.getBaseUrl() + preferencesUtil.getImageUrl()).into(binding.userImage);
        }

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!binding.name.getText().toString().equals(preferencesUtil.getName()) && binding.name.getText().toString().length() > 0) {
                    binding.saveUsername.setVisibility(View.VISIBLE);
                } else {
                    binding.saveUsername.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.saveUsername.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.name.getText().toString())) {
                progressDialog = ProgressDialog.show(this, "", getString(R.string.loading), true);
                editAccountVM.postUpdateUsername(preferencesUtil.getTOKEN(), binding.name.getText().toString());
            } else {
                binding.name.setError("");
            }
        });

        binding.savePhoto.setOnClickListener(view -> {
            if (isReadStoragePermissionGranted()) {
                progressDialog = ProgressDialog.show(this, "", getString(R.string.loading), true);
                String imageUrl = getRealPathFromURI(imageUri);
                File imageFile = new File(imageUrl);
                editAccountVM.uploadUserImagePassword(imageFile, preferencesUtil.getName(), preferencesUtil.getTOKEN());

            }
        });

        binding.selectUserImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

    public void showDialog() {
        ChangePasswordDialog baseDialog = new ChangePasswordDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(baseDialog);
        dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ChangePasswordDialog.ClickListener clickListener = new ChangePasswordDialog.ClickListener() {
            @Override
            public void onClickChangePasswordBtn(String oldPassword, String newPassword, String confirmPassword) {
                ChangePasswordDialog.ClickListener.super.onClickChangePasswordBtn(oldPassword, newPassword, confirmPassword);

                if (oldPassword.equals(preferencesUtil.getPassword())
                        && !TextUtils.isEmpty(newPassword)
                        && newPassword.equals(confirmPassword)) {
                    progressDialog = ProgressDialog.show(EditAccountActivity.this, "", getString(R.string.loading), true);
                    enteredNewPassword = newPassword;
                    editAccountVM.postUpdatePassword(preferencesUtil.getTOKEN(), oldPassword, newPassword);

                } else if (!oldPassword.equals(preferencesUtil.getPassword())){
                    baseDialog.setError(getString(R.string.incorrect_current_pwd));
                } else if (newPassword.equals("")) {
                    baseDialog.setError("Yangi parolni kiriting..");
                } else if (!newPassword.equals(confirmPassword)) {
                    baseDialog.setError(getString(R.string.error_confirm_pwd));
                }
            }
        };
        baseDialog.setClickListener(clickListener);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void onSuccessUpdateUsername(BaseResponse model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {

            System.out.println("Message: " + model.getMessage());
            System.out.println(model.getCode());
            Toast.makeText(this, getString(R.string.fullname_change_success), Toast.LENGTH_SHORT).show();
            isAccountInfoChanged = true;
            binding.saveUsername.setVisibility(View.INVISIBLE);

            preferencesUtil.saveName(binding.name.getText().toString());

        }
    }

    public void onFailUpdateUsername(String error) {
        progressDialog.dismiss();
        System.out.println("Error **");
        System.out.println(error);
        System.out.println("Error **");
        binding.error.setText("Tizimda hatolik yuz berdi.");
    }

    public void onSuccessUpdatePassword(BaseResponse model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {

            System.out.println("Message: " + model.getMessage());
            System.out.println(model.getCode());
            Toast.makeText(this, getString(R.string.success_change_pwd), Toast.LENGTH_SHORT).show();
            isAccountInfoChanged = true;
            binding.saveUsername.setVisibility(View.INVISIBLE);

            preferencesUtil.savePassword(enteredNewPassword);

            dialog.dismiss();
        } else {
            binding.error.setText("Tizimda hatolik yuz berdi. " + model.getMessage());
        }
    }

    public void onFailUpdatePassword(String error) {
        progressDialog.dismiss();
        System.out.println("Error **");
        System.out.println(error);
        System.out.println("Error **");
        binding.error.setText("Tizimda hatolik yuz berdi.");
    }


    public void onSuccessUploadUserImage(BaseResponse model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {

            System.out.println("Message: " + model.getMessage());
            System.out.println(model.getCode());
            Toast.makeText(this, getString(R.string.photo_upload_success), Toast.LENGTH_SHORT).show();
            isAccountInfoChanged = true;
            binding.savePhoto.setVisibility(View.INVISIBLE);


        }
    }

    public void onFailUploadUserImage(String error) {
        progressDialog.dismiss();
        System.out.println("Error **");
        System.out.println(error);
        System.out.println("Error **");
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        isReadStoragePermissionGranted();
        switch (requestCode) {

            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            Bitmap rotatedBitmap = rotateImage(bitmap, 90);
                            binding.userImage.setImageBitmap(rotatedBitmap);

                            binding.savePhoto.setVisibility(View.VISIBLE);

                            // imageurl = getRealPathFromURI(imageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
            case PICK_IMAGE:
                if (requestCode == PICK_IMAGE) {
                    // Get the url of the image from data
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            imageUri = selectedImageUri;
                            path = data.getData().getPath();
                            binding.savePhoto.setVisibility(View.VISIBLE);
                            // update the preview image in the layout
                            binding.userImage.setImageURI(selectedImageUri);

                        }
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("ok");

                return true;
            } else {

                System.out.println("no");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionRequestCode);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            System.out.println("ok");
            return true;
        }
    }
}
