package com.saltedfish.chanelview;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by SaltedFish on 2017/11/14.
 * ListView TouchListener 事件分发处理
 */

public abstract class TouchEventHandler implements View.OnTouchListener {
    public boolean dispatchToView = false;
    private boolean isClick = false;
    private int lastAction;
    private float prevX = 0.0f;
    private float prevY = 0.0f;
    private boolean swiping = false;
    private final VelocityTracker velocityTracker = VelocityTracker.obtain();

    public abstract boolean onClick(MotionEvent motionEvent, View view);

    public abstract boolean onDown(MotionEvent motionEvent, View view);

    public abstract boolean onLongClick(MotionEvent motionEvent, View view);

    public abstract boolean onOtherEvent(MotionEvent motionEvent, View view);

    public abstract boolean onSwipeDown(MotionEvent motionEvent, View view, float f);

    public abstract boolean onSwipeFinish(MotionEvent motionEvent, View view, float f);

    public abstract boolean onSwipeLeft(MotionEvent motionEvent, View view, float f);

    public abstract boolean onSwipeRight(MotionEvent motionEvent, View view, float f);

    public abstract boolean onSwipeStart(MotionEvent motionEvent, View view);

    public abstract boolean onSwipeUp(MotionEvent motionEvent, View view, float f);

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        this.lastAction = action;
        if (action == 0) {
            this.velocityTracker.addMovement(motionEvent);
            this.prevX = motionEvent.getRawX();
            this.prevY = motionEvent.getRawY();
            this.isClick = true;
            view.postDelayed(new Runnable() {
                public void run() {
                    if (TouchEventHandler.this.lastAction == 0) {
                        TouchEventHandler.this.dispatchToView = TouchEventHandler.this.onLongClick(motionEvent, view);
                    }
                }
            }, (long) ViewConfiguration.getLongPressTimeout());
            this.dispatchToView = onDown(motionEvent, view);
        } else if (action == 2) {
            this.isClick = false;
            this.velocityTracker.addMovement(motionEvent);
            if (!this.swiping) {
                this.dispatchToView = onSwipeStart(motionEvent, view);
            }
            this.swiping = true;
            float sX = motionEvent.getRawX() - this.prevX;
            float sY = motionEvent.getRawY() - this.prevY;
            if (Math.abs(sX) > Math.abs(sY)) {
                if (sX > 0.0f) {
                    this.dispatchToView = onSwipeRight(motionEvent, view, sX);
                } else if (sX < 0.0f) {
                    this.dispatchToView = onSwipeLeft(motionEvent, view, sX);
                }
            } else if (sY > 0.0f) {
                this.dispatchToView = onSwipeDown(motionEvent, view, sY);
            } else if (sY < 0.0f) {
                this.dispatchToView = onSwipeUp(motionEvent, view, sY);
            }
            this.prevY = motionEvent.getRawY();
            this.prevX = motionEvent.getRawX();
        } else if (action == 1 || (this.swiping && action == 3)) {
            this.velocityTracker.addMovement(motionEvent);
            if (this.swiping) {
                this.velocityTracker.computeCurrentVelocity(10);
                this.dispatchToView = onSwipeFinish(motionEvent, view, this.velocityTracker.getXVelocity());
                this.swiping = false;
            } else if (!this.isClick) {
                this.dispatchToView = onOtherEvent(motionEvent, view);
            } else if (motionEvent.getEventTime() - motionEvent.getDownTime() < ((long) ViewConfiguration.getLongPressTimeout())) {
                this.dispatchToView = onClick(motionEvent, view);
            }
        } else {
            this.dispatchToView = onOtherEvent(motionEvent, view);
        }
        return this.dispatchToView;
    }
}
