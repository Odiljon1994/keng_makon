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
import com.toplevel.kengmakon.ui.fragments.WishlistFragment;
import com.toplevel.kengmakon.utils.PreferencesUtil;
import com.toplevel.kengmakon.utils.Utils;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    private HomeFragment homeFragment;
    private WishlistFragment wishlistFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Utils.setAppLocale(this, preferencesUtil.getLANGUAGE());
        homeFragment = new HomeFragment();
        wishlistFragment = new WishlistFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, wishlistFragment).commit();
        changeState(1);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    changeState(1);
                    break;
                case R.id.wishlist:
                    changeState(2);
                    break;
                case R.id.chat:

                    break;
                case R.id.profile:

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
            getSupportFragmentManager().beginTransaction().hide(wishlistFragment).commit();
            getSupportFragmentManager().beginTransaction().show(homeFragment).commit();

        } else if (position == 2) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            getSupportFragmentManager().beginTransaction().show(wishlistFragment).commit();
        }
    }
}