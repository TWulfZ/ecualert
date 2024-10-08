package com.twulfz.ecualert.database.models;

public class UserModel {
    private String nombre;
    private String foto_url;
    private String correo;
    private String uid;


    public UserModel() {
    }

    public UserModel(String nombre,  String foto_url,  String correo, String uid) {
        this.nombre = nombre;
        this.foto_url = foto_url;
        this.correo = correo;
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUid() {
        return uid;
    }
}
