package com.twulfz.ecualert.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {
    private FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void createUserDocument(UserModel user, final CreateUserCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", user.getNombre());
        userData.put("foto_url", user.getFoto_url());
        userData.put("correo", user.getCorreo());
        userData.put("uid", user.getUid());

        db.collection("usuarios")
                .add(userData)
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

    public void getUserData(String uid, final UserDataCallback callback) {
        db.collection("usuarios")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            UserModel user = queryDocumentSnapshots.getDocuments().get(0).toObject(UserModel.class);
                            callback.onSuccess(user);
                        } else {
                            callback.onFailure(new Exception("No se encontraron datos de usuario."));
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

    public interface CreateUserCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface UserDataCallback {
        void onSuccess(UserModel user);
        void onFailure(Exception e);
    }
}
