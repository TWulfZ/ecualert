package com.twulfz.ecualert;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_nueva_contransena extends AppCompatActivity {

    private EditText nuevaContrasena;
    private EditText confirmarContrasena;
    private Button btnModificar;
    private FirebaseAuth mAuth;

    private static final String TAG = "PasswordUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_contransena);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nuevaContrasena = findViewById(R.id.nueva_contraseña);
        confirmarContrasena = findViewById(R.id.confirmar_contraseña);
        btnModificar = findViewById(R.id.btnmodificar);
        mAuth = FirebaseAuth.getInstance();

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarContrasena();
            }
        });
    }

    // En el método cambiarContrasena()

    private void cambiarContrasena() {
        String nueva = nuevaContrasena.getText().toString().trim();
        String confirmar = confirmarContrasena.getText().toString().trim();

        // Validar que las contraseñas no estén vacías y coincidan
        if (TextUtils.isEmpty(nueva) || TextUtils.isEmpty(confirmar)) {
            Toast.makeText(Activity_nueva_contransena.this, "Ambos campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nueva.equals(confirmar)) {
            Toast.makeText(Activity_nueva_contransena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el usuario actualmente autenticado
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Actualizar la contraseña del usuario en Firebase
            user.updatePassword(nueva).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Contraseña actualizada con éxito
                    Toast.makeText(Activity_nueva_contransena.this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                    // Redirigir al usuario a la pantalla de inicio de sesión
                    Intent intent = new Intent(Activity_nueva_contransena.this, Activity_inicio_sesion.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error al actualizar la contraseña
                    Toast.makeText(Activity_nueva_contransena.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // El usuario no está autenticado
            Toast.makeText(Activity_nueva_contransena.this, "No se encontró ningún usuario actual", Toast.LENGTH_SHORT).show();
        }
    }
}