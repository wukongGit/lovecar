package com.sunc.view;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SizeDivider extends FlexibleDivider {
    private static final int DEFAULT_SIZE = 2;
    protected SizeProvider mSizeProvider;

    public SizeDivider() {
        mSizeProvider = new SimpleSizeProvider();
    }

    public SizeDivider(SizeProvider provider) {
        this.mSizeProvider = provider;
    }

    public SizeDivider(SizeProvider sizeProvider, MarginProvider marginProvider,
                       VisibilityProvider visibilityProvider) {
        super(marginProvider, visibilityProvider);
        mSizeProvider = sizeProvider;
    }

    @Override
    void draw(Canvas c, RecyclerView parent, int position, View child, boolean forFix) {

    }

    @Override
    protected int getDividerSize(int position, RecyclerView parent) {
        return mSizeProvider.dividerSize(position, parent);
    }

    /**
     * Interface for controlling divider size
     */
    public interface SizeProvider {

        /**
         * Returns size value of divider.
         * Height for horizontal divider, width for vertical divider
         *
         * @param position Divider position
         * @param parent   RecyclerView
         * @return Size of divider
         */
        public int dividerSize(int position, RecyclerView parent);
    }

    public static class SimpleSizeProvider implements SizeProvider {
        @Override
        public int dividerSize(int position, RecyclerView parent) {
            return DEFAULT_SIZE;
        }
    }
}
