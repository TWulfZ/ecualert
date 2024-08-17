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

public class Activity_olvidaste_contrasena extends AppCompatActivity {

    private EditText correoEditText;
    private Button btnRestablecer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_olvidaste_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        correoEditText = findViewById(R.id.correo);
        btnRestablecer = findViewById(R.id.btnRestablecer);

        btnRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoEditText.getText().toString().trim();
                if (correo.isEmpty()) {
                    Toast.makeText(Activity_olvidaste_contrasena.this, "Por favor ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(correo)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Activity_olvidaste_contrasena.this, "Correo de restablecimiento enviado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Activity_olvidaste_contrasena.this, Activity_inicio_sesion.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Activity_olvidaste_contrasena.this, "Error al enviar correo de restablecimiento: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
