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
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
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
    private String mTitleStr, mMessageStr;
    private int mTitleTextAppearanceResId, mMessageTextAppearanceResId;
    private int mSelectedStarColour;
    private int mUnselectedStarColour;
    private RatingBar mRatingBar;
    private int mNumberOfStars;
    @DrawableRes
    private int mDrawableResSelected;
    @DrawableRes
    private int mDrawableResUnSelected;
    private float mStepSize;
    private float mDefaultRating;
    private float mRating;
    private boolean mSaveRating;

    private TextView mTextTitle, mTextMessage;
    private OnUserSelectedRatingListener mOnUserSelectedRatingListener;
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

    public OnUserSelectedRatingListener getOnUserSelectedRatingListener() {
        return mOnUserSelectedRatingListener;
    }

    public void setOnUserSelectedRatingListener(OnUserSelectedRatingListener onUserSelectedRatingListener) {
        mOnUserSelectedRatingListener = onUserSelectedRatingListener;
    }

    private void loadAttributes(AttributeSet attrs) {
        loadAttributes(attrs, 0, 0);
    }

    private void loadAttributes(AttributeSet attrs, int defStyleAttr) {
        loadAttributes(attrs, defStyleAttr, 0);
    }

    private void loadAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        mTitleStr = a.getString(R.styleable.RateTheApp_rateTheAppTitleText);

        // Message Text Appearance
        mMessageTextAppearanceResId = a.getResourceId(R.styleable.RateTheApp_rateTheAppMessageTextAppearance, R.style.RateTheAppMessageTextAppearance);
        mMessageStr = a.getString(R.styleable.RateTheApp_rateTheAppMessageText);

        // Stars & Rating
        mNumberOfStars = a.getInt(R.styleable.RateTheApp_rateTheAppNumberOfStars, DEFAULT_NUMBER_OF_STARS);
        mStepSize = a.getFloat(R.styleable.RateTheApp_rateTheAppStepSize, DEFAULT_STEP_SIZE);
        mDefaultRating = a.getFloat(R.styleable.RateTheApp_rateTheAppDefaultRating, DEFAULT_RATING);
        mSelectedStarColour = a.getColor(R.styleable.RateTheApp_rateTheAppSelectedStarColor, ContextCompat.getColor(getContext(), R.color.RateTheApp_SelectedStarColor));
        mUnselectedStarColour = a.getColor(R.styleable.RateTheApp_rateTheAppUnselectedStarColor, ContextCompat.getColor(getContext(), R.color.RateTheApp_UnselectedStarColor));
        mDrawableResUnSelected = a.getResourceId(R.styleable.RateTheApp_rateTheAppStarUnSelectedDrawable, R.drawable.ic_rating_star_border_grey_36dp);
        mDrawableResSelected = a.getResourceId(R.styleable.RateTheApp_rateTheAppStarSelectedDrawable, R.drawable.ic_rating_star_green_36dp);

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

        mRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mRatingBar.setNumStars(mNumberOfStars);
        mRatingBar.setStepSize(mStepSize);

        // Initialise the title
        initTitle();

        // Initialise the message
        initMessage();

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

    private void initTitle() {
        mTextTitle = (TextView) findViewById(R.id.text_rating_title);
        // Hide the title if an empty title text attribute was provided
        if (mTitleStr != null && mTitleStr.isEmpty()) {
            mTextTitle.setVisibility(GONE);
        } else {
            // Set the title text if provided
            if (mTitleStr != null) {
                mTextTitle.setText(mTitleStr);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTextTitle.setTextAppearance(mTitleTextAppearanceResId);
            } else {
                mTextTitle.setTextAppearance(getContext(), mTitleTextAppearanceResId);
            }
        }
    }

    private void initMessage() {
        mTextMessage = (TextView) findViewById(R.id.text_rating_message);
        // Hide the message if an empty message text attribute was provided
        if (mMessageStr != null && mMessageStr.isEmpty()) {
            mTextMessage.setVisibility(GONE);
        } else {
            // Set the title text if provided
            if (mMessageStr != null) {
                mTextMessage.setText(mMessageStr);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTextMessage.setTextAppearance(mMessageTextAppearanceResId);
            } else {
                mTextMessage.setTextAppearance(getContext(), mMessageTextAppearanceResId);
            }
        }
    }

    private void initStars() {
        Drawable selected = ContextCompat.getDrawable(getContext(), mDrawableResSelected);
        selected = selected.mutate();
        selected.setColorFilter(new PorterDuffColorFilter(mSelectedStarColour, PorterDuff.Mode.SRC_ATOP));

        Drawable unselected = ContextCompat.getDrawable(getContext(), mDrawableResUnSelected);
        unselected = unselected.mutate();
        unselected.setColorFilter(new PorterDuffColorFilter(mUnselectedStarColour, PorterDuff.Mode.SRC_ATOP));

        LayerDrawable ld = (LayerDrawable) mRatingBar.getProgressDrawable();
        ld.setDrawableByLayerId(android.R.id.background, unselected);
        ld.setDrawableByLayerId(android.R.id.secondaryProgress, unselected);
        ld.setDrawableByLayerId(android.R.id.progress, selected);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRatingBar.setProgressDrawableTiled(ld);
        } else {
            Drawable tiledDrawable = tileify(ld, false);
            mRatingBar.setProgressDrawable(tiledDrawable);
        }
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively traverse layer and state list drawables.
     * This method was copied from android.widget.ProgressBar, however it was highlighted in their code that this may be sub optimal.
     *
     * @param drawable The drawable to tileify
     * @param clip     Whether to clip drawable
     * @return The tiled drawable
     */
    private Drawable tileify(Drawable drawable, boolean clip) {
        if (drawable instanceof LayerDrawable) {
            final LayerDrawable orig = (LayerDrawable) drawable;
            final int N = orig.getNumberOfLayers();
            final Drawable[] outDrawables = new Drawable[N];

            for (int i = 0; i < N; i++) {
                final int id = orig.getId(i);
                outDrawables[i] = tileify(orig.getDrawable(i), (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }

            final LayerDrawable clone = new LayerDrawable(outDrawables);
            for (int i = 0; i < N; i++) {
                clone.setId(i, orig.getId(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    clone.setLayerGravity(i, orig.getLayerGravity(i));
                    clone.setLayerWidth(i, orig.getLayerWidth(i));
                    clone.setLayerHeight(i, orig.getLayerHeight(i));
                    clone.setLayerInsetLeft(i, orig.getLayerInsetLeft(i));
                    clone.setLayerInsetRight(i, orig.getLayerInsetRight(i));
                    clone.setLayerInsetTop(i, orig.getLayerInsetTop(i));
                    clone.setLayerInsetBottom(i, orig.getLayerInsetBottom(i));
                    clone.setLayerInsetStart(i, orig.getLayerInsetStart(i));
                    clone.setLayerInsetEnd(i, orig.getLayerInsetEnd(i));
                }
            }

            return clone;
        }

        if (drawable instanceof BitmapDrawable) {
            final BitmapDrawable bitmap = (BitmapDrawable) drawable;

            final BitmapDrawable clone = (BitmapDrawable) bitmap.getConstantState().newDrawable();
            clone.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);

            if (clip) {
                return new ClipDrawable(clone, Gravity.LEFT, ClipDrawable.HORIZONTAL);
            } else {
                return clone;
            }
        }

        return drawable;
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

    public float getRating() {
        return mRatingBar.getRating();
    }

    public void setRating(float rating) {
        mRatingBar.setRating(rating);
    }

    public void reset() {
        mRatingBar.setRating(0);
    }

    /**
     * This method returns the TextView which represents the title in the layout. This method is provided for specific use cases where this library has not provided the exact config needed.
     *
     * @return TextView mTextTitle - The TextView associated with the title
     */
    public TextView getTitleTextView() {
        return mTextTitle;
    }

    /**
     * This method returns the TextView which represents the message in the layout. This method is provided for specific use cases where this library has not provided the exact config needed.
     *
     * @return TextView mTextMessage - The TextView associated with the message
     */
    public TextView getMessageTextView() {
        return mTextMessage;
    }

    public interface OnUserSelectedRatingListener {
        void onRatingChanged(RateTheApp rateTheApp, float rating);
    }
}