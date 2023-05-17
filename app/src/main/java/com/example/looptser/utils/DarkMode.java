package com.example.looptser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkMode {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean nightMode;

    public DarkMode(Context context) {
        sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
    }

    public void setDarkModeSwitch(Switch switchMode) {
        switchMode.setChecked(nightMode);

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });
    }
}

