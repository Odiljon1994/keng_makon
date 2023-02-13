package com.toplevel.kengmakon.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.api.Api;
import com.toplevel.kengmakon.api.ApiUtils;
import com.toplevel.kengmakon.databinding.FragmentSettingsBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.RecentlyViewedModel;
import com.toplevel.kengmakon.models.UserInfoModel;
import com.toplevel.kengmakon.ui.AboutActivity;
import com.toplevel.kengmakon.ui.BranchesActivity;
import com.toplevel.kengmakon.ui.ChooseLanguageActivity;
import com.toplevel.kengmakon.ui.EditAccountActivity;
import com.toplevel.kengmakon.ui.FeedbackActivity;
import com.toplevel.kengmakon.ui.LoginActivity;
import com.toplevel.kengmakon.ui.MainActivity;
import com.toplevel.kengmakon.ui.OrdersActivity;
import com.toplevel.kengmakon.ui.dialogs.BaseDialog;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.utils.LanguageChangeListener;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.RecentlyViewedDB;
import com.toplevel.kengmakon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    AuthVM authVM;
    private String language = "";
    private LanguageChangeListener languageChangeListener;
    private RecentlyViewedDB recentlyViewedDB;

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
            binding.editAccount.setVisibility(View.GONE);
        }

        binding.editAccount.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), EditAccountActivity.class));
        });

        binding.language.setOnClickListener(view1 -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogThema);
            View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.bottom_sheet_select_language, (LinearLayout) view.findViewById(R.id.bottomSheet));


            ImageView ruImage = bottomSheetView.findViewById(R.id.ruDot);
            ImageView uzImage = bottomSheetView.findViewById(R.id.uzDot);
            ImageView enImage = bottomSheetView.findViewById(R.id.enDot);

            TextView selectLangTxt = bottomSheetView.findViewById(R.id.selectLangTxt);
            selectLangTxt.setText(getActivity().getString(R.string.select_language));

            language = preferencesUtil.getLANGUAGE();
            if (preferencesUtil.getLANGUAGE().equals("ru")) {
                ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                uzImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                enImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                uzImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
            }
            bottomSheetView.findViewById(R.id.uzLayout).setOnClickListener(view2 -> {
                language = "uz";
                if (!preferencesUtil.getLANGUAGE().equals("uz")) {
                    uzImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                    ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));

                    preferencesUtil.saveLanguage("uz");
                    Utils.setAppLocale(getActivity(), "uz");
                    languageChangeListener.languageChange();

                } else {
                    uzImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                    ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));

                }
                bottomSheetDialog.dismiss();

            });

            bottomSheetView.findViewById(R.id.ruLayout).setOnClickListener(view2 -> {
                language = "ru";
                if (!preferencesUtil.getLANGUAGE().equals("ru")) {
                    ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                    uzImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));

                    preferencesUtil.saveLanguage("ru");
                    Utils.setAppLocale(getActivity(), "ru");
                    languageChangeListener.languageChange();
                } else {
                    ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                    uzImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));

                }
                bottomSheetDialog.dismiss();


            });

            bottomSheetView.findViewById(R.id.enLayout).setOnClickListener(view2 -> {
                language = "en";
                if (!preferencesUtil.getLANGUAGE().equals("en")) {
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                    ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));

                    preferencesUtil.saveLanguage("en");
                    Utils.setAppLocale(getActivity(), "en");
                    languageChangeListener.languageChange();
                } else {
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.selected_lng));
                    ruImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));
                    enImage.setImageDrawable(getActivity().getDrawable(R.drawable.unselected_lng));

                }
                bottomSheetDialog.dismiss();

            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        binding.logout.setOnClickListener(view1 -> {
            showLogOutDialog();
        });

        binding.branches.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), BranchesActivity.class)));

        binding.about.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AboutActivity.class)));
        binding.myOrders.setOnClickListener(view1 -> {
            if (preferencesUtil.getIsIsSignedIn()) {
                Utils.setAppLocale(getContext(), preferencesUtil.getLANGUAGE());
                startActivity(new Intent(getActivity(), OrdersActivity.class));
            } else {
                showDialog();
            }

        });



        binding.languageTxt.setText(preferencesUtil.getLANGUAGE());



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

    @Override
    public void onResume() {
        super.onResume();
        binding.name.setText(preferencesUtil.getName());
        if (preferencesUtil.getIsIsSignedIn()) {

            authVM.getUserInfo(preferencesUtil.getTOKEN());
        }
    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(getActivity());
        baseDialog.setTitle(getString(R.string.not_logged_in), getString(R.string.want_to_login), "");
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

    public void showLogOutDialog() {
        BaseDialog baseDialog = new BaseDialog(getActivity());
        baseDialog.setTitle("Chindan ham dasturdan chiqmoqchimisiz?", "", "");
        baseDialog.changeBtnText("Yo'q", "Ha");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClickOk() {
                BaseDialog.ClickListener.super.onClickOk();
                dialog.dismiss();
                preferencesUtil.saveIsSignedIn(false);
                preferencesUtil.saveName("");
                preferencesUtil.savePassword("");
                preferencesUtil.saveEmail("");
                preferencesUtil.savePhoneNumber("");
                preferencesUtil.saveTOKEN("");
                preferencesUtil.saveUserId(-1);
                preferencesUtil.saveIsPushTokenDone(false);

                recentlyViewedDB = new RecentlyViewedDB(getContext());

                List<RecentlyViewedModel> list = getDataFromDB();

                if (list != null && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {

                        recentlyViewedDB.deleteRow(list.get(i));

                    }
                }


                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();

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
            preferencesUtil.saveUserId(model.getData().getId());
            preferencesUtil.savePhoneNumber(model.getData().getPhone());
            binding.name.setText(model.getData().getName());
            if (!TextUtils.isEmpty(model.getData().getImage())) {
                preferencesUtil.saveImageUrl(model.getData().getImage());
                Glide.with(getActivity()).load(ApiUtils.getBaseUrl() + model.getData().getImage()).into(binding.userImage);
            }
        }
    }

    public void onFailUserInfo(String error) {


    }

    public void setLanguageChangeListener(LanguageChangeListener languageChangeListener) {
        this.languageChangeListener = languageChangeListener;
    }

    public List<RecentlyViewedModel> getDataFromDB() {
        List<RecentlyViewedModel> list = new ArrayList<>();
        Cursor cursor = recentlyViewedDB.getData();

        while (cursor.moveToNext()) {
            list.add(new RecentlyViewedModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }

        return list;
    }


}
