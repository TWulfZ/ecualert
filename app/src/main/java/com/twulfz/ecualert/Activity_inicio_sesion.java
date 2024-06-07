package com.twulfz.ecualert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.sesion.SesionManager;

public class Activity_inicio_sesion extends AppCompatActivity {

    Button btnLoggin, btnRegister, btnRecover;

    EditText txtEmail, txtPassword;


    private AuthManager authManager;
    private FirestoreManager firestoreManager;

    // User sesion
    SesionManager sesionManager;


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

        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);

        authManager = new AuthManager();
        firestoreManager = new FirestoreManager();

        btnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Activity_inicio_sesion.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    LoginUser(email, password);
                }
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

        btnRecover = findViewById(R.id.btnRecover);
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

    private void LoginUser(String email, String password) {
        authManager.loginUser(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser authUser) {
                // Guardar sesion en SharedPreferences
                sesionManager = new SesionManager(getSharedPreferences(SesionManager.SESSION, MODE_PRIVATE));
                sesionManager.saveUser(email, password, authUser.getUid());

                // Toast: Bienvenido <username>
                String username = authUser.getDisplayName();
                Toast.makeText(Activity_inicio_sesion.this, "Bienvenido " + username, Toast.LENGTH_SHORT).show();

                // Enviar a la MainActivity
                Intent intent = new Intent(Activity_inicio_sesion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Activity_inicio_sesion.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;
    }
}