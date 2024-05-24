package com.twulfz.ecualert;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Adcuenta extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private View frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adcuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.boton_nave);
        frameLayout = findViewById(R.id.frame_menu);
     bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             int itemid = item.getItemId();
             if (itemid == R.id.navi_inicio) {
                 loadFragment(new InicioFragment(), false);
             } else if (itemid == R.id.navi_cuenta) {
                 loadFragment(new CuentaFragment(), false);
             }

             return true;
         }
     });
        loadFragment(new InicioFragment(), true);


    }
    private void loadFragment (Fragment fragment, boolean inicioApp){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (inicioApp){
            fragmentTransaction.add(R.id.frame_menu, fragment);

        }else {
            fragmentTransaction.replace(R.id.frame_menu, fragment);

        }
        fragmentTransaction.commit();
    }
}