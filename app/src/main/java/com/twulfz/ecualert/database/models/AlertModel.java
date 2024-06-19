package com.twulfz.ecualert.database;

import com.google.firebase.firestore.GeoPoint;

public class AlertModel {

    private String fecha;
    private GeoPoint ubicacion;
    private String uid_autor;

    public AlertModel(String fecha, GeoPoint ubicacion, String uid_autor) {
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.uid_autor = uid_autor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUid_autor() {
        return uid_autor;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }
}
