package com.twulfz.ecualert.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.FirestoreManager;
import com.twulfz.ecualert.database.ReportAdapter;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportFragment extends Fragment {

    FirestoreManager firestoreManager;
    Spinner sp_order;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestoreManager = new FirestoreManager();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sp_order = view.findViewById(R.id.sp_order);

        List<String> orders = Arrays.asList("Orden ▼", "Orden ▲");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.order_selection, orders);
        adapter.setDropDownViewResource(R.layout.order_selection);
        sp_order.setAdapter(adapter);


        loadData(view);

    }

    public void loadData(View view) {
        firestoreManager.getAlerts(new FirestoreManager.AlertsCallback() {
            @Override
            public void onSuccess(ArrayList<AlertModel> alerts) {
                ArrayList<AlertModel> alertsFiltered = new ArrayList<>();
                // Filter empty reports
                for (AlertModel alert : alerts) {
                    if (alert.getFecha() != null && alert.getUbicacion() != null && alert.getUid_autor() != null) {
                        alertsFiltered.add(alert);
                    }
                }
                ReportAdapter adapter = new ReportAdapter(view.getContext(), alertsFiltered);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Error", e.getMessage());
            }
        });
    }

}