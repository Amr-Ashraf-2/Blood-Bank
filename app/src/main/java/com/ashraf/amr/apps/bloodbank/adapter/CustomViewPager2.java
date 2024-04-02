package com.ashraf.amr.apps.bloodbank.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.ashraf.amr.apps.bloodbank.utils.SwipeDirection;

public class CustomViewPager2 extends ViewPager {

    private float initialXValue;
    private SwipeDirection direction;
    private boolean isPagingEnabled = true;

    public CustomViewPager2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.ALL;
    }

    public CustomViewPager2(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return this.isPagingEnabled && super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return this.isPagingEnabled && super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if(this.direction == SwipeDirection.ALL) return true;

        if(direction == SwipeDirection.NONE )//disable any swipe
            return false;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float diffX = event.getX() - initialXValue;
            if (diffX > 0 && direction == SwipeDirection.RIGHT) {
                // swipe from left to right detected
                return false;
            } else if (diffX < 0 && direction == SwipeDirection.LEFT) {
                // swipe from right to left detected
                return false;
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

}
