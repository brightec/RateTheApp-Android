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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.google.common.annotations.VisibleForTesting;

/**
 * The default OnRatingChangeListener that offers to take the user to the play store if a good rating was given, otherwise it prompts
 * the user to email the app developer to provide feedback.
 */
public class DefaultOnUserSelectedRatingListener implements RateTheApp.OnUserSelectedRatingListener {
    private static final float DEFAULT_MIN_GOOD_RATING = 3;

    private float mMinGoodRating;
    private String mNegativeButtonText;
    private String mPositiveButtonText;
    private String mGoodRatingTitle;
    private String mGoodRatingMessage;
    private String mBadRatingTitle;
    private String mBadRatingMessage;
    private String mFeedbackEmailTo;
    private String mFeedbackEmailSubject;
    private String mFeedbackEmailMessage;

    public DefaultOnUserSelectedRatingListener(float minGoodRating, String negativeButtonText, String positiveButtonText, String goodRatingTitle, String goodRatingMessage, String badRatingTitle, String badRatingMessage, String feedbackEmailTo, String feedbackEmailSubject, String feedbackEmailMessage) {
        mMinGoodRating = minGoodRating;
        mNegativeButtonText = negativeButtonText;
        mPositiveButtonText = positiveButtonText;
        mGoodRatingTitle = goodRatingTitle;
        mGoodRatingMessage = goodRatingMessage;
        mBadRatingTitle = badRatingTitle;
        mBadRatingMessage = badRatingMessage;
        mFeedbackEmailTo = feedbackEmailTo;
        mFeedbackEmailSubject = feedbackEmailSubject;
        mFeedbackEmailMessage = feedbackEmailMessage;
    }

    public static DefaultOnUserSelectedRatingListener createDefaultInstance(Context context) {
        return new DefaultOnUserSelectedRatingListener(
                DEFAULT_MIN_GOOD_RATING,
                context.getString(R.string.ratetheapp_negative_button),
                context.getString(R.string.ratetheapp_positive_button),
                context.getString(R.string.ratetheapp_goodrating_title),
                context.getString(R.string.ratetheapp_goodrating_text),
                context.getString(R.string.ratetheapp_badrating_title),
                context.getString(R.string.ratetheapp_badrating_text),
                context.getString(R.string.ratetheapp_feedback_emailaddress),
                context.getString(R.string.ratetheapp_feedback_subject),
                null);
    }

    public float getMinGoodRating() {
        return mMinGoodRating;
    }

    public void setMinGoodRating(float minGoodRating) {
        mMinGoodRating = minGoodRating;
    }

    public String getNegativeButtonText() {
        return mNegativeButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        mNegativeButtonText = negativeButtonText;
    }

    public String getPositiveButtonText() {
        return mPositiveButtonText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        mPositiveButtonText = positiveButtonText;
    }

    public String getGoodRatingTitle() {
        return mGoodRatingTitle;
    }

    public void setGoodRatingTitle(String goodRatingTitle) {
        mGoodRatingTitle = goodRatingTitle;
    }

    public String getGoodRatingMessage() {
        return mGoodRatingMessage;
    }

    public void setGoodRatingMessage(String goodRatingMessage) {
        mGoodRatingMessage = goodRatingMessage;
    }

    public String getBadRatingTitle() {
        return mBadRatingTitle;
    }

    public void setBadRatingTitle(String badRatingTitle) {
        mBadRatingTitle = badRatingTitle;
    }

    public String getBadRatingMessage() {
        return mBadRatingMessage;
    }

    public void setBadRatingMessage(String badRatingMessage) {
        mBadRatingMessage = badRatingMessage;
    }

    public String getFeedbackEmailTo() {
        return mFeedbackEmailTo;
    }

    public void setFeedbackEmailTo(String feedbackEmailTo) {
        mFeedbackEmailTo = feedbackEmailTo;
    }

    public String getFeedbackEmailSubject() {
        return mFeedbackEmailSubject;
    }

    public void setFeedbackEmailSubject(String feedbackEmailSubject) {
        mFeedbackEmailSubject = feedbackEmailSubject;
    }

    @Override
    public void onRatingChanged(final RateTheApp rateTheApp, final float rating) {
        final Context context = rateTheApp.getContext();
        if (rating >= mMinGoodRating) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle(mGoodRatingTitle);
            alertDialog.setMessage(mGoodRatingMessage);
            alertDialog.setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rateTheApp.hidePermanently();
                    gotoAppStore(context);
                }
            });
            alertDialog.setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rateTheApp.hidePermanently();
                }
            });
            alertDialog.create().show();
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle(mBadRatingTitle);
            alertDialog.setMessage(mBadRatingMessage);
            alertDialog.setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendEmail(context, (int) rating);
                }
            });
            alertDialog.setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // nothing to do..
                }
            });
            alertDialog.create().show();
        }
    }

    private void gotoAppStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void sendEmail(Context context, int numStars) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mFeedbackEmailTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, mFeedbackEmailSubject);
        if (mFeedbackEmailMessage == null) {
            mFeedbackEmailMessage = getDefaultEmailMessage(context, numStars);
        }
        intent.putExtra(Intent.EXTRA_TEXT, mFeedbackEmailMessage);

        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    /**
     * Method for obtaining a default feedback email message
     *
     * @param context  A context to get package info and strings against
     * @param numStars The star rating the user clicked so that this can be addeed to the email
     * @return String The a message to put into the email body
     */
    @VisibleForTesting
    String getDefaultEmailMessage(Context context, int numStars) {
        String version = "Unknown";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return context.getString(R.string.ratetheapp_feedback_extra_information, numStars, Utils.getDeviceName(), Build.VERSION.SDK_INT, version);
    }
}