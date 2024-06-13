package com.twulfz.ecualert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_olvidaste_contrasena extends AppCompatActivity {

    private EditText mEditTextEmail;
    private Button mButtonresetPassword;
    private String email;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

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
        mDialog = new ProgressDialog(this);

        mEditTextEmail = findViewById(R.id.editTextEmail);
        mButtonresetPassword = findViewById(R.id.btnRestablecer);

        mButtonresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();

                if (!email.isEmpty()) {
                    mDialog.setMessage("Cargando...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                } else {
                    Toast.makeText(Activity_olvidaste_contrasena.this, "Debe ingresar email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Activity_olvidaste_contrasena.this, "Se envió un correo para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity_olvidaste_contrasena.this, Activity_nueva_contransena.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Activity_olvidaste_contrasena.this, "No se pudo enviar el correo para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }
}