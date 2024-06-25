package com.twulfz.ecualert.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreManager {
    private FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    // USERS
    public void createUserDocument(UserModel user, final CreateDocumentCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", user.getNombre());
        userData.put("foto_url", user.getFoto_url());
        userData.put("correo", user.getCorreo());
        userData.put("uid", user.getUid());

        db.collection("usuarios")
                .document(user.getUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });

    }

    public void getUserData(String uid, final UserDataCallback callback) {
        db.collection("usuarios")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            UserModel user = task.getResult().getDocuments().get(0).toObject(UserModel.class);
                            callback.onSuccess(user);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void updateUserData(String uid, UserModel user, final UserDataCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", user.getNombre());
        userData.put("foto_url", user.getFoto_url());
        userData.put("correo", user.getCorreo());
        userData.put("uid", user.getUid());

        db.collection("usuarios")
                .document(uid)
                .update(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }

    public void getUsers(final UsersCallback callback) {
        db.collection("usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<UserModel> users = task.getResult().toObjects(UserModel.class);
                            callback.onSuccess(users);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    // ALERTS
    public void createAlertDocument(AlertModel alert, final CreateDocumentCallback callback) {
        Map<String, Object> alertData = new HashMap<>();
        alertData.put("fecha", alert.getFecha());
        alertData.put("ubicacion", alert.getUbicacion());
        alertData.put("uid_autor", alert.getUid_autor());

        db.collection("alertas")
                .add(alertData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }

    public void getAlerts(final AlertsCallback callback) {
        db.collection("alertas")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<AlertModel> alertList = task.getResult().toObjects(AlertModel.class);
                            ArrayList<AlertModel> alerts = new ArrayList<>(alertList);
                            callback.onSuccess(alerts);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void getAlertByUid(String uid, final AlertCallback callback) {
        db.collection("alertas")
                .whereEqualTo("uid_autor", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AlertModel alert = task.getResult().getDocuments().get(0).toObject(AlertModel.class);
                            callback.onSuccess(alert);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public interface CreateDocumentCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface UserDataCallback {
        void onSuccess(UserModel user);
        void onFailure(Exception e);
    }
    public interface UsersCallback {
        void onSuccess(List<UserModel> users);
        void onFailure(Exception e);
    }
    public interface AlertCallback {
        void onSuccess(AlertModel alert);
        void onFailure(Exception e);
    }
    public interface AlertsCallback {
        void onSuccess(ArrayList<AlertModel> alerts);
        void onFailure(Exception e);
    }
}
