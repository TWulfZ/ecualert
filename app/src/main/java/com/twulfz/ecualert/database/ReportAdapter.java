package com.twulfz.ecualert.database;

import android.content.Context;
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

import com.twulfz.ecualert.R;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    Context context;
    ArrayList<AlertModel> arrayList;

    FirestoreManager firestoreManager;


    public ReportAdapter(Context context, ArrayList<AlertModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        firestoreManager = new FirestoreManager();
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
        AlertModel alert = arrayList.get(position);
        if(alert.getFecha() == null && alert.getUbicacion() == null && alert.getUid_autor() == null) {
            return;
        }
        String stringDate = arrayList.get(position).getFecha().toDate().toString();
        // Find the user who made the report
        firestoreManager.getUserData(arrayList.get(position).getUid_autor(), new FirestoreManager.UserDataCallback() {
            @Override
            public void onSuccess(UserModel user) {
                // Render only if the user exists
                holder.txt_username.setText(user.getNombre());
                holder.txt_time.setText(stringDate);
                holder.txt_time.setText(stringDate);
                if (user.getFoto_url() != "") {
                    Uri uri = Uri.parse(user.getFoto_url());
                    holder.img_user.setImageURI(uri);
                }
                // Format date
                // TODO: CHANGE DESIGN AND DATA STRUCTURE ( TIME AND DATE IN DIFFERENT TEXTVIEWS )

                holder.btn_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Send to map with the current location
                        //Toast.makeText(context, "Location:" + arrayList.get(position).getUbicacion().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Exception e) {
                holder.txt_username.setText("Error Usuario");
                Log.d("Error", e.getMessage());

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_time;
        ImageButton btn_location;
        ImageView img_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_time = itemView.findViewById(R.id.txt_time);
            btn_location = itemView.findViewById(R.id.btn_location);
            img_user = itemView.findViewById(R.id.img_user);
        }
    }
}

