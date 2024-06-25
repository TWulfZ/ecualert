package com.twulfz.ecualert.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twulfz.ecualert.MainActivity;
import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class MapFragment extends Fragment implements OnMapReadyCallback, MainActivity.DataUpdaterListener {

    GoogleMap mMap;
    Location currentLocation;
    private Location selectedLocation;
    private ArrayList<AlertModel> cachedAlerts;
    private ArrayList<UserModel> cachedUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            currentLocation = mainActivity.getCurrentLocation();
            cachedAlerts = mainActivity.getCachedAlerts();
            cachedUsers = mainActivity.getCachedUsers();
            updateMap(mainActivity.getCachedAlerts(), mainActivity.getCachedUsers(), currentLocation);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        updateMap(cachedAlerts, cachedUsers, currentLocation);
    }

    public void onDataUpdated(ArrayList<AlertModel> alerts, ArrayList<UserModel> users) {
        updateMap(alerts, users, currentLocation);
    }

    private void updateMap(ArrayList<AlertModel> alerts, ArrayList<UserModel> users, Location currentLocation) {
        if (mMap == null) return;

        mMap.clear();

        // Set default location
        if(currentLocation == null) {
            currentLocation = new Location("MyLocation");
            currentLocation.setLatitude(-0.5193919);
            currentLocation.setLongitude(-78.5726457);
        }

        for (AlertModel alert : alerts) {
            if (alert.getFecha() != null && alert.getUbicacion() != null && alert.getUid_autor() != null) {
                LatLng latLng = new LatLng(alert.getUbicacion().getLatitude(), alert.getUbicacion().getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(labelAlert(alert))
                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_alert_location)));
            }
        }

        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Mi ubicación")
                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_person_location)));

        if (selectedLocation != null) {
            LatLng selectedLatLng = new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 16.5f));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.5f));
        }
    }

    private String labelAlert(AlertModel alert) {
        String username = "Usuario desconocido";

        // Find the user who made the report
        Optional<UserModel> user = cachedUsers.stream().filter(u -> u.getUid().equals(alert.getUid_autor())).findFirst();
        if (user.isPresent()) {
            username = user.get().getNombre();
        }

        // Format date to Spanish
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a 'el' d 'de' MMMM", new Locale("es", "ES"));
        String date = sdf.format(alert.getFecha().toDate());
        date = date.replace("a. m.", "AM").replace("p. m.", "PM");

        return username + " a las " + date;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}