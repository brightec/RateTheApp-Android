package uk.co.brightec.ratetheapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateTheApp extends LinearLayout {

    private static final int MIN_GOOD_RATING = 3;
    public static final String PREF_RATETHEAPP_SHOW = "ratetheapp_show";
    public static final String PREF_RATETHEAPP_RATING = "ratetheapp_rating";

    private String mTitleText;
    private int mTitleTextAppearanceResId;

    public RateTheApp(Context context) {
        this(context, null);
    }

    public RateTheApp(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RateTheApp(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RateTheApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes)  {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RateTheApp, defStyleAttr, defStyleRes);

        // Title Text Appearance
        mTitleTextAppearanceResId = a.getResourceId(R.styleable.RateTheApp_rateTheAppTitleTextAppearance, R.style.RateTheAppTitleTextAppearance);
        mTitleText = a.getString(R.styleable.RateTheApp_rateTheAppTitleText);

        a.recycle();

        if (!isInEditMode() && !shouldShow()) {
            this.setVisibility(GONE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ratetheapp_layout, this, true);

        // Initialise the title
        initTitle();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        float rating = getSavedRating();
        ratingBar.setRating(rating);

        ratingBar.setOnRatingBarChangeListener(ratingChangeListener);
    }

    private void initTitle() {
        TextView title = (TextView) findViewById(R.id.ratingTitleText);
        // Hide the title if an empty title text attribute was provided
        if (mTitleText != null && mTitleText.isEmpty()) {
            title.setVisibility(GONE);
        }
        else {
            // Set the title text if provided
            if (mTitleText != null) {
                title.setText(mTitleText);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setTextAppearance(mTitleTextAppearanceResId);
            }
            else {
                title.setTextAppearance(getContext(), mTitleTextAppearanceResId);
            }
        }
    }

    private boolean shouldShow() {
        return Utils.readSharedSetting(getContext(), PREF_RATETHEAPP_SHOW, true);
    }

    private void hidePermanently () {
        Utils.saveSharedSetting(getContext(), PREF_RATETHEAPP_SHOW, false);
        this.setVisibility(GONE);
    }

    private void saveRating(float rating) {
        Utils.saveSharedSetting(getContext(), PREF_RATETHEAPP_RATING, rating);
    }

    private float getSavedRating() {
        return Utils.readSharedSetting(getContext(), PREF_RATETHEAPP_RATING, 0.0f);
    }

    private RatingBar.OnRatingBarChangeListener ratingChangeListener = new RatingBar.OnRatingBarChangeListener() {

        @Override
        public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
            saveRating(rating);
            if (rating > MIN_GOOD_RATING) {
                String message = getContext().getString(R.string.ratetheapp_goodrating_text);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(R.string.ratetheapp_goodrating_title);
                alertDialog.setMessage(message);
                alertDialog.setPositiveButton(R.string.ratetheapp_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hidePermanently();
                        gotoAppStore();
                    }
                });
                alertDialog.setNegativeButton(R.string.ratetheapp_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hidePermanently();
                    }
                });
                alertDialog.create().show();
            } else {
                String message = getContext().getString(R.string.ratetheapp_badrating_text);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(R.string.ratetheapp_badrating_title);
                alertDialog.setMessage(message);
                alertDialog.setPositiveButton(R.string.ratetheapp_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail((int) rating);
                    }
                });
                alertDialog.setNegativeButton(R.string.ratetheapp_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing to do..
                    }
                });
                alertDialog.create().show();
            }
        }
    };

    private void gotoAppStore() {
        final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
        try {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void sendEmail(int numStars) {
        PackageInfo pInfo = null;
        try {
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo != null ? pInfo.versionName : "Unknown";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.ratetheapp_feedback_emailaddress)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.ratetheapp_feedback_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.ratetheapp_feedback_extra_information, numStars, Utils.getDeviceName(), Build.VERSION.SDK_INT, version));

        getContext().startActivity(Intent.createChooser(intent, "Send Email"));
    }
}