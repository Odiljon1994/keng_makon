package com.toplevel.kengmakon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityMainBinding;
import com.toplevel.kengmakon.ui.fragments.HomeFragment;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    private HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Utils.setAppLocale(this, preferencesUtil.getLANGUAGE());
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, homeFragment).commit();
        changeState(1);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    changeState(1);
                    break;
                case R.id.wishlist:
                    Toast.makeText(this, "wishlist", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.chat:
                    Toast.makeText(this, "chat", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.profile:
                    Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
        binding.cashbackBtn.setOnClickListener(view -> {
            Toast.makeText(this, "Cashback", Toast.LENGTH_SHORT).show();
        });
    }

    public void changeState(int position) {
        if (position == 1) {
            getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
        }
    }
}