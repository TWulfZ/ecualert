package com.twulfz.ecualert;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CuentaFragment extends Fragment {
    Button btnCuenta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        // Obtener referencia al botón
        btnCuenta = view.findViewById(R.id.btn_ccuenta);

        // Configurar el OnClickListener para el botón
        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una intención para abrir la actividad deseada
                Intent intent = new Intent(getActivity(), Menucon.class);
                startActivity(intent);
            }
        });

        return view; }
}