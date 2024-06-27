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
import com.twulfz.ecualert.database.models.UserModel;

import es.dmoral.toasty.Toasty;

public class Activity_registro extends AppCompatActivity {

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
                if (username.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                    //Toast.makeText(Activity_registro.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    Toasty.info(Activity_registro.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    RegisterUser(username, correo, password, "");
                }
            }
        });
    }


    // Implements image uploading
    private void RegisterUser(String username, String correo, String password, String urlPicture) {
        authManager.registerUser(correo, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                authManager.updateUserProfile(user, username, urlPicture);

                // Create user model
                UserModel userDoc = new UserModel(username, urlPicture, correo, user.getUid());
                firestoreManager.createUserDocument(userDoc, new FirestoreManager.CreateDocumentCallback() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(Activity_registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        Toasty.success(Activity_registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        // Enviar a la pantalla de inicio
                        Intent intent = new Intent(Activity_registro.this, Activity_inicio_sesion.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        //Toast.makeText(Activity_registro.this, "Error con la base de datos", Toast.LENGTH_SHORT).show();
                        Toasty.error(Activity_registro.this, "Error con la base de datos", Toast.LENGTH_SHORT).show();
                        Log.d("Error", e.getMessage());
                    }
                });

            }

            @Override
            public void onFailure(Exception e) {
                //Toast.makeText(Activity_registro.this, "El correo ya se encuentra registrado o problema con la autenticación", Toast.LENGTH_SHORT).show();
                Toasty.error(Activity_registro.this, "El correo ya se encuentra registrado o problema con la autenticación", Toast.LENGTH_SHORT).show();
                Log.d("Error", e.getMessage());
            }
        });
    }

}