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
    private String mTitleTextStr;
    private int mTitleTextAppearanceResId;
    private int mSelectedStarColour;
    private int mUnselectedStarColour;
    private RatingBar mRatingBar;
    private int mNumberOfStars;
    private float mStepSize;
    private float mDefaultRating;
    private float mRating;
    private boolean mSaveRating;

    private TextView mTextTitle;

    public interface OnUserSelectedRatingListener {
        void onRatingChanged(RateTheApp rateTheApp, float rating);
    }

    private OnUserSelectedRatingListener mOnUserSelectedRatingListener;

    public OnUserSelectedRatingListener getOnUserSelectedRatingListener() {
        return mOnUserSelectedRatingListener;
    }

    public void setOnUserSelectedRatingListener(OnUserSelectedRatingListener onUserSelectedRatingListener) {
        mOnUserSelectedRatingListener = onUserSelectedRatingListener;
    }

    public RateTheApp(Context context) {
        super(context);
        init();
    }

    public RateTheApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs);
        init();
    }

    public RateTheApp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RateTheApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadAttributes(attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void loadAttributes(AttributeSet attrs){
        loadAttributes(attrs, 0, 0);
    }

    private void loadAttributes(AttributeSet attrs, int defStyleAttr){
        loadAttributes(attrs, defStyleAttr, 0);
    }

    private void loadAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes){
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
        mTitleTextStr = a.getString(R.styleable.RateTheApp_rateTheAppTitleText);

        // Stars & Rating
        mNumberOfStars = a.getInt(R.styleable.RateTheApp_rateTheAppNumberOfStars, DEFAULT_NUMBER_OF_STARS);
        mStepSize = a.getFloat(R.styleable.RateTheApp_rateTheAppStepSize, DEFAULT_STEP_SIZE);
        mDefaultRating = a.getFloat(R.styleable.RateTheApp_rateTheAppDefaultRating, DEFAULT_RATING);
        mSelectedStarColour = a.getColor(R.styleable.RateTheApp_rateTheAppSelectedStarColor, getColor(R.color.RateTheApp_SelectedStarColor));
        mUnselectedStarColour = a.getColor(R.styleable.RateTheApp_rateTheAppUnselectedStarColor, getColor(R.color.RateTheApp_UnselectedStarColor));

        mSaveRating = a.getBoolean(R.styleable.RateTheApp_rateTheAppSaveRating, true);

        a.recycle();
    }

    private void init() {
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
        setOnUserSelectedRatingListener(DefaultOnUserSelectedRatingListener.createDefaultInstance(getContext()));
    }

    private int getColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(colorResId, null);
        } else {
            return getResources().getColor(colorResId);
        }
    }

    private void initTitle() {
        mTextTitle = (TextView) findViewById(R.id.ratingTitleText);
        // Hide the title if an empty title text attribute was provided
        if (mTitleTextStr != null && mTitleTextStr.isEmpty()) {
            mTextTitle.setVisibility(GONE);
        } else {
            // Set the title text if provided
            if (mTitleTextStr != null) {
                mTextTitle.setText(mTitleTextStr);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTextTitle.setTextAppearance(mTitleTextAppearanceResId);
            } else {
                mTextTitle.setTextAppearance(getContext(), mTitleTextAppearanceResId);
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

    public void hidePermanently() {
        Utils.saveSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_SHOW_SUFFIX, false);
        this.setVisibility(GONE);
    }

    private void saveRating(float rating) {
        Utils.saveSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_RATING_SUFFIX, rating);
    }

    private float getSavedRating(float defaultRating) {
        return Utils.readSharedSetting(getContext(), mInstanceName + PREF_RATETHEAPP_RATING_SUFFIX, defaultRating);
    }

    public void setRating(float rating) {
        mRatingBar.setRating(rating);
    }

    public float getRating() {
        return mRatingBar.getRating();
    }

    public void reset() {
        mRatingBar.setRating(0);
    }

    private RatingBar.OnRatingBarChangeListener ratingChangeListener = new RatingBar.OnRatingBarChangeListener() {

        @Override
        public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
            // Save the rating
            if (mSaveRating) {
                saveRating(rating);
            }

            // If a rateChangeListener was provided, call it
            if (mOnUserSelectedRatingListener != null && fromUser) {
                mOnUserSelectedRatingListener.onRatingChanged(RateTheApp.this, rating);
            }
        }
    };

    public TextView getTitleTextView() {
        return mTextTitle;
    }
}