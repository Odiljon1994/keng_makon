package com.toplevel.kengmakon.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.geo_gray_icon);

        String latLng = getIntent().getStringExtra("langtitude_longtitude");
        String[] locationPairs = latLng.split(",");


        LatLng storeLocation = new LatLng(Double.parseDouble(locationPairs[0]), Double.parseDouble(locationPairs[1]));
        mMap.addMarker(new MarkerOptions().position(storeLocation)).setTitle("Keng Makon");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));

        mMap.setMinZoomPreference(17);
        mMap.setMaxZoomPreference(25);
    }
}
