package com.twulfz.ecualert.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.twulfz.ecualert.R;

public class HomeFragment extends Fragment {

    ImageButton btnAlert, btnReport, btnMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAlert = view.findViewById(R.id.btn_alert);
        btnReport = view.findViewById(R.id.btn_report);
        btnMap = view.findViewById(R.id.btn_map);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportFragment reportFragment = new ReportFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, reportFragment).commit();
            }
        });
    }
}