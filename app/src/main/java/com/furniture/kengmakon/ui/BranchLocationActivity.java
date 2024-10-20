package com.furniture.kengmakon.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.furniture.kengmakon.utils.NetworkChangeListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ActivityBranchLocationBinding;

public class BranchLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityBranchLocationBinding binding;
    private GoogleMap mMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch_location);

        binding.backBtn.setOnClickListener(view -> finish());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String latLng = getIntent().getStringExtra("langtitude_longtitude");
        String[] locationPairs = latLng.split(",");

        binding.showOnMapBtn.setOnClickListener(view -> {
         //   String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(locationPairs[0]), Double.parseDouble(locationPairs[1]));
            String uri = "http://maps.google.com/maps?daddr=" + locationPairs[0] + "," + locationPairs[1];

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException ex)
            {
                try
                {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                }
                catch(ActivityNotFoundException innerEx)
                {
                    Toast.makeText(this, "Ushbu funksiyani ishlatish uchun navigator o'rnatishingizni so'raymiz!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker);

        String latLng = getIntent().getStringExtra("langtitude_longtitude");
        String[] locationPairs = latLng.split(",");


        LatLng storeLocation = new LatLng(Double.parseDouble(locationPairs[0]), Double.parseDouble(locationPairs[1]));
        mMap.addMarker(new MarkerOptions().position(storeLocation).title("Keng Makon")).setIcon(icon);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));

        mMap.setMinZoomPreference(17);
        mMap.setMaxZoomPreference(25);
    }

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
