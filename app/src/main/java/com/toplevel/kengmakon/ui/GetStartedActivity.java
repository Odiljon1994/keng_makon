package com.toplevel.kengmakon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityGetStartedBinding;
import com.toplevel.kengmakon.models.GetStartedModel;
import com.toplevel.kengmakon.ui.adapters.GetStartedAdapter;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetStartedActivity extends AppCompatActivity {

    ActivityGetStartedBinding binding;
    GetStartedAdapter adapter;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_started);

        Utils.setAppLocale(this, preferencesUtil.getLANGUAGE());
        List<GetStartedModel> list = new ArrayList<>();
        list.add(new GetStartedModel(getDrawable(R.drawable.perfect_furniture_img), getResources().getString(R.string.find_perfect_furniture_title), getResources().getString(R.string.find_perfect_furniture_subtitle)));
        list.add(new GetStartedModel(getDrawable(R.drawable.all_categories_img), getResources().getString(R.string.all_categories_title), getResources().getString(R.string.all_categories_subtitle)));
        list.add(new GetStartedModel(getDrawable(R.drawable.cashback_img), getResources().getString(R.string.cashback_title), getResources().getString(R.string.cashback_subtitle)));

        adapter = new GetStartedAdapter(list);
        binding.viewPager.setAdapter(adapter);
        binding.dots.setViewPager(binding.viewPager);

        binding.signIn.setOnClickListener(view -> {
            startActivity(new Intent(GetStartedActivity.this, LoginActivity.class));
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (binding.viewPager.getCurrentItem() == 2) {
                    binding.nextBtn.setText(getResources().getString(R.string.get_started));
                } else {
                    binding.nextBtn.setText(getResources().getString(R.string.next));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.skipButton.setOnClickListener(view -> {
            Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            preferencesUtil.saveGetStarted(true);
            startActivity(intent);
            finish();
        });

        binding.nextBtn.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            if (binding.nextBtn.getText().toString().equals(getResources().getString(R.string.get_started)) && binding.viewPager.getCurrentItem() == 2) {
                Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                preferencesUtil.saveGetStarted(true);
                startActivity(intent);
                finish();
            }
            if (binding.viewPager.getCurrentItem() == 2){
                binding.nextBtn.setText(getResources().getString(R.string.get_started));
            }

        });

    }
}
