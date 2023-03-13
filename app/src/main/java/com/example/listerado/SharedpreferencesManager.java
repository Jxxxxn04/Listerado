package com.example.listerado;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedpreferencesManager {

    private final SharedPreferences sharedPreferences;

    public SharedpreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("email");
        editor.remove("imageString");
        editor.remove("id");
        editor.remove("hashed_password");
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public String getHashed_password() {
        return sharedPreferences.getString("hashed_password", "");
    }

    public String getHasimage() {
        return sharedPreferences.getString("hasImage", "");
    }

    public String getId() {
        return sharedPreferences.getString("id", "");
    }

    public String getImageToString() {
        return sharedPreferences.getString("imageToString", "");
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public void changeUsername(String newUsername) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", newUsername);
        editor.apply();
    }

    public void changeEmail(String newEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", newEmail);
        editor.apply();
    }

    public void changeHashedPassword(String newHashedPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hashed_password", newHashedPassword);
        editor.apply();
    }

    public void changeHasImage(String newHasImage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hasImage", newHasImage);
        editor.apply();
    }

    public void changeID(String newID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", newID);
        editor.apply();
    }

    public void changeImageToString(String newImageToString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageToString", newImageToString);
        editor.apply();
    }

    public void changePassword(String newPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", newPassword);
        editor.apply();
    }
}
