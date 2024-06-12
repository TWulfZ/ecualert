package com.twulfz.ecualert.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twulfz.ecualert.ActivityEditarContrasena;
import com.twulfz.ecualert.Activity_inicio_sesion;
import com.twulfz.ecualert.R;
import com.twulfz.ecualert.activity_editar_nombre;
import com.twulfz.ecualert.sesion.SesionManager;


public class UserFragment extends Fragment {

    Button btnUsername, btnEditPass, btnLogout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SesionManager sesionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnEditPass = view.findViewById(R.id.btn_edit_pass);
        btnUsername = view.findViewById(R.id.btn_user_name);
        btnLogout = view.findViewById(R.id.btn_logout);

        Context context = getActivity().getApplicationContext();
        sesionManager = new SesionManager(context.getSharedPreferences(SesionManager.SESSION, Context.MODE_PRIVATE));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Set username
        if (user != null) {
            btnUsername.setText(user.getDisplayName());
            Intent intent1 = new Intent(getContext(), activity_editar_nombre.class);
            startActivity(intent1);
        }

        btnEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityEditarContrasena.class);
                startActivity(intent);

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sesionManager.logoutUser();
                mAuth.signOut();
                Intent intent = new Intent(getContext(), Activity_inicio_sesion.class);
                startActivity(intent);
            }
        });
    }
}