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
import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.utils.LocationManager;

public class HomeFragment extends Fragment{

    ImageButton btnAlert, btnReports, btnMap;
    BottomNavigationView bottomNavigationView;
    FirestoreManager firestoreManager;
    AuthManager authManager;

    // Location
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    Boolean locationPermissionGranted = false;
    Double latitude, longitude;

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationManager = new LocationManager(fusedLocationProviderClient);
        firestoreManager = new FirestoreManager();
        authManager = new AuthManager();
        requestLocation();

        btnAlert.setOnClickListener(new View.OnClickListener() {
            // TODO: Send alert with the current location
            @Override
            public void onClick(View v) {
                if (!locationPermissionGranted) {
                    requestLocation();
                } else {
                    // Send alert to firestore
                    GeoPoint geoPoint = new GeoPoint(latitude, longitude);
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
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, mapFragment).commit();

                bottomNavigationView.setSelectedItemId(R.id.mapBN);
            }
        });

    }

    public void requestLocation() {
        locationManager.getLastLocation(getActivity(), new LocationManager.LocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                btnAlert.setImageResource(R.drawable.logo_white);
                btnAlert.setAlpha(1.0f);
                locationPermissionGranted = true;
            }

            @Override
            public void onLocationNotAvailable() {
                btnAlert.setImageResource(R.drawable.logo_white_blocked);
                btnAlert.setAlpha(0.6f);
                locationPermissionGranted = false;
                Toast.makeText(getContext(), "Error al obtener ubicación", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationPermissionDenied() {
                btnAlert.setAlpha(0.6f);
                btnAlert.setImageResource(R.drawable.logo_white_blocked);
                locationPermissionGranted = false;
                Toast.makeText(getContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}