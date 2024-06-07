package com.twulfz.ecualert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class Activity_inicio_sesion extends AppCompatActivity {

    Button btnLoggin;
    Button btnRegister;
    Button btnRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLoggin = findViewById(R.id.btnLoggin);

        btnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send to main activity
                Intent intent = new Intent(Activity_inicio_sesion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister = findViewById(R.id.btnRegister);  // Usar btnRegister, no btnrRgister
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_inicio_sesion.this, Form_Registro.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnRecover = findViewById(R.id.btnRecover);
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send to main activity
                Intent intent = new Intent(Activity_inicio_sesion.this, Activity_olvidaste_contrasena.class);
                startActivity(intent);
                finish();
            }
        });


    }
}