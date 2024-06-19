package com.twulfz.ecualert.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationManager {
    public static final int FINE_PERMISSION_CODE = 1;

    Boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;


    public LocationManager(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public void getLastLocation(Activity context, LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            callback.onLocationPermissionDenied();
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    locationPermissionGranted = true;
                    callback.onLocationReceived(location);
                } else {
                    Log.d("AppPermissions", "Location not available");
                    callback.onLocationNotAvailable();
                }
            }
        });
    }



    public interface LocationCallback {
        void onLocationReceived(Location location);
        void onLocationNotAvailable();
        void onLocationPermissionDenied();
    }
}
