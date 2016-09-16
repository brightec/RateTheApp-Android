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
import android.support.annotation.Nullable;

public class InstanceSettings {

    private static final String PREF_SHOW_SUFFIX = "_show";
    private static final String PREF_RATING_SUFFIX = "_rating";

    private Context mContext;
    private String mInstanceName;

    InstanceSettings(Context context, String instanceName) {
        mContext = context;
        mInstanceName = instanceName;
    }

    /**
     * Returns the InstanceSettings for the RateTheApp widget
     * Note: If you are using the 'app:rateTheAppName' you need to provide the rateTheAppName you
     * want the InstanceSettings of.
     *
     * @param context Context
     * @return InstanceSettings
     */
    @SuppressWarnings({"unused"})
    public static InstanceSettings getInstanceSettings(Context context) {
        return getInstanceSettings(context, null);
    }

    /**
     * Returns the InstanceSettings for the RateTheApp widget
     * Note: If you are using the 'app:rateTheAppName' you need to provide the rateTheAppName you
     * want the InstanceSettings of.
     *
     * @param context        Context
     * @param rateTheAppName String
     * @return InstanceSettings
     */
    @SuppressWarnings({"unused"})
    public static InstanceSettings getInstanceSettings(Context context, @Nullable String
            rateTheAppName) {
        String instanceName = Utils.getInstanceNameFromRateTheAppName(rateTheAppName);
        return new InstanceSettings(context, instanceName);
    }

    /**
     * Returns a boolean to indicate whether the given instance of RateTheApp should be shown
     *
     * @return boolean TRUE if should be shown
     */
    public boolean shouldShow() {
        return Utils.readSharedSetting(mContext, mInstanceName + PREF_SHOW_SUFFIX, true);
    }

    /**
     * Set the given instance of RateTheApp to hide permanently
     */
    public void hidePermanently() {
        Utils.saveSharedSetting(mContext, mInstanceName + PREF_SHOW_SUFFIX, false);
    }

    /**
     * Save the rating on the given instance of RateTheApp
     *
     * @param rating float
     */
    public void saveRating(float rating) {
        Utils.saveSharedSetting(mContext, mInstanceName + PREF_RATING_SUFFIX, rating);
    }

    /**
     * Get the rating on the given instance of RateTheApp
     *
     * @param defaultRating float The default value you would like returned if no rating has been
     *                      saved
     * @return float
     */
    public float getSavedRating(float defaultRating) {
        return Utils.readSharedSetting(mContext, mInstanceName + PREF_RATING_SUFFIX, defaultRating);
    }

    /**
     * Reset the given instance of RateTheApp widget's rating and visibility.
     */
    public void resetWidget() {
        // Reset the widget visibility to true
        Utils.saveSharedSetting(mContext, mInstanceName + PREF_SHOW_SUFFIX, true);
        // Rest the widget rating to zero
        saveRating(0f);
    }
}
