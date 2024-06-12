package com.twulfz.ecualert;
import static android.graphics.Color.argb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_editar_nombre extends AppCompatActivity {

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

        // Mostrar el AlertDialog cuando se crea la actividad
        mostrarAlertDialog();
    }


    private void mostrarAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de continuar?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes poner el código que deseas ejecutar si el usuario está seguro
                        // Por ejemplo, puedes llamar a un método que realice la acción que querías realizar
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Si el usuario no está seguro, puedes cerrar la actividad o realizar otra acción
                        finish(); // Cerrar la actividad actual
                    }
                });

        // Configurar que el diálogo no sea cancelable
        builder.setCancelable(false);

        // Crear y mostrar el AlertDialog
        AlertDialog dialog = builder.create();

        // Establecer el fondo semitransparente para el diálogo
        dialog.getWindow().setBackgroundDrawableResource(R.color.font_color);

        dialog.show();
        // Ajustar el tamaño del diálogo para ocupar toda la pantalla
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
    }
}
