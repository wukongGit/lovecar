package com.sunc.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/9/23.
 */

public class TouchIndicator extends View {
    TouchIndicatorListener mTouchIndicatorListener;

    public TouchIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTouchIndicatorListener(TouchIndicatorListener listener) {
        mTouchIndicatorListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(mTouchIndicatorListener != null) {
            mTouchIndicatorListener.touched();
        }
        return false;
    }

    public interface TouchIndicatorListener {
        void touched();
    }
}
