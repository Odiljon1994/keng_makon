package com.toplevel.kengmakon.entity;

import android.content.Context;

import com.google.gson.Gson;

import com.toplevel.kengmakon.utils.PreferencesUtil;

import javax.inject.Inject;

public class User {
    private Context context;
    private Gson gson;
    private PreferencesUtil preferenceUtil;

    @Inject
    public User(Context context, Gson gson, PreferencesUtil preferencesUtil){
        this.context = context;
        this.gson = gson;
        this.preferenceUtil = preferencesUtil;
    }
}
