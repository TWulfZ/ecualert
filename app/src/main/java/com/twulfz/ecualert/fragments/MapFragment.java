package com.twulfz.ecualert.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twulfz.ecualert.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    EditText txtLatitutd, txtAltitud;
    GoogleMap mMap;

    // TODO: Locations Render from Reports in Firestore database (Only render reports from 24 hours ago)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //txtLatitutd = view.findViewById(R.id.txtLatitutd);
        //txtAltitud = view.findViewById(R.id.txtAltitud);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng Machachi = new LatLng(-0.5193919,-78.5726457);
        mMap.addMarker(new MarkerOptions().position(Machachi).title("Las Orquideas"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Machachi));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Machachi,18f));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        //txtLatitutd.setText("" + latLng.latitude);
        //txtAltitud.setText("" + latLng.longitude);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        //txtLatitutd.setText("" + latLng.latitude);
        //txtAltitud.setText("" + latLng.longitude);
    }
}