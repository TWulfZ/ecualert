package com.twulfz.ecualert.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.twulfz.ecualert.MainActivity;
import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.ReportAdapter;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportFragment extends Fragment implements MainActivity.DataUpdaterListener {

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

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sp_order = view.findViewById(R.id.sp_order);

        List<String> orders = Arrays.asList("Orden ▼", "Orden ▲");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.order_selection, orders);
        adapter.setDropDownViewResource(R.layout.order_selection);
        sp_order.setAdapter(adapter);

        // Cache
        if (getActivity() instanceof MainActivity) {
            ArrayList<AlertModel> cachedAlerts = ((MainActivity) getActivity()).getCachedAlerts();
            ArrayList<UserModel> cachedUsers = ((MainActivity) getActivity()).getCachedUsers();
            if(!cachedAlerts.isEmpty()) {
                updateUI(cachedAlerts, cachedUsers);
            }
        }

    }


    @Override
    public void onDataUpdated(ArrayList<AlertModel> alerts, ArrayList<UserModel> users) {
        updateUI(alerts, users);
    }

    private void updateUI(ArrayList<AlertModel> alerts, ArrayList<UserModel> users) {
        ArrayList<AlertModel> alertsFiltered = new ArrayList<>();
        for (AlertModel alert : alerts) {
            if (alert.getFecha() != null && alert.getUbicacion() != null && alert.getUid_autor() != null) {
                alertsFiltered.add(alert);
            }
        }

        ReportAdapter adapter = new ReportAdapter(getContext(), alertsFiltered, users);
        recyclerView.setAdapter(adapter);
    }

}