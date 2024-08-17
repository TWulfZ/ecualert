package com.twulfz.ecualert.database.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class AlertModel {

    private Timestamp fecha;
    private GeoPoint ubicacion;
    private String uid_autor;

    public AlertModel() {
    }
    public AlertModel(Timestamp fecha, GeoPoint ubicacion, String uid_autor) {
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.uid_autor = uid_autor;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public String getUid_autor() {
        return uid_autor;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }
}
