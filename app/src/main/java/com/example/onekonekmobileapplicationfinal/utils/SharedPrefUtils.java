package com.example.onekonekmobileapplicationfinal.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private static final String PREFS_NAME = "OnekonekPref";
    private SharedPreferences sharedPreferences;

    // Constructor
    public SharedPrefUtils(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Save token and auth to SharedPreferences
    public void saveToSharedPreferences(String token, String auth) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("auth", auth);
        editor.apply();
    }

    // Check if the user is logged in
    public boolean isUserLoggedIn() {
        String token = sharedPreferences.getString("token", null);
        return token != null && !token.isEmpty();
    }

    // Retrieve token
    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    // Retrieve auth
    public String getAuth() {
        return sharedPreferences.getString("auth", null);
    }

    public void  setAccountId(String accountId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accountId", accountId);
        editor.apply();
    }

    public String getAccountId() {
        return  sharedPreferences.getString("accountId", null);
    }

    // Remove token and auth (for logout)
    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

