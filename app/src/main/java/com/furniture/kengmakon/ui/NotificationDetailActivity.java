package com.furniture.kengmakon.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.furniture.kengmakon.MyApp;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityNotificationDetailBinding;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class NotificationDetailActivity extends AppCompatActivity {

    private ActivityNotificationDetailBinding binding;

    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_detail);

        binding.backBtn.setOnClickListener(view -> finish());

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");



        try {

            binding.name.setText(parseLang(title));

            binding.description.setText(description);

            String parsedDescription = parseLang(description);

            String datePairs[] = date.split("T");
            binding.date.setText(datePairs[0]);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.description.setText(Html.fromHtml(parsedDescription, Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.description.setText(Html.fromHtml(parsedDescription));
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String parseLang(String data) {
        try {
            String parsedData = "";

            Gson parser = new Gson();
            PushNotificationModel pushNotificationTitleModel = parser.fromJson(data, PushNotificationModel.class);

            if (preferencesUtil.getLANGUAGE().equals("uz")) {
                parsedData = pushNotificationTitleModel.getUz();
            } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
                parsedData = pushNotificationTitleModel.getRu();
            } else if (preferencesUtil.getLANGUAGE().equals("en")) {
                parsedData = pushNotificationTitleModel.getEn();
            }

            return parsedData;
        } catch (Exception e) {
            return "";
        }

    }
}
