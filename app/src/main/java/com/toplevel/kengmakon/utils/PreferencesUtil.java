package com.toplevel.kengmakon.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String OTP = "OTP";

    private static final String LANGUAGE = "LANGUAGE";
    private static final String IS_GET_STARTED_DONE = "IS_GET_STARTED_DONE";
    private static final String TOKEN = "TOKEN";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String IS_SIGNED_IN = "IS_SIGNED_IN";
    private static final String IS_PUSH_TOKEN_DONE = "IS_PUSH_TOKEN_DONE";
    private static final String USER_ID = "USER_ID";
    private static final String IMAGE_URL = "IMAGE_URL";


    public PreferencesUtil(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }
    public String getLANGUAGE() {
        return sharedPreferences.getString(LANGUAGE, "");
    }

    public void saveLanguage(String language) {
        sharedPreferences.edit().putString(LANGUAGE, language).apply();
    }

    public String getTOKEN() {
        return sharedPreferences.getString(TOKEN, "");
    }

    public void saveTOKEN(String token) {
        sharedPreferences.edit().putString(TOKEN, token).apply();
    }

    public void saveOtp(String otp) {
        sharedPreferences.edit().putString(OTP, otp).apply();
    }
    public String getOtp() {
        return sharedPreferences.getString(OTP, "");
    }


    public void saveGetStarted(boolean isDone) {
        sharedPreferences.edit().putBoolean(IS_GET_STARTED_DONE, isDone).apply();
    }
    public boolean getIsGetStartedDone() {
        return sharedPreferences.getBoolean(IS_GET_STARTED_DONE, false);
    }

    public String getName() {
        return sharedPreferences.getString(NAME, "");
    }

    public void saveName(String name) {
        sharedPreferences.edit().putString(NAME, name).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void savePassword(String password) {
        sharedPreferences.edit().putString(PASSWORD, password).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void saveEmail(String email) {
        sharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(PHONE_NUMBER, "");
    }

    public void savePhoneNumber(String phoneNumber) {
        sharedPreferences.edit().putString(PHONE_NUMBER, phoneNumber).apply();
    }

    public void saveIsSignedIn(boolean isSignedIn) {
        sharedPreferences.edit().putBoolean(IS_SIGNED_IN, isSignedIn).apply();
    }
    public boolean getIsIsSignedIn() {
        return sharedPreferences.getBoolean(IS_SIGNED_IN, false);
    }

    public void saveIsPushTokenDone(boolean isPushTokenDone) {
        sharedPreferences.edit().putBoolean(IS_PUSH_TOKEN_DONE, isPushTokenDone).apply();
    }
    public boolean getIsPushTokenDone() {
        return sharedPreferences.getBoolean(IS_PUSH_TOKEN_DONE, false);
    }
    public int getUserId() {
        return sharedPreferences.getInt(USER_ID, -1);
    }

    public void saveUserId(int userId) {
        sharedPreferences.edit().putInt(USER_ID, userId).apply();
    }

    public String getImageUrl() {
        return sharedPreferences.getString(IMAGE_URL, "");
    }

    public void saveImageUrl(String imageUrl) {
        sharedPreferences.edit().putString(IMAGE_URL, imageUrl).apply();
    }
}
