package com.toplevel.kengmakon.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityEditAccountBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.ui.viewmodels.EditAccountVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account);
        editAccountVM = ViewModelProviders.of(this, viewModelFactory).get(EditAccountVM.class);
        editAccountVM.onSuccessUploadUserImagePasswordLiveData().observe(this, this::onSuccessUploadUserImage);
        editAccountVM.onFailUploadUserImageLiveData().observe(this, this::onFailUploadUserImage);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.name.setText(preferencesUtil.getName());
        binding.phoneNumber.setText(preferencesUtil.getPhoneNumber());
        binding.email.setText(preferencesUtil.getEmail());

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

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onSuccessUploadUserImage(BaseResponse model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {

            System.out.println("Message: " + model.getMessage());
            System.out.println(model.getCode());
            //startActivity(new Intent(this, CustomerInfoActivity.class));


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
