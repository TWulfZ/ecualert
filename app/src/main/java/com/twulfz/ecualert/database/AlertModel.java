package com.twulfz.ecualert.database;

public class AlertModel {

    private String fecha;
    private String uid_autor;
    private String ubicacion;

    public AlertModel(String fecha, String uid_autor, String ubicacion) {
        this.fecha = fecha;
        this.uid_autor = uid_autor;
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUid_autor() {
        return uid_autor;
    }

    public String getUbicacion() {
        return ubicacion;
    }
}
