package com.wohoocreators.dynamicisland;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    private final Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public boolean getSetting(String setting) {
        // Get preferences
        SharedPreferences prefs = context.getSharedPreferences("com.wohoocreators.dynamicisland", Context.MODE_PRIVATE);
        return prefs.getBoolean(setting, false);
    }
    public int getSettingInt(String setting) {
        // Get preferences
        SharedPreferences prefs = context.getSharedPreferences("com.wohoocreators.dynamicisland", Context.MODE_PRIVATE);
        return prefs.getInt(setting, 0);
    }
}
