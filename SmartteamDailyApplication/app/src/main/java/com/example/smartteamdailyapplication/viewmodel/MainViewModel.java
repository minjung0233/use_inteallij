package com.example.smartteamdailyapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class MainViewModel extends AndroidViewModel {

    //Tag-ovi za  SharedPreferences
    private static final String PREFS_THEME = "THEME";
    private static final String PREFS_SET = "IS_THEME_SET";

    //SharedPreferences
    SharedPreferences prefsTheme = getApplication().getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE);
    SharedPreferences prefsIsThemeSet =  getApplication().getSharedPreferences(PREFS_SET,  Context.MODE_PRIVATE);

    //MutableLiveData
    MutableLiveData<Boolean> fabClicked = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getFabClicked() {
        return fabClicked;
    }

    public void onFabClicked() {
        fabClicked.setValue(true);
    }

    //SharedPreferences Getters and Setters
//    public SharedPreferences getPrefsTheme() {
//        return prefsTheme;
//    }
//
//    public SharedPreferences getPrefsIsThemeSet() {
//        return prefsIsThemeSet;
//    }
//
//    public void setPrefsIsThemeSet() {
//        SharedPreferences.Editor editor = getApplication().getSharedPreferences(PREFS_SET, Context.MODE_PRIVATE).edit();
//        editor.putBoolean("isThemeSet", false );
//        editor.apply();
//    }
}
