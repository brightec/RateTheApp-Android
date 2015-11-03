package uk.co.brightec.ratetheapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class Utils {

    private static final String PREFERENCES_FILE = "ratetheapp_settings";

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Helper method to read an app preference
     */
    public static Float readSharedSetting(Context ctx, String settingName, Float defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getFloat(settingName, defaultValue);
    }

    /**
     * Helper method to save an app preference
     */
    public static void saveSharedSetting(Context ctx, String settingName, Float settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(settingName, settingValue);
        editor.apply();
    }

    /**
     * Helper method to save an app preference
     */
    public static void saveSharedSetting(Context ctx, String settingName, Boolean settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(settingName, settingValue);
        editor.apply();
    }

    /**
     * Helper method to read an app preference
     */
    public static Boolean readSharedSetting(Context ctx, String settingName, Boolean defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(settingName, defaultValue);
    }
}
