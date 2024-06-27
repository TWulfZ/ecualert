package com.twulfz.ecualert;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.twulfz.ecualert.database.AuthManager;
import com.twulfz.ecualert.utils.SesionManager;

import es.dmoral.toasty.Toasty;

public class ActivityEditarContrasena extends AppCompatActivity {

    Button btnEdit, btnCancel;
    EditText txtPassword, txtNewPassword, txtConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnEdit = findViewById(R.id.btn_edit);
        btnCancel = findViewById(R.id.btn_cancel);
        txtPassword = findViewById(R.id.txt_password);
        txtNewPassword = findViewById(R.id.txt_new_password);
        txtConfirmNewPassword = findViewById(R.id.txt_new_password_confirm);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = txtPassword.getText().toString();
                String newPassword = txtNewPassword.getText().toString();
                String confirmPassword = txtConfirmNewPassword.getText().toString();

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    //Toast.makeText(ActivityEditarContrasena.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    Toasty.info(ActivityEditarContrasena.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    //Toast.makeText(ActivityEditarContrasena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    Toasty.error(ActivityEditarContrasena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                showAlertDialog(oldPassword, newPassword, confirmPassword);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showAlertDialog(String oldPassword, String newPassword, String confirmPassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de cambiar tu contraseña?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editPassword(oldPassword, newPassword, confirmPassword);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // Para que el diálogo no sea cancelable
        builder.setCancelable(false);

        // mostrar el alerta
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(R.color.fontAlt_color);

        dialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void editPassword(String oldPassword, String newPassword, String confirmPassword) {
        AuthManager authManager = new AuthManager();
        SesionManager sesionManager = new SesionManager(getSharedPreferences(SesionManager.SESSION, MODE_PRIVATE));
        String email = sesionManager.getEmail();

        authManager.loginUser(email, oldPassword, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser authUser) {
                authUser.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //Toast.makeText(ActivityEditarContrasena.this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                                Toasty.success(ActivityEditarContrasena.this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                                sesionManager.editPassword(newPassword);
                                Intent intent = new Intent(ActivityEditarContrasena.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Toast.makeText(ActivityEditarContrasena.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                Toasty.error(ActivityEditarContrasena.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Quitar mensaje de error concatenado en Despliegue
                //Toast.makeText(ActivityEditarContrasena.this, "La contraseña actual no coincide " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Toasty.error(ActivityEditarContrasena.this, "La contraseña actual no coincide " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}