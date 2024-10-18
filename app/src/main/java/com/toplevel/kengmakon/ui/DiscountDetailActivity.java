package com.toplevel.kengmakon.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityDiscountDetailBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.DiscountModel;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

public class DiscountDetailActivity extends AppCompatActivity {

    private int prevPercent = 10;
    private int nextPercent = 20;
    private double prevAmount = 1000000;
    private double nextAmount = 2000000;
    private double amount = 1500000;
    private int percent = 10;
    @Inject
    PreferencesUtil preferencesUtil;
    private UserVM userVM;
    @Inject
    ViewModelFactory viewModelFactory;

    private ActivityDiscountDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discount_detail);
        userVM = ViewModelProviders.of(this, viewModelFactory).get(UserVM.class);
        userVM.onSuccessDiscountLiveData().observe(this, this::onSuccessDiscount);
        userVM.onFailDiscountLiveData().observe(this, this::onFailDiscount);

        userVM.getDiscount(preferencesUtil.getTOKEN());

        binding.backBtn.setOnClickListener(view -> finish());

        if (!TextUtils.isEmpty(preferencesUtil.getName())) {
            binding.userName.setText(preferencesUtil.getName().toUpperCase(Locale.ROOT));
        }

        binding.progressbar.setMax((int) nextAmount);
        binding.progressbar.setProgress((int) amount);

        binding.totalDiscount.setText(percent + "%");

        binding.amount.setText(amount + "so'm");


    }

    public void onSuccessDiscount(DiscountModel model) {

        binding.progressbar.setIndeterminate(false);
        if (model.getCode() == 200) {

            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.totalDiscount.setText(model.getData().getPercent() + "%");

            binding.prevPercent.setText(model.getData().getPrev_percent() + " %");

            binding.nextPercent.setText(model.getData().getNext_percent() + " %");

            if (model.getData().getNext_percent() == -1 || model.getData().getNext_amount() == -1) {
                binding.nextPercent.setVisibility(View.INVISIBLE);
                binding.nextAmount.setVisibility(View.INVISIBLE);
                binding.progressbar.setMax(100);
                binding.progressbar.setProgress(30);
            } else {
                binding.progressbar.setMax((int) (model.getData().getNext_amount() - model.getData().getPrev_amount()));
                binding.progressbar.setProgress((int) (model.getData().getAmount() - model.getData().getPrev_amount()));
            }

            if (model.getData().getPrev_amount() < 1000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000) + " ming");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000) + " тыс");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000) + " thousand");
                }
            } else if (model.getData().getPrev_amount() >= 1000000 && model.getData().getPrev_amount() < 1000000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000000) + " mln");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000000) + " млн");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000000) + " mln");
                }
            } else if (model.getData().getPrev_amount() >= 1000000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000000000) + " mlrd");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000000000) + " млрд");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.prevAmount.setText(df.format(model.getData().getPrev_amount() / 1000000000) + " billion");
                }
            }





            if (model.getData().getNext_amount() < 1000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000) + " ming");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000) + " тыс");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000) + " thousand");
                }
            } else if (model.getData().getNext_amount() >= 1000000 && model.getData().getNext_amount() < 1000000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000000) + " mln");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000000) + " млн");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000000) + " mln");
                }
            } else if (model.getData().getNext_amount() >= 1000000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000000000) + " mlrd");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000000000) + " млрд");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.nextAmount.setText(df.format(model.getData().getNext_amount() / 1000000000) + " billion");
                }
            }






            if (model.getData().getAmount() < 1000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000) + " ming");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000) + " тыс");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000) + " thousand");
                }
            } else if (model.getData().getAmount() >= 1000000 && model.getData().getAmount() < 1000000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000000) + " mln");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000000) + " млн");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000000) + " mln");
                }
            } else if (model.getData().getAmount() >= 1000000000) {
                if (preferencesUtil.getLANGUAGE().equals("uz")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000000000) + " mlrd");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000000000) + " млрд");
                } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                    binding.amount.setText(df.format(model.getData().getAmount() / 1000000000) + " billion");
                }
            }




            binding.totalDiscount.setText(model.getData().getPercent() + " %");



        }
    }

    public void onFailDiscount(String model) {

    }
}
