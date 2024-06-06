package com.twulfz.ecualert;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.databinding.ActivityMainBinding;
import com.twulfz.ecualert.fragments.HomeFragment;
import com.twulfz.ecualert.fragments.MapFragment;
import com.twulfz.ecualert.fragments.UserFragment;
import com.twulfz.ecualert.sesion.SesionManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    SesionManager sesionManager;
    AuthManager authManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Verifica si hay una sesion activa
        checkUserSesion();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fix height of bottomNavigation (needs: buildFeatures { viewBinding = true } in app/build.gradle)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set home as default
        bottomNavigationView.setSelectedItemId(R.id.homeBN);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new HomeFragment()).commit();

        // Set listener for bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.mapBN) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new MapFragment()).commit();
                    return true;
                } else if (itemId == R.id.homeBN) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new HomeFragment()).commit();
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
                    Toast.makeText(MainActivity.this, "Por favor, inicia sesion. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
}