package com.toplevel.kengmakon.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentSettingsBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.UserInfoModel;
import com.toplevel.kengmakon.ui.FeedbackActivity;
import com.toplevel.kengmakon.ui.LoginActivity;
import com.toplevel.kengmakon.ui.MainActivity;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    AuthVM authVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View view = binding.getRoot();
        authVM = ViewModelProviders.of(this, viewModelFactory).get(AuthVM.class);
        authVM.userInfoSuccessLiveData().observe(getActivity(), this::onSuccessUserInfo);
        authVM.onFailUserInfoLiveData().observe(getActivity(), this::onFailUserInfo);

        if (!preferencesUtil.getIsIsSignedIn()) {
            binding.logout.setVisibility(View.INVISIBLE);
        }

        binding.languageTxt.setText(preferencesUtil.getLANGUAGE());
        if (preferencesUtil.getIsIsSignedIn() && preferencesUtil.getName().equals("")) {
            authVM.getUserInfo(preferencesUtil.getTOKEN());
        } else {
            binding.name.setText(preferencesUtil.getName());
        }

        binding.feedback.setOnClickListener(view1 -> {
            if (preferencesUtil.getIsIsSignedIn()) {
                Utils.setAppLocale(getContext(), preferencesUtil.getLANGUAGE());
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
            } else {
                showDialog();
            }
        });

        return view;
    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(getActivity());
        baseDialog.setTitle("Siz ro'yxatdan o'tmadingiz", "Ro'yxatdan o'tishni hohlaysizmi?");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClickOk() {
                BaseDialog.ClickListener.super.onClickOk();
                dialog.dismiss();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }

            @Override
            public void onClickNo() {
                BaseDialog.ClickListener.super.onClickNo();
                dialog.dismiss();
            }
        };
        baseDialog.setClickListener(clickListener);
    }

    public void onSuccessUserInfo(UserInfoModel model) {

        if (model.getCode() == 200) {
            preferencesUtil.saveName(model.getData().getName());
            preferencesUtil.savePhoneNumber(model.getData().getPhone());
            binding.name.setText(model.getData().getName());
        }
    }

    public void onFailUserInfo(String error) {


    }
}
