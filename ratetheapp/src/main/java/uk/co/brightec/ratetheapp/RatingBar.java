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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Subclass of android.widget.RatingBar which aims to fix vectors drawables not working and the
 * sizing going wrong with different size drawables.
 *
 * @see <a href="https://code.google.com/p/android/issues/detail?id=196713">Google issue</a>
 * @see <a href="http://stackoverflow.com/questions/34001823/ratingbar-custom-vector-drawables-superimposing/">Stack overflow answer</a>
 * @see android.widget.RatingBar
 */
public class RatingBar extends android.widget.RatingBar {

    private Bitmap mSampleTile;
    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Returns {@code true} if the target drawable needs to be tileified.
     * <p/>
     * Note: Copied from android.widget.ProgressBar
     *
     * @param dr the drawable to check
     * @return {@code true} if the target drawable needs to be tileified,
     * {@code false} otherwise
     */
    private static boolean needsTileify(Drawable dr) {
        if (dr instanceof LayerDrawable) {
            final LayerDrawable orig = (LayerDrawable) dr;
            final int N = orig.getNumberOfLayers();
            for (int i = 0; i < N; i++) {
                if (needsTileify(orig.getDrawable(i))) {
                    return true;
                }
            }
            return false;
        }

        // If there's a bitmap that's not wrapped with a ClipDrawable or
        // ScaleDrawable, we'll need to wrap it and apply tiling.
        if (dr instanceof BitmapDrawable || dr instanceof VectorDrawable) {
            return true;
        }

        return false;
    }

    /**
     * Initialise the drawables/stars on the RatingBar.
     * REQUIRED - For this class to have any affect
     *
     * @param drawableResSelected   The selected star drawable
     * @param selectedStarColour    The selected star color
     * @param drawableResUnSelected The unselected star drawable
     * @param unselectedStarColour  The unselected star color
     */
    public void initStars(@DrawableRes int drawableResSelected,
                          @ColorInt @Nullable Integer selectedStarColour,
                          @DrawableRes int drawableResUnSelected,
                          @ColorInt @Nullable Integer unselectedStarColour) {

        Drawable selected = ContextCompat.getDrawable(getContext(), drawableResSelected);
        if (selectedStarColour != null) {
            if (isInEditMode()) {
                selected.setColorFilter(new PorterDuffColorFilter(selectedStarColour,
                        PorterDuff.Mode.SRC_ATOP));
            } else {
                selected = selected.mutate();
                selected.setColorFilter(new PorterDuffColorFilter(selectedStarColour,
                        PorterDuff.Mode.SRC_ATOP));
            }
        }

        Drawable unselected = ContextCompat.getDrawable(getContext(), drawableResUnSelected);
        if (unselectedStarColour != null) {
            if (isInEditMode()) {
                unselected.setColorFilter(new PorterDuffColorFilter(unselectedStarColour,
                        PorterDuff.Mode.SRC_ATOP));
            } else {
                unselected = unselected.mutate();
                unselected.setColorFilter(new PorterDuffColorFilter(unselectedStarColour,
                        PorterDuff.Mode.SRC_ATOP));
            }
        }

        LayerDrawable ld = (LayerDrawable) getProgressDrawable();
        ld.setDrawableByLayerId(android.R.id.background, unselected);
        ld.setDrawableByLayerId(android.R.id.secondaryProgress, unselected);
        ld.setDrawableByLayerId(android.R.id.progress, selected);

        Drawable progressDrawable;
        if (needsTileify(ld)) {
            progressDrawable = tileify(ld, false);
            setProgressDrawable(progressDrawable);
        } else {
            progressDrawable = ld;
            setProgressDrawable(progressDrawable);
        }
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     * <p/>
     * Note: Copied from android.widget.ProgressBar
     */
    private Drawable tileify(Drawable drawable, boolean clip) {
        if (drawable instanceof DrawableWrapper) {
            Drawable inner = ((DrawableWrapper) drawable).getWrappedDrawable();
            if (inner != null) {
                inner = tileify(inner, clip);
                ((DrawableWrapper) drawable).setWrappedDrawable(inner);
            }
            return drawable;
        }

        if (drawable instanceof LayerDrawable) {
            final LayerDrawable orig = (LayerDrawable) drawable;
            final int N = orig.getNumberOfLayers();
            final Drawable[] outDrawables = new Drawable[N];

            for (int i = 0; i < N; i++) {
                final int id = orig.getId(i);
                outDrawables[i] = tileify(orig.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
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
            final Bitmap tileBitmap = bitmap.getBitmap();
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }

            final BitmapDrawable clone = (BitmapDrawable) bitmap.getConstantState().newDrawable();
            clone.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);

            if (clip) {
                return new ClipDrawable(clone, Gravity.LEFT, ClipDrawable.HORIZONTAL);
            } else {
                return clone;
            }
        }

        if (drawable instanceof VectorDrawable) {
            return tileify(getBitmapDrawableFromVectorDrawable(drawable), clip);
        }

        return drawable;
    }

    /**
     * Create a BitmapDrawable from a Drawable (particularly useful for VectorDrawable)
     *
     * @param drawable Drawable
     * @return BitmapDrawable
     */
    private BitmapDrawable getBitmapDrawableFromVectorDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener The listener.
     */
    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        mOnRatingBarChangeListener = listener;
        super.setOnRatingBarChangeListener(new android.widget.RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(android.widget.RatingBar ratingBar, float rating, boolean fromUser) {
                mOnRatingBarChangeListener.onRatingChanged(RatingBar.this, rating, fromUser);
            }
        });
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mSampleTile != null) {
            final int width = mSampleTile.getWidth() * getNumStars();
            setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                    getMeasuredHeight());
        }
    }

    /**
     * A callback that notifies clients when the rating has been changed. This
     * includes changes that were initiated by the user through a touch gesture
     * or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnRatingBarChangeListener {

        /**
         * Notification that the rating has changed. Clients can use the
         * fromUser parameter to distinguish user-initiated changes from those
         * that occurred programmatically. This will not be called continuously
         * while the user is dragging, only when the user finalizes a rating by
         * lifting the touch.
         *
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating    The current rating. This will be in the range
         *                  0..numStars.
         * @param fromUser  True if the rating change was initiated by a user's
         *                  touch gesture or arrow key/horizontal trackbell movement.
         */
        void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser);

    }
}
