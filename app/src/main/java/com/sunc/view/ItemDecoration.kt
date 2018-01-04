package com.sunc.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

import com.sunc.car.lovecar.R
import com.sunc.utils.DimenUtils

@Suppress("DEPRECATION")
class ItemDecoration(private val mContext: Context) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable

    init {
        val colorDrawable = ColorDrawable()
        colorDrawable.color = mContext.resources.getColor(R.color.dividerX1)
        mDivider = colorDrawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    private fun getSpanCount(parent: RecyclerView?): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent!!.layoutManager
        if (layoutManager is GridLayoutManager) {

            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                    .spanCount
        }
        return spanCount
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin + DimenUtils.dp2px(mContext, 1f)
            val top = child.bottom + params.bottomMargin
            val bottom = top + DimenUtils.dp2px(mContext, 1f)
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val left = child.right + params.rightMargin
            val right = left + DimenUtils.dp2px(mContext, 1f)

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun isLastColum(parent: RecyclerView, pos: Int, spanCount: Int,
                            childCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)
            // 如果是最后一列，则不需要绘制右边v
            {
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)
                // 如果是最后一列，则不需要绘制右边
                {
                    return true
                }
            } else {
                var count = childCount - childCount % spanCount
                if (pos >= count)
                // 如果是最后一列，则不需要绘制右边
                    return true
            }
        }
        return false
    }

    private fun isLastRaw(parent: RecyclerView, pos: Int, spanCount: Int,
                          childCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            var count = childCount - childCount % spanCount
            if (pos >= count)
            // 如果是最后一行，则不需要绘制底部
                return true
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                var count = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= count)
                    return true
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int,
                                parent: RecyclerView?) {
        val spanCount = getSpanCount(parent)
        val childCount = parent!!.adapter.itemCount
        if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
            // 如果是最后一行，则不需要绘制底部
            outRect.set(0, 0, DimenUtils.dp2px(mContext, 1f), 0)
        } else if (isLastColum(parent, itemPosition, spanCount, childCount))
        // 如果是最后一列，则不需要绘制右边
        {
            outRect.set(0, 0, 0, DimenUtils.dp2px(mContext, 1f))
        } else {
            outRect.set(0, 0, DimenUtils.dp2px(mContext, 1f), DimenUtils.dp2px(mContext, 1f))
        }
    }
}
