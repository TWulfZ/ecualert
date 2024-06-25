package com.twulfz.ecualert.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.twulfz.ecualert.MainActivity;
import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.utils.LocationManager;

public class HomeFragment extends Fragment implements MainActivity.LocationUpdateListener {

    ImageButton btnAlert, btnReports, btnMap;
    BottomNavigationView bottomNavigationView;
    FirestoreManager firestoreManager;
    AuthManager authManager;

    // Location
    private Location currentLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        btnAlert = view.findViewById(R.id.btn_alert);
        btnReports = view.findViewById(R.id.btn_reports);
        btnMap = view.findViewById(R.id.btn_map);

        // Location
        firestoreManager = new FirestoreManager();
        authManager = new AuthManager();

        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null) {
                    sendAlert();
                } else {
                    Toast.makeText(getContext(), "Ubicación no disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportFragment reportFragment = new ReportFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, reportFragment).commit();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFragment mapFragment = new MapFragment();
                //mapFragment.clearSelectedAlert();
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, mapFragment).commit();

                bottomNavigationView.setSelectedItemId(R.id.mapBN);
            }
        });

    }

    private void sendAlert() {
        GeoPoint geoPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        AlertModel alert = new AlertModel(Timestamp.now(), geoPoint, authManager.getCurrentUser().getUid());
        firestoreManager.createAlertDocument(alert, new FirestoreManager.CreateDocumentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Alerta enviada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Error al enviar alerta", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", e.getMessage());
            }
        });
    }

    @Override
    public void onLocationUpdated(Location location) {
        currentLocation = location;
        updateAlertButtonState();
    }

    private void updateAlertButtonState() {
        if (currentLocation != null) {
            btnAlert.setImageResource(R.drawable.logo_white);
            btnAlert.setAlpha(1.0f);
        } else {
            btnAlert.setImageResource(R.drawable.logo_white_blocked);
            btnAlert.setAlpha(0.6f);
        }
    }

}