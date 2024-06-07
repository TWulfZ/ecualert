package com.twulfz.ecualert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.database.UserModel;

public class Form_Registro extends AppCompatActivity {

    Button btnLoggin;
    EditText txtUsername, txtEmail, txtPassword;
    FirebaseFirestore db;

    private AuthManager authManager;
    private FirestoreManager firestoreManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLoggin = findViewById(R.id.btnLoggin);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        authManager = new AuthManager();
        firestoreManager = new FirestoreManager();

        btnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo, password, username;
                username = txtUsername.getText().toString();
                correo = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                // Auth register
                RegisterUser(username, correo, password, "");
            }
        });
    }


    // Implements image uploading
    private void RegisterUser(String username, String correo, String password, String urlPicture) {
        authManager.registerUser(correo, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                authManager.updateUserProfile(user, username, urlPicture);

                Toast.makeText(Form_Registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                // Enviar a la pantalla de inicio
                Intent intent = new Intent(Form_Registro.this, Activity_inicio_sesion.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Error", e.getMessage());
            }
        });
    }

}