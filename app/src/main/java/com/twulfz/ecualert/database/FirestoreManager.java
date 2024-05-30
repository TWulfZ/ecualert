package com.twulfz.ecualert.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public interface CreateUserCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
