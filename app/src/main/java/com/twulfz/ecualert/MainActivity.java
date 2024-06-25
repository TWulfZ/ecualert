package com.twulfz.ecualert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;
import com.twulfz.ecualert.databinding.ActivityMainBinding;
import com.twulfz.ecualert.fragments.HomeFragment;
import com.twulfz.ecualert.fragments.MapFragment;
import com.twulfz.ecualert.fragments.UserFragment;
import com.twulfz.ecualert.utils.CacheManager;
import com.twulfz.ecualert.utils.LocationManager;
import com.twulfz.ecualert.utils.SesionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Location
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    // Cache
    private ArrayList<AlertModel> cachedAlerts;
    private ArrayList<UserModel> cachedUsers;
    private CacheManager cacheManager;


    private ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    SesionManager sesionManager;
    AuthManager authManager;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Check if user is logged in
        checkUserSesion();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = new LocationManager(fusedLocationProviderClient);

        requestLocation();

        // Cache
        cachedAlerts = new ArrayList<>();
        cachedUsers = new ArrayList<>();
        cacheManager = new CacheManager(this);

        loadDataFromCache();


        homeFragment = new HomeFragment();

        // Fix height of bottomNavigation (needs: buildFeatures { viewBinding = true } in app/build.gradle)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set home as default
        bottomNavigationView.setSelectedItemId(R.id.homeBN);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

        // Set listener for bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.mapBN) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new MapFragment()).commit();
                    return true;
                } else if (itemId == R.id.homeBN) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.userBN) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new UserFragment()).commit();
                    return true;
                }

                return false;
            }
        });
    }

    private void checkUserSesion() {
        sesionManager = new SesionManager(getSharedPreferences(SesionManager.SESSION, MODE_PRIVATE));
        authManager = new AuthManager();

        String email = sesionManager.getEmail();
        String password = sesionManager.getPassword();
        String uid = sesionManager.getUid();
        if (!email.isEmpty() && !password.isEmpty() && !uid.isEmpty()) {
            authManager.loginUser(email, password, new AuthManager.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser authUser) {
                    Toast.makeText(MainActivity.this, "Bienvenido " + authUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(MainActivity.this, "La sesion ha expirado, inicia sesion. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Activity_inicio_sesion.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            // No hay sesion en SharedPreferences
            Toast.makeText(MainActivity.this, "Por favor, inicia sesion.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Activity_inicio_sesion.class);
            startActivity(intent);
            finish();
        }
    }

    // LOCATION
    private void requestLocation() {
        locationManager.getLastLocation(this, new LocationManager.LocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                currentLocation = location;
                notifyFragmentsLocationUpdated();
            }

            @Override
            public void onLocationNotAvailable() {
                Toast.makeText(MainActivity.this, "Error al obtener ubicación", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationPermissionDenied() {
                Toast.makeText(MainActivity.this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyFragmentsLocationUpdated() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof LocationUpdateListener) {
                ((LocationUpdateListener) fragment).onLocationUpdated(currentLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationManager.FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadDataFromCache() {
        if (cacheManager.isCacheValid()) {
            cachedAlerts = cacheManager.getAlerts();
            cachedUsers = cacheManager.getUsers();
            notifyFragmentsDataUpdated();
        } else {
            loadDataFromFirestore();
        }
    }

    private void loadDataFromFirestore() {
        FirestoreManager firestoreManager = new FirestoreManager();
        firestoreManager.getAlerts(new FirestoreManager.AlertsCallback() {
            @Override
            public void onSuccess(ArrayList<AlertModel> alerts) {
                cachedAlerts = alerts;
                cacheManager.saveAlerts(alerts);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Error", "Error al traer las alertas: " + e.getMessage());
            }
        });

        firestoreManager.getUsers(new FirestoreManager.UsersCallback() {
            @Override
            public void onSuccess(List<UserModel> users) {
                cachedUsers = new ArrayList<>(users);
                cacheManager.saveUsers(cachedUsers);
                cacheManager.updateLastFetchTime();
                notifyFragmentsDataUpdated();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Error", "Error al traer los usuarios: " + e.getMessage());
            }
        });
    }

    private void notifyFragmentsDataUpdated() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flFragment);
        if (currentFragment instanceof DataUpdaterListener) {
            ((DataUpdaterListener) currentFragment).onDataUpdated(cachedAlerts, cachedUsers);
        }
    }

    public interface LocationUpdateListener {
        void onLocationUpdated(Location location);
    }

    public interface DataUpdaterListener {
        void onDataUpdated(ArrayList<AlertModel> alerts, ArrayList<UserModel> users);
    }

    // Geters
    public ArrayList<AlertModel> getCachedAlerts() {
        return new ArrayList<>(cachedAlerts);
    }

    public ArrayList<UserModel> getCachedUsers() {
        return new ArrayList<>(cachedUsers);
    }

    public Location getCurrentLocation() { return currentLocation; }
}