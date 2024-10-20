package com.furniture.kengmakon.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.furniture.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityAboutBinding;
import com.furniture.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        if (preferencesUtil.getLANGUAGE().equals("uz")) {
            binding.title.setText("Keng Makon mebel fabrikasi haqida");
            binding.image.setImageDrawable(getDrawable(R.drawable.about_img_uz));

        } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
            binding.title.setText("О фабрике мебели Keng Makon");
            binding.image.setImageDrawable(getDrawable(R.drawable.about_img_ru));
        } else if (preferencesUtil.getLANGUAGE().equals("en")) {
            binding.title.setText("About Keng Makon");
            binding.image.setImageDrawable(getDrawable(R.drawable.abount_img_en));

        }

        binding.backBtn.setOnClickListener(view -> finish());
    }
}
