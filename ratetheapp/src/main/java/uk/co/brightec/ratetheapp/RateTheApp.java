package uk.co.brightec.ratetheapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateTheApp extends LinearLayout {

    public static final String PREF_RATETHEAPP_PREFIX = "ratetheapp";
    public static final String PREF_RATETHEAPP_SHOW_SUFFIX = "_show";
    public static final String PREF_RATETHEAPP_RATING_SUFFIX = "_rating";

    private static final int DEFAULT_NUMBER_OF_STARS = 5;
    private static final float DEFAULT_STEP_SIZE = 1f;
    private static final float DEFAULT_RATING = 0.0f;

    private String mInstanceName;
    private String mTitleText;
    private int mTitleTextAppearanceResId;
    private int mSelectedStarColour;
    private int mUnselectedStarColour;
    private RatingBar mRatingBar;
    private int mNumberOfStars;
    private float mStepSize;
    private float mDefaultRating;
    private float mRating;
    private boolean mSaveRating;

    public interface OnRatingChangeListener {
        void onRatingChanged(RateTheApp rateTheApp, float rating, boolean fromUser);
    }

    private OnRatingChangeListener mRatingChangeListener;

    public OnRatingChangeListener getRatingChangeListener() {
        return mRatingChangeListener;
    }

    public void setRatingChangeListener(OnRatingChangeListener ratingChangeListener) {
        mRatingChangeListener = ratingChangeListener;
    }

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

        // Instance name for this rating bar
        mInstanceName = PREF_RATETHEAPP_PREFIX;
        String instanceName = a.getString(R.styleable.RateTheApp_rateTheAppName);
        if (instanceName != null) {
            mInstanceName += "_" + instanceName;
        }

        // Title Text Appearance
        mTitleTextAppearanceResId = a.getResourceId(R.styleable.RateTheApp_rateTheAppTitleTextAppearance, R.style.RateTheAppTitleTextAppearance);
        mTitleText = a.getString(R.styleable.RateTheApp_rateTheAppTitleText);

        // Stars & Rating
        mNumberOfStars = a.getInt(R.styleable.RateTheApp_rateTheAppNumberOfStars, DEFAULT_NUMBER_OF_STARS);
        mStepSize = a.getFloat(R.styleable.RateTheApp_rateTheAppStepSize, DEFAULT_STEP_SIZE);
        mDefaultRating = a.getFloat(R.styleable.RateTheApp_rateTheAppDefaultRating, DEFAULT_RATING);
        mSelectedStarColour = a.getColor(R.styleable.RateTheApp_rateTheAppSelectedStarColor, getColor(R.color.RateTheApp_SelectedStarColor));
        mUnselectedStarColour = a.getColor(R.styleable.RateTheApp_rateTheAppUnselectedStarColor, getColor(R.color.RateTheApp_UnselectedStarColor));

        mSaveRating = a.getBoolean(R.styleable.RateTheApp_rateTheAppSaveRating, true);

        a.recycle();

        if (!isInEditMode() && !shouldShow()) {
            this.setVisibility(GONE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ratetheapp_layout, this, true);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setNumStars(mNumberOfStars);
        mRatingBar.setStepSize(mStepSize);

        // Initialise the title
        initTitle();

        // Initialise the stars
        initStars();

        // Set previously saved rating (else use default rating)
        mRating = getSavedRating(-1f);
        if (mRating == -1f) {
            mRating = mDefaultRating;
        }

        mRatingBar.setRating(mRating);

        mRatingBar.setOnRatingBarChangeListener(ratingChangeListener);

        // Set the default RateChangeListener
        setRatingChangeListener(DefaultOnRatingChangeListener.createDefaultInstance(getContext()));
    }

    private int getColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(colorResId, null);
        }
        else {
            return getResources().getColor(colorResId);
        }
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

    private void initStars() {
        LayerDrawable drawable = (LayerDrawable) mRatingBar.getProgressDrawable();
        Drawable progress2 = drawable.getDrawable(2);
        DrawableCompat.setTint(progress2, mSelectedStarColour);

        Drawable progress1 = drawable.getDrawable(1);
        DrawableCompat.setTintMode(progress1, PorterDuff.Mode.DST_ATOP);
        DrawableCompat.setTint(progress1, mSelectedStarColour);
        DrawableCompat.setTintMode(progress1, PorterDuff.Mode.SRC_ATOP);
        DrawableCompat.setTint(progress1, mUnselectedStarColour);

        Drawable progress = drawable.getDrawable(0);
        DrawableCompat.setTint(progress, mUnselectedStarColour);
    }

    public boolean shouldShow() {
        return Utils.readSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_SHOW_SUFFIX, true);
    }

    public void hidePermanently () {
        Utils.saveSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_SHOW_SUFFIX, false);
        this.setVisibility(GONE);
    }

    private void saveRating(float rating) {
        Utils.saveSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_RATING_SUFFIX, rating);
    }

    private float getSavedRating(float defaultRating) {
        return Utils.readSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_RATING_SUFFIX, defaultRating);
    }

    public float getRating() {
        return mRatingBar.getRating();
    }

    private RatingBar.OnRatingBarChangeListener ratingChangeListener = new RatingBar.OnRatingBarChangeListener() {

        @Override
        public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
        // Save the rating
        if (mSaveRating) {
            saveRating(rating);
        }

        // If a rateChangeListener was provided, call it
        if (mRatingChangeListener != null) {
            mRatingChangeListener.onRatingChanged(RateTheApp.this, rating, fromUser);
        }
        }
    };
}