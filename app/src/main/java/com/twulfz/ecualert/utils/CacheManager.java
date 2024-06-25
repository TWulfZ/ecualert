package com.twulfz.ecualert.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twulfz.ecualert.database.models.AlertModel;
import com.twulfz.ecualert.database.models.UserModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CacheManager {
    // needs implementation("com.google.code.gson:gson:2.8.9"); in build.gradle (Module: app)
    private static final String PREF_NAME = "EcuAlertCache";
    private static final String KEY_ALERTS = "cached_alerts";
    private static final String KEY_USERS = "cached_users";
    private static final String KEY_LAST_FETCH_TIME = "last_fetch_time";
    private static final long CACHE_DURATION = 5 * 60 * 1000;

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public CacheManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // TODO: Delete test Logs
    public void saveAlerts(ArrayList<AlertModel> alerts) {
        Log.d("CacheManager", "Saving: alerts: " + alerts);
        String json = gson.toJson(alerts);
        sharedPreferences.edit().putString(KEY_ALERTS, json).apply();
    }

    public ArrayList<AlertModel> getAlerts() {
        Log.d("CacheManager", "Getting: alerts");
        String json = sharedPreferences.getString(KEY_ALERTS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<AlertModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUsers(ArrayList<UserModel> users) {
        Log.d("CacheManager", "Saving: users: " + users);
        String json = gson.toJson(users);
        sharedPreferences.edit().putString(KEY_USERS, json).apply();
    }

    public ArrayList<UserModel> getUsers() {
        Log.d("CacheManager", "Getting: users");
        String json = sharedPreferences.getString(KEY_USERS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<UserModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void updateLastFetchTime() {
        Log.d("CacheManager", "Updating last fetch time");
        sharedPreferences.edit().putLong(KEY_LAST_FETCH_TIME, System.currentTimeMillis()).apply();
    }

    public boolean isCacheValid() {
        Log.d("CacheManager", "Checking if cache is valid");
        long lastFetchTime = sharedPreferences.getLong(KEY_LAST_FETCH_TIME, 0);
        return System.currentTimeMillis() - lastFetchTime < CACHE_DURATION;
    }

    public void clearCache() {
        sharedPreferences.edit().clear().apply();
    }

}