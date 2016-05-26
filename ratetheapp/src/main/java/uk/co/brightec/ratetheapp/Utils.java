/*
 * Copyright 2016 Brightec Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.brightec.ratetheapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.common.annotations.VisibleForTesting;

public class Utils {

    private static final String PREFERENCES_FILE = "ratetheapp_settings";

    public static String getDeviceName() {
        return formatDeviceName(Build.MANUFACTURER, Build.MODEL);
    }

    /**
     * Formats the manufacturer and model strings into a human readable device name
     *
     * @param manufacturer String e.g. Build.MANUFACTURER
     * @param model        String e.g. Build.MODEL
     * @return String Human readable device name e.g. Samsung S6
     */
    @VisibleForTesting
    static String formatDeviceName(String manufacturer, String model) {
        // FIXME: alistairsykes 26/05/2016 : If model is "" we might not want "manufacturer " (space)
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * Capitalize function will make the first letter of the first word of the given string to a capital. Only tested in for UK English language. Only the first char will be made upper case.
     *
     * @param s String
     * @return String The capitalized string. null or "" will return "". " t" will return " t".
     */
    @VisibleForTesting
    static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return ""; //Should this really return empty for a null?
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
