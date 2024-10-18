package com.toplevel.kengmakon.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentCashbackBinding;
import com.toplevel.kengmakon.databinding.FragmentNewCashbackBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.CashbackModel;
import com.toplevel.kengmakon.models.DiscountModel;
import com.toplevel.kengmakon.ui.CashbackDetailActivity;
import com.toplevel.kengmakon.ui.DiscountDetailActivity;
import com.toplevel.kengmakon.ui.adapters.CashbackAdapter;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

public class NewCashbackFragment extends Fragment {

    FragmentNewCashbackBinding binding;
    private CashbackAdapter adapter;
    private UserVM userVM;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_cashback, container, false);
        View view = binding.getRoot();
        userVM = ViewModelProviders.of(this, viewModelFactory).get(UserVM.class);
        userVM.onSuccessCashbackLiveData().observe(getActivity(), this::onSuccessCashback);
        userVM.onFailCashbackLiveData().observe(getActivity(), this::onFailCashback);
        userVM.onSuccessDiscountLiveData().observe(getActivity(), this::onSuccessDiscount);
        userVM.onFailDiscountLiveData().observe(getActivity(), this::onFailDiscount);


        userVM.getCashback(preferencesUtil.getTOKEN(), preferencesUtil.getUserId());
        userVM.getDiscount(preferencesUtil.getTOKEN());

        binding.detail.setOnClickListener(view1 ->  startActivity(new Intent(getActivity(), CashbackDetailActivity.class)));
        binding.detailDiscount.setOnClickListener(view1 ->  startActivity(new Intent(getActivity(), DiscountDetailActivity.class)));

        if (!TextUtils.isEmpty(preferencesUtil.getName())) {
            binding.userName.setText(preferencesUtil.getName().toUpperCase(Locale.ROOT));
            binding.userNameDiscount.setText(preferencesUtil.getName().toUpperCase(Locale.ROOT));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(preferencesUtil.getName())) {
            binding.userName.setText(preferencesUtil.getName().toUpperCase(Locale.ROOT));
        }
    }

    public void onSuccessCashback(CashbackModel model) {


        if (model.getCode() == 200 && model.getData().getData().getItems().size() > 0) {

            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.totalCashback.setText(df.format(model.getData().getData().getCashback().getRemain()) + "sum");
            // binding.totalCashback.setText(String.valueOf(model.getData().getData().getCashback().getTotal()));
            adapter.setItems(model.getData().getData().getItems());
        }
    }

    public void onFailCashback(String model) {

    }

    public void onSuccessDiscount(DiscountModel model) {


        if (model.getCode() == 200) {

            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.totalDiscount.setText(model.getData().getPercent() + "%");
//            binding.totalCashback.setText(df.format(model.getData().getData().getCashback().getTotal()) + "sum");
//            // binding.totalCashback.setText(String.valueOf(model.getData().getData().getCashback().getTotal()));
//            adapter.setItems(model.getData().getData().getItems());
        }
    }

    public void onFailDiscount(String model) {

    }
}
