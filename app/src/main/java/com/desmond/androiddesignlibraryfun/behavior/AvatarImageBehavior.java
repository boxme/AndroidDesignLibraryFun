package com.desmond.androiddesignlibraryfun.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.desmond.androiddesignlibraryfun.R;

/**
 * Created by desmond on 30/8/15.
 */
public class AvatarImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private final static String TAG = AvatarImageBehavior.class.getSimpleName();
    private final static float MIN_AVATAR_PERCENTAGE_SIZE = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING = 80;

    private final Context mContext;

    private float mAvatarMaxSize;

    private int mStartYPosition;
    private int mFinalYPosition;

    private int mStartHeight;
    private int mFinalHeight;

    private int mStartXPosition;
    private int mFinalXPosition;

    private float mFinalLeftAvatarPadding;
    private float mStartPosition;
    private float mStartToolbarPosition;

    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
        mFinalLeftAvatarPadding = mContext.getResources().getDimension(R.dimen.abc_action_bar_default_padding_start_material);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (child.getY() + (child.getHeight() / 2));

        if (mFinalYPosition == 0)
            mFinalYPosition = dependency.getHeight() / 2;

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (mFinalHeight == 0)
            mFinalHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.image_final_width);

        if (mStartXPosition == 0)
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));

        if (mFinalXPosition == 0)
            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (mFinalHeight / 2);

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY() + (dependency.getHeight()/2);

        final int maxScrollDist = (int) (mStartToolbarPosition - getStatusBarHeight());
        float expandedPercentageFactor = dependency.getY() / maxScrollDist;

        float distYToSubtract = ((mStartYPosition - mFinalYPosition) * (1f - expandedPercentageFactor))
                + (child.getHeight() / 2);

        float distXToSubtract = ((mStartXPosition - mFinalXPosition) * (1f - expandedPercentageFactor))
                + (child.getWidth() / 2);

        float heightToSubtract = ((mStartHeight - mFinalHeight) * (1f - expandedPercentageFactor));

        child.setY(mStartYPosition - distYToSubtract);
        child.setX(mStartXPosition - distXToSubtract);

        int proportionalAvatarSize = (int) (mAvatarMaxSize * expandedPercentageFactor);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (mStartHeight - heightToSubtract);
        lp.height = (int) (mStartHeight - heightToSubtract);
        child.setLayoutParams(lp);

        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resId);
        }

        return result;
    }
}
