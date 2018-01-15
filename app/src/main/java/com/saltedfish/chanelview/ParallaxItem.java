package com.saltedfish.chanelview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by SaltedFish on 2018/1/14.
 * 视差效果Item
 */

public class ParallaxItem extends RelativeLayout {
    public static final float EPSILON = 1.0E-4f;

    public enum Anim {
        NONE,
        OPENING,
        FOLDING
    }

    public static int deviceWidth;
    protected float parallax = 0.0f;
    protected int parallaxOffset = 0;
    private Anim prevAnim = Anim.NONE;
    protected static int openedHeight = 0;
    protected static int foldedHeight = 0;
    private Anim scrollDirection = Anim.NONE;

    private ImageView ivMask;
    private ImageView ivContent;

    public ParallaxItem(Context context) {
        this(context, null);
    }

    public ParallaxItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init(context);
    }

    private void init(Context context) {
        deviceWidth = DisplayUtil.getDeviceWidth(context);
        openedHeight = (int) ((DisplayUtil.getDeviceHight(context) - DisplayUtil.getStatusBarHeight(context)) * 0.6);
        foldedHeight = (openedHeight / 3);
        this.prevAnim = Anim.NONE;
        this.scrollDirection = Anim.NONE;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivMask = (ImageView) findViewById(R.id.iv_mask);
        ivContent = (ImageView) findViewById(R.id.iv_content);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredHeight = (int) Math.round(interpolate((float) foldedHeight, (float) openedHeight, this.parallax));
        setMeasuredDimension(deviceWidth, desiredHeight);
        ivMask.measure(MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.UNSPECIFIED));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void parallax(float percentage) {
        if (percentage < 0.0f) {
            percentage = 0.0f;
        } else if (percentage > 1.0f) {
            percentage = 1.0f;
        }
        if (Math.abs(this.parallax - percentage) > EPSILON && this.scrollDirection == Anim.NONE) {
            if (this.parallax - percentage > 0.0f) {
                this.prevAnim = this.scrollDirection;
                this.scrollDirection = Anim.FOLDING;
            } else if (this.parallax - percentage < 0.0f) {
                this.prevAnim = this.scrollDirection;
                this.scrollDirection = Anim.OPENING;
            }
        }
        parallax = percentage;
        int h = (int) Math.round(interpolate((float) foldedHeight, (float) openedHeight, this.parallax));
        ivMask.getLayoutParams().height = h;
        requestLayout();
    }

    protected double interpolate(float from, float to, float t) {
        return (double) (((1.0f - t) * from) + (to * t));
    }

    public void setViewHeight(int height) {
        openedHeight = height;
    }

    public float getParallax() {
        return this.parallax;
    }

    public void setParallax(float parallax) {
        this.parallax = parallax;
    }

    public void setParallaxOffset(int offset) {
        this.parallaxOffset = offset;
    }

}
