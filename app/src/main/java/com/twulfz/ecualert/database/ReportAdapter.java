package com.twulfz.ecualert.database;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.twulfz.ecualert.MainActivity;
import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;
import com.twulfz.ecualert.fragments.MapFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    Context context;
    ArrayList<AlertModel> alertsList;
    ArrayList<UserModel> usersList;

    public ReportAdapter(Context context, ArrayList<AlertModel> alertsList, ArrayList<UserModel> usersList) {
        this.context = context;
        this.alertsList = alertsList;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card_item, parent, false);
        return new ViewHolder(view);
    }

    // Only set Data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Filter empty reports
        AlertModel alert = alertsList.get(position);
        UserModel user = findUser(alert.getUid_autor());

        if (alert.getFecha() == null && alert.getUbicacion() == null && alert.getUid_autor() == null) {
            return;
        }

        // Format date to Spanish
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", new Locale("es", "ES"));
        String time = sdf.format(alert.getFecha().toDate());
        time = time.replace("a. m.", "AM").replace("p. m.", "PM");
        sdf = new SimpleDateFormat("dd/MM", new Locale("es", "ES"));
        String date = sdf.format(alert.getFecha().toDate());

        holder.txt_time.setText(time);
        holder.txt_date.setText(date);

        // Find the user who made the report
        if (!(user == null)) {
            holder.txt_username.setText(user.getNombre());
            if (user.getFoto_url() != "") {
                Uri uri = Uri.parse(user.getFoto_url());
                holder.img_user.setImageURI(uri);
            }
        } else {
            holder.txt_username.setText("Usuario no encontrado");
            holder.txt_username.setTextSize(16f);
            holder.txt_username.setAlpha(0.5f);
            holder.txt_username.setTextColor(context.getResources().getColor(R.color.warning_color));
        }

        holder.btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send Location to maps
                // TODO: Show location on map
                MapFragment mapFragment = new MapFragment();
                MainActivity activity = (MainActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, mapFragment).commit();
                Location location = new Location("Selected Location");
                location.setLatitude(alert.getUbicacion().getLatitude());
                location.setLongitude(alert.getUbicacion().getLongitude());
                activity.setSelectedALert(alert);

                BottomNavigationView bottomNavigationView = ((MainActivity) context).findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.mapBN);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alertsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_time, txt_date;
        ImageButton btn_location;
        ImageView img_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_date = itemView.findViewById(R.id.txt_date);
            btn_location = itemView.findViewById(R.id.btn_location);
            img_user = itemView.findViewById(R.id.img_user);
        }
    }

    private UserModel findUser(String uid) {
        Optional<UserModel> foundUser = usersList.stream()
                .filter(user -> user.getUid().equals(uid))
                .findFirst();
        return foundUser.orElse(null);
    }
}

