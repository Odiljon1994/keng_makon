package com.toplevel.kengmakon.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityChooseLanguageBinding;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import java.util.Locale;

import javax.inject.Inject;

public class ChooseLanguageActivity extends AppCompatActivity {

    ActivityChooseLanguageBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    private String language = "uz";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);

        if (preferencesUtil.getLANGUAGE().equals("")) {
            setAppLocale("uz");
        } else {
            setAppLocale(preferencesUtil.getLANGUAGE());
            language = preferencesUtil.getLANGUAGE();
            if (preferencesUtil.getLANGUAGE().equals("ru")) {
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.enDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                binding.enDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
            }
        }

        binding.nextbtn.setOnClickListener(view -> {
            preferencesUtil.saveLanguage(language);
            startActivity(new Intent(ChooseLanguageActivity.this, GetStartedActivity.class));
        });

        binding.uzLayout.setOnClickListener(view -> {
            language = "uz";
            if (!preferencesUtil.getLANGUAGE().equals("uz")) {
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.enDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));

                preferencesUtil.saveLanguage("uz");
                setAppLocale("uz");
                preferencesUtil.saveLanguage("uz");
                finish();
                startActivity(new Intent(this, ChooseLanguageActivity.class));
            } else {
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.enDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));

            }
        });

        binding.ruLayout.setOnClickListener(view -> {
            language = "ru";
            if (!preferencesUtil.getLANGUAGE().equals("ru")) {
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.enDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));

                preferencesUtil.saveLanguage("ru");
                setAppLocale("ru");
                preferencesUtil.saveLanguage("ru");
                finish();
                startActivity(new Intent(this, ChooseLanguageActivity.class));
            } else {
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.enDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));

            }
        });

        binding.enLayout.setOnClickListener(view -> {
            language = "en";
            if (!preferencesUtil.getLANGUAGE().equals("en")) {
                binding.enDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));

                preferencesUtil.saveLanguage("en");
                setAppLocale("en");
                preferencesUtil.saveLanguage("en");
                finish();
                startActivity(new Intent(this, ChooseLanguageActivity.class));
            } else {
                binding.enDot.setImageDrawable(getDrawable(R.drawable.selected_lng));
                binding.ruDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));
                binding.uzDot.setImageDrawable(getDrawable(R.drawable.unselected_lng));

            }
        });
    }

    private void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
