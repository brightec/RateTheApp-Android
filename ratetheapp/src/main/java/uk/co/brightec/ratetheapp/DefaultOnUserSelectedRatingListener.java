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

    /**
     * The creates a default instance of this OnUserSelectedRatingListener.
     * Override the string resources to easily changes these values. Or use the setters provided.
     * <p/>
     * Default values are (see createDefaultInstance(String) for other defaults):
     * <ul>
     * <li>mFeedbackEmailTo - mobile@website.com - R.string.ratetheapp_feedback_emailaddress</li>
     * </ul>
     *
     * @see .DefaultOnUserSelectedRatingListener.createDefaultInstance(String)
     */
    @SuppressWarnings({"unused"})
    public static DefaultOnUserSelectedRatingListener createDefaultInstance() {
        return createDefaultInstance(Application.instance.getString(R.string
                .ratetheapp_feedback_emailaddress));
    }

    /**
     * The creates a default instance of this OnUserSelectedRatingListener.
     * Override the string resources to easily changes these values. Or use the setters provided.
     * <p/>
     * Default values are:
     * <ul>
     * <li>mMinGoodRating - 3 - DefaultOnUserSelectedRatingListener.DEFAULT_MIN_GOOD_RATING</li>
     * <li>mNegativeButtonText - No Thanks - R.string.ratetheapp_negative_button</li>
     * <li>mPositiveButtonText - Sure - R.string.ratetheapp_positive_button</li>
     * <li>mGoodRatingTitle - Thanks! - R.string.ratetheapp_goodrating_title</li>
     * <li>mGoodRatingMessage - Thanks so much!  Would you mind rating us or leaving a review at
     * the App Store? - R.string.ratetheapp_goodrating_text</li>
     * <li>mBadRatingTitle - Hi There! - R.string.ratetheapp_badrating_title</li>
     * <li>mBadRatingMessage - I’m really sorry to hear that you don’t like our app. Would you mind
     * sending me your
     * thoughts on how we can improve the app? I’ll respond directly. Thanks for your help. - R.string.ratetheapp_badrating_text</li>
     * <li>mFeedbackEmailSubject - App Feedback: Android - R.string.ratetheapp_feedback_subject</li>
     * <li>mFeedbackEmailMessage - null</li>
     * </ul>
     *
     * @param emailTo String
     * @return DefaultOnUserSelectedRatingListener
     */
    @SuppressWarnings({"unused"})
    public static DefaultOnUserSelectedRatingListener createDefaultInstance(String emailTo) {
        return new DefaultOnUserSelectedRatingListener(
                DEFAULT_MIN_GOOD_RATING,
                Application.instance.getString(R.string.ratetheapp_negative_button),
                Application.instance.getString(R.string.ratetheapp_positive_button),
                Application.instance.getString(R.string.ratetheapp_goodrating_title),
                Application.instance.getString(R.string.ratetheapp_goodrating_text),
                Application.instance.getString(R.string.ratetheapp_badrating_title),
                Application.instance.getString(R.string.ratetheapp_badrating_text),
                emailTo,
                Application.instance.getString(R.string.ratetheapp_feedback_subject),
                null);
    }

    /**
     * Get the min good rating which has been set
     *
     * @return float
     */
    @SuppressWarnings({"unused"})
    public float getMinGoodRating() {
        return mMinGoodRating;
    }

    /**
     * Set the minimum rating required to be considered good
     *
     * @param minGoodRating float
     */
    @SuppressWarnings({"unused"})
    public void setMinGoodRating(float minGoodRating) {
        mMinGoodRating = minGoodRating;
    }

    /**
     * Get the text which is set on the negative button in the resulting alert dialog
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getNegativeButtonText() {
        return mNegativeButtonText;
    }

    /**
     * Set the text which is set on the negative button in the resulting alert dialog
     */
    @SuppressWarnings({"unused"})
    public void setNegativeButtonText(String negativeButtonText) {
        mNegativeButtonText = negativeButtonText;
    }

    /**
     * Get the text which is set on the positive button in the resulting alert dialog
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getPositiveButtonText() {
        return mPositiveButtonText;
    }

    /**
     * Set the text which is set on the positive button in the resulting alert dialog
     */
    @SuppressWarnings({"unused"})
    public void setPositiveButtonText(String positiveButtonText) {
        mPositiveButtonText = positiveButtonText;
    }

    /**
     * Get the text which is set as the title in the resulting alert dialog if the rating was
     * considered good
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getGoodRatingTitle() {
        return mGoodRatingTitle;
    }

    /**
     * Set the text which is set as the title in the resulting alert dialog if the rating was
     * considered good
     */
    @SuppressWarnings({"unused"})
    public void setGoodRatingTitle(String goodRatingTitle) {
        mGoodRatingTitle = goodRatingTitle;
    }

    /**
     * Get the text which is set as the message in the resulting alert dialog if the rating was
     * considered good
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getGoodRatingMessage() {
        return mGoodRatingMessage;
    }

    /**
     * Set the text which is set as the message in the resulting alert dialog if the rating was
     * considered good
     */
    @SuppressWarnings({"unused"})
    public void setGoodRatingMessage(String goodRatingMessage) {
        mGoodRatingMessage = goodRatingMessage;
    }

    /**
     * Get the text which is set as the title in the resulting alert dialog if the rating was
     * considered bad
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getBadRatingTitle() {
        return mBadRatingTitle;
    }

    /**
     * Set the text which is set as the title in the resulting alert dialog if the rating was
     * considered bad
     */
    @SuppressWarnings({"unused"})
    public void setBadRatingTitle(String badRatingTitle) {
        mBadRatingTitle = badRatingTitle;
    }

    /**
     * Get the text which is set as the message in the resulting alert dialog if the rating was
     * considered bad
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getBadRatingMessage() {
        return mBadRatingMessage;
    }

    /**
     * Set the text which is set as the message in the resulting alert dialog if the rating was
     * considered bad
     */
    @SuppressWarnings({"unused"})
    public void setBadRatingMessage(String badRatingMessage) {
        mBadRatingMessage = badRatingMessage;
    }

    /**
     * Get the email address feedback would be sent to
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getFeedbackEmailTo() {
        return mFeedbackEmailTo;
    }

    /**
     * Set the email address feedback would be sent to
     */
    @SuppressWarnings({"unused"})
    public void setFeedbackEmailTo(String feedbackEmailTo) {
        mFeedbackEmailTo = feedbackEmailTo;
    }

    /**
     * Get the subject which a feedback email would have
     *
     * @return String
     */
    @SuppressWarnings({"unused"})
    public String getFeedbackEmailSubject() {
        return mFeedbackEmailSubject;
    }

    /**
     * Set the subject which a feedback email would have
     */
    @SuppressWarnings({"unused"})
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
                    gotoAppStore(rateTheApp.getContext());
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
                    sendEmail(rateTheApp.getContext(), (int) rating);
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

    /**
     * Start activity which would present the playstore on our apps page
     *
     * @param context Context
     */
    private void gotoAppStore(Context context) {
        String appPackageName = Application.instance.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                    appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google" +
                    ".com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Start activity which would offer to send an email
     *
     * @param context  Context
     * @param numStars int
     */
    private void sendEmail(Context context, int numStars) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mFeedbackEmailTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, mFeedbackEmailSubject);
        if (mFeedbackEmailMessage == null) {
            mFeedbackEmailMessage = getDefaultEmailMessage(numStars);
        }
        intent.putExtra(Intent.EXTRA_TEXT, mFeedbackEmailMessage);

        context.startActivity(Intent.createChooser(intent, Application.instance.getString(
                R.string.email_intent_chooser_title)));
    }

    /**
     * Method for obtaining a default feedback email message
     *
     * @param numStars The star rating the user clicked so that this can be addeed to the email
     * @return String The a message to put into the email body
     */
    @VisibleForTesting
    String getDefaultEmailMessage(int numStars) {
        String version = "Unknown";
        try {
            version = Application.instance.getPackageManager().getPackageInfo(Application.instance.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return Application.instance.getString(R.string.ratetheapp_feedback_extra_information, numStars, Utils.getDeviceName(), Build.VERSION.SDK_INT, version);
    }
}