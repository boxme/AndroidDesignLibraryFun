package com.desmond.androiddesignlibraryfun;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by desmond on 28/7/15.
 */
public class SwitchableFAB extends FloatingActionButton {

    AnimatorSet mAnimSet;

    boolean mFirstState;

    Drawable mSrcFirst;
    Drawable mSrcSecond;

    ColorStateList mBgTintFirst;
    ColorStateList mBgTintSecond;

    public SwitchableFAB(Context context) {
        super(context);
        init(context, null);
    }

    public SwitchableFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwitchableFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mFirstState = true;
        mSrcFirst = getDrawable();
        mBgTintFirst = getBackgroundTintList();

        if (attrs != null) {
            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.SwitchableFAB, 0, 0);
            mSrcSecond = attr.getDrawable(R.styleable.SwitchableFAB_srcSecond);
            mBgTintSecond = attr.getColorStateList(R.styleable.SwitchableFAB_backgroundTintSecond);
            attr.recycle();
        }
    }

    public void switchState() {
        if (mFirstState) {
            switchState(mSrcSecond, mBgTintSecond);
        } else {
            switchState(mSrcFirst, mBgTintFirst);
        }

        mFirstState = !mFirstState;
    }

    private void switchState(final Drawable src, final ColorStateList tint) {
        if (mAnimSet != null) {
            mAnimSet.cancel();
            mAnimSet = null;
        }

        final int scaleDuration = 200;
        final Drawable currentSrc = getDrawable();

        // Scaling down animation
        ObjectAnimator circleAnimOutX = ObjectAnimator.ofFloat(this, "scaleX", 1.0F, 0.1F);
        ObjectAnimator circleAnimOutY = ObjectAnimator.ofFloat(this, "scaleY", 1.0F, 0.1F);
        circleAnimOutX.setDuration(scaleDuration);
        circleAnimOutY.setDuration(scaleDuration);

        final int alphaAnimDuration = 150;

        // Alpha out of the current icon
        ObjectAnimator iconAnimOut = ObjectAnimator.ofInt(currentSrc, "alpha", 255, 0);
        iconAnimOut.setDuration(alphaAnimDuration);

        final int initialDelay = 100;

        final AnimatorSet animOutSet = new AnimatorSet();
        animOutSet.setInterpolator(new AccelerateInterpolator());
        animOutSet.playTogether(circleAnimOutX, circleAnimOutY, iconAnimOut);
        animOutSet.setStartDelay(initialDelay);
        animOutSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                SwitchableFAB.this.setBackgroundTintList(tint);
                SwitchableFAB.this.setImageDrawable(src);
                SwitchableFAB.this.jumpDrawablesToCurrentState();
                animOutSet.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        // Scaling up animation
        ObjectAnimator circleAnimInX = ObjectAnimator.ofFloat(this, "scaleX", 0.1F, 1.0F);
        ObjectAnimator circleAnimInY = ObjectAnimator.ofFloat(this, "scaleY", 0.1F, 1.0F);
        circleAnimInX.setDuration(scaleDuration);
        circleAnimInY.setDuration(scaleDuration);

        final int alphaAnimDelay = 50;

        // Alpha in of the next icon
        src.setAlpha(0);
        ObjectAnimator iconAnimIn = ObjectAnimator.ofInt(src, "alpha", 0, 255);
        iconAnimIn.setDuration(alphaAnimDuration);
        iconAnimIn.setStartDelay(alphaAnimDelay);

        final AnimatorSet animInSet = new AnimatorSet();
        animInSet.setInterpolator(new DecelerateInterpolator());
        animInSet.playTogether(circleAnimInX, circleAnimInY, iconAnimIn);

        mAnimSet = new AnimatorSet();
        mAnimSet.playSequentially(animOutSet, animInSet);
        mAnimSet.start();
    }
}
