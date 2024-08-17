package com.twulfz.ecualert;
import static android.graphics.Color.argb;

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
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.database.models.UserModel;
import com.twulfz.ecualert.utils.SesionManager;

import es.dmoral.toasty.Toasty;

public class ActivityEditarNombre extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    Button btnmodificar_nombre, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_nombre);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUsername = findViewById(R.id.txt_new_name);
        txtPassword = findViewById(R.id.txt_password_actually);
        btnmodificar_nombre = findViewById(R.id.btnmodificar_nombre);
        btnCancel = findViewById(R.id.btn_cancel_name);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnmodificar_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if (nombreUsuario.isEmpty() || password.isEmpty()) {
                    //Toast.makeText(ActivityEditarContrasena.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    Toasty.info(ActivityEditarNombre.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                mostrarAlertDialog(nombreUsuario, password);
            }
        });
    }

    private void mostrarAlertDialog(String nombreUsuario, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de cambiar tu nombre?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editName(nombreUsuario, password);
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

    private void editName(String nombreUsuario, String password) {

        AuthManager authManager = new AuthManager();
        FirestoreManager firestoreManager = new FirestoreManager();
        SesionManager sesionManager = new SesionManager(getSharedPreferences(SesionManager.SESSION, MODE_PRIVATE));
        String email = sesionManager.getEmail();

        authManager.loginUser(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser authUser) {
                UserModel userUpdated = new UserModel(nombreUsuario, email, "", authUser.getUid());
                authManager.updateUserProfile(authUser, nombreUsuario, "", new AuthManager.AuthVoidCallback() {
                    @Override
                    public void onSuccess() {
                            firestoreManager.updateUserData(authUser.getUid(), userUpdated, new FirestoreManager.UserDataCallback() {
                                @Override
                                public void onSuccess(UserModel user) {
                                    //Toast.makeText(ActivityEditarContrasena.this, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    Toasty.success(ActivityEditarNombre.this, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    //Toast.makeText(ActivityEditarContrasena.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                    Toasty.error(ActivityEditarNombre.this, "Error al actualizar el nombre en la base de datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(ActivityEditarNombre.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        //Toast.makeText(ActivityEditarContrasena.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                        Toasty.error(ActivityEditarNombre.this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Quitar mensaje de error concatenado en Despliegue
                //Toast.makeText(ActivityEditarContrasena.this, "La contraseña actual no coincide " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Toasty.error(ActivityEditarNombre.this, "La contraseña actual no coincide " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
