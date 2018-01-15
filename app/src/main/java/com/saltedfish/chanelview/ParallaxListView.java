package com.saltedfish.chanelview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by SaltedFish on 2018/1/14.
 * 滑动视差效果ListView
 */

public class ParallaxListView extends ListView {
    private static final int MAX_SCROLL_DURATION = 300;

    private int mSelectedIndex = 0;
    private int scrollingToPosition = -1;
    private boolean scrollDownEnabled = true;
    private MotionEvent lockedSwipeEvent = null;
    private ScrollDirection swipe = ScrollDirection.NONE;

    public enum ScrollDirection {
        NONE,
        UP,
        DOWN
    }

    public ParallaxListView(Context context) {
        this(context, null);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnScrollListener(new ParallaxScroll());
        setOnTouchListener(new ParallaxTouchEvent());
    }

    private void gotoPrev() {
        int firstVisible = getFirstVisiblePosition();
        View v = getChildAt(0);
        if (v != null) {
            smoothScrollToPositionFromTop(firstVisible, 0, Math.abs(((-v.getTop()) * 300) / 600));
        }
    }

    private void gotoNext() {
        int i;
        int firstVisible = getFirstVisiblePosition();
        if (this.scrollDownEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        View v = getChildAt(i);
        if (v == null) {
            return;
        }
        if (this.scrollDownEnabled) {
            smoothScrollToPositionFromTop(firstVisible + 1, 0, Math.abs((v.getTop() * 300) / 600));
        }
    }

    class ParallaxTouchEvent extends TouchEventHandler {

        @Override
        public boolean onClick(MotionEvent motionEvent, View view) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent, View view) {
            return false;
        }

        @Override
        public boolean onLongClick(MotionEvent motionEvent, View view) {
            return false;
        }

        @Override
        public boolean onOtherEvent(MotionEvent motionEvent, View view) {
            return false;
        }

        @Override
        public boolean onSwipeDown(MotionEvent motionEvent, View view, float distance) {
            if (Math.abs(distance) > 3) {
                ParallaxListView.this.swipe = ScrollDirection.UP;
            }
            if (!ParallaxListView.this.scrollDownEnabled && ParallaxListView.this.lockedSwipeEvent != null && motionEvent.getY() <= ParallaxListView.this.lockedSwipeEvent.getY()) {
                return true;
            }
            ParallaxListView.this.lockedSwipeEvent = null;
            return false;
        }

        @Override
        public boolean onSwipeFinish(MotionEvent motionEvent, View view, float f) {
            switch (ParallaxListView.this.swipe) {
                case UP:
                    ParallaxListView.this.gotoPrev();
                    break;
                case DOWN:
                    ParallaxListView.this.gotoNext();
                    break;
            }
            return false;
        }

        @Override
        public boolean onSwipeLeft(MotionEvent motionEvent, View view, float f) {
            return true;
        }

        @Override
        public boolean onSwipeRight(MotionEvent motionEvent, View view, float f) {
            return true;
        }

        @Override
        public boolean onSwipeStart(MotionEvent motionEvent, View view) {
            ParallaxListView.this.swipe = ScrollDirection.NONE;
            return false;
        }

        @Override
        public boolean onSwipeUp(MotionEvent motionEvent, View view, float distance) {
            if (!(ParallaxListView.this.swipe == ScrollDirection.DOWN || ParallaxListView.this.scrollDownEnabled || ParallaxListView.this.lockedSwipeEvent != null)) {
                ParallaxListView.this.lockedSwipeEvent = MotionEvent.obtain(motionEvent);
            }
            if (Math.abs(distance) > 3) {
                ParallaxListView.this.swipe = ScrollDirection.DOWN;
            }
            return !ParallaxListView.this.scrollDownEnabled;
        }
    }

    class ParallaxScroll implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            ParallaxAdapter a = (ParallaxAdapter) getAdapter();
            if (a != null) {
                a.setFirstVisibleItem(firstVisibleItem);
            }
            View v;
            for (int i = 0; i < visibleItemCount; i++) {
                v = getChildAt(i);
                if (v != null && (v instanceof ParallaxItem)) {
                    ParallaxItem mi = (ParallaxItem) v;
                    if (v.getBottom() <= ParallaxItem.openedHeight || v.getTop() < 0) {
                        mi.setParallaxOffset(0);
                        if (mi.getParallax() != 1) {
                            mi.parallax(1.0f);
                        }
                    } else if (v.getTop() < ParallaxItem.openedHeight) {
                        mi.setParallaxOffset(0);
                        mi.parallax(1.0f - (((float) v.getTop()) / ((float) ParallaxItem.openedHeight)));
                    } else {
                        mi.setParallaxOffset(Math.round(((((float) (v.getTop() - ParallaxItem.openedHeight)) / ((float) getMeasuredHeight())) * ((float) ParallaxItem.foldedHeight)) * 0.4f));
                        if (mi.getParallax() != 0) {
                            mi.parallax(0.0f);
                        }
                    }
                }
            }
        }
    }

}
