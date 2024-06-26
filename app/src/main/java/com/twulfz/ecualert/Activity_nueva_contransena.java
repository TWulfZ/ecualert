package com.twulfz.ecualert;

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

public class Activity_nueva_contransena extends AppCompatActivity {

    private EditText nuevaContrasenaEditText;
    private EditText confirmarContrasenaEditText;
    private Button modificarButton;

    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        nuevaContrasenaEditText = findViewById(R.id.nueva_contraseña);
        confirmarContrasenaEditText = findViewById(R.id.confirmar_contraseña);
        modificarButton = findViewById(R.id.btnmodificar);

        modificarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaContrasena = nuevaContrasenaEditText.getText().toString();
                String confirmarContrasena = confirmarContrasenaEditText.getText().toString();

                if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    Toast.makeText(Activity_nueva_contransena.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    Toast.makeText(Activity_nueva_contransena.this, "Las contraseñas no coinciden...", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.updatePassword(nuevaContrasena)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Activity_nueva_contransena.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Activity_nueva_contransena.this, Activity_inicio_sesion.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Activity_nueva_contransena.this, "Error, no se actualizó la contraseña" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(Activity_nueva_contransena.this, "El usuario no existe, por favor cree una cuenta en el apartado de registro", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity_nueva_contransena.this, Activity_inicio_sesion.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}