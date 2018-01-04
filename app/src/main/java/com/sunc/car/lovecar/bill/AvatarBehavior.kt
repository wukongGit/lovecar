package com.sunc.car.lovecar.bill

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView

import com.sunc.car.lovecar.R
import com.sunc.utils.AnimHelper

/**
 * Created by long on 2016/7/11.
 * 头像Behavior
 */
class AvatarBehavior(private val mContext: Context, attrs: AttributeSet?) : CoordinatorLayout.Behavior<ImageView>(mContext, attrs) {
    // 整个滚动的范围
    private var mTotalScrollRange: Int = 0
    // AppBarLayout高度
    private var mAppBarHeight: Int = 0
    // AppBarLayout宽度
    private var mAppBarWidth: Int = 0
    // 控件原始大小
    private var mOriginalSize: Int = 0
    // 控件最终大小
    private var mFinalSize: Int = 0
    // 控件最终缩放的尺寸,设置坐标值需要算上该值
    private var mScaleSize: Float = 0.toFloat()
    // 原始x坐标
    private var mOriginalX: Float = 0.toFloat()
    // 最终x坐标
    private var mFinalX: Float = 0.toFloat()
    // 起始y坐标
    private var mOriginalY: Float = 0.toFloat()
    // 最终y坐标
    private var mFinalY: Float = 0.toFloat()
    // ToolBar高度
    private var mToolBarHeight: Int = 0
    // AppBar的起始Y坐标
    private var mAppBarStartY: Float = 0.toFloat()
    // 滚动执行百分比[0~1]
    private var mPercent: Float = 0.toFloat()
    // Y轴移动插值器
    private val mMoveYInterpolator: DecelerateInterpolator
    // X轴移动插值器
    private val mMoveXInterpolator: AccelerateInterpolator
    // 最终变换的视图，因为在5.0以上AppBarLayout在收缩到最终状态会覆盖变换后的视图，所以添加一个最终显示的图片
    private var mFinalView: ImageView? = null
    // 最终变换的视图离底部的大小
    private var mFinalViewMarginBottom: Int = 0


    init {
        mMoveYInterpolator = DecelerateInterpolator()
        mMoveXInterpolator = AccelerateInterpolator()
        if (attrs != null) {
            val a = mContext.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior)
            mFinalSize = a.getDimension(R.styleable.AvatarImageBehavior_finalSize, 0f).toInt()
            mFinalX = a.getDimension(R.styleable.AvatarImageBehavior_finalX, 0f)
            mToolBarHeight = a.getDimension(R.styleable.AvatarImageBehavior_toolBarHeight, 0f).toInt()
            a.recycle()
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: ImageView?, dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: ImageView?, dependency: View?): Boolean {

        if (dependency is AppBarLayout) {
            _initVariables(child, dependency)

            mPercent = (mAppBarStartY - dependency.y) * 1.0f / mTotalScrollRange

            val percentY = mMoveYInterpolator.getInterpolation(mPercent)
            AnimHelper.setViewY(child!!, mOriginalY, mFinalY - mScaleSize, percentY)

            if (mPercent > ANIM_CHANGE_POINT) {
                val scalePercent = (mPercent - ANIM_CHANGE_POINT) / (1 - ANIM_CHANGE_POINT)
                val percentX = mMoveXInterpolator.getInterpolation(scalePercent)
                AnimHelper.scaleView(child, mOriginalSize.toFloat(), mFinalSize.toFloat(), scalePercent)
                AnimHelper.setViewX(child, mOriginalX, mFinalX - mScaleSize, percentX)
            } else {
                AnimHelper.scaleView(child, mOriginalSize.toFloat(), mFinalSize.toFloat(), 0f)
                AnimHelper.setViewX(child, mOriginalX, mFinalX - mScaleSize, 0f)
            }
            if (mFinalView != null) {
                if (percentY == 1.0f) {
                    // 滚动到顶时才显示
                    mFinalView!!.visibility = View.VISIBLE
                } else {
                    mFinalView!!.visibility = View.GONE
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && dependency is CollapsingToolbarLayout) {
            // 大于5.0才生成新的最终的头像，因为5.0以上AppBarLayout会覆盖变换后的头像
            if (mFinalView == null && mFinalSize != 0 && mFinalX != 0f && mFinalViewMarginBottom != 0) {
                mFinalView = ImageView(mContext)
                mFinalView!!.visibility = View.GONE
                // 添加为CollapsingToolbarLayout子视图
                dependency.addView(mFinalView)
                val params = mFinalView!!.layoutParams as FrameLayout.LayoutParams
                // 设置大小
                params.width = mFinalSize
                params.height = mFinalSize
                // 设置位置，最后显示时相当于变换后的头像位置
                params.gravity = Gravity.BOTTOM
                params.leftMargin = mFinalX.toInt()
                params.bottomMargin = mFinalViewMarginBottom
                mFinalView!!.layoutParams = params
                mFinalView!!.setImageDrawable(child!!.drawable)
            } else {
                mFinalView!!.setImageDrawable(child!!.drawable)
            }
        }

        return true
    }

    /**
     * 初始化变量
     *
     * @param child
     * @param dependency
     */
    private fun _initVariables(child: ImageView?, dependency: View) {
        if (mAppBarHeight == 0) {
            mAppBarHeight = dependency.height
            mAppBarStartY = dependency.y
        }
        if (mTotalScrollRange == 0) {
            mTotalScrollRange = (dependency as AppBarLayout).totalScrollRange
        }
        if (mOriginalSize == 0) {
            mOriginalSize = child!!.width
        }
        if (mFinalSize == 0) {
            mFinalSize = mContext.resources.getDimensionPixelSize(R.dimen.avatar_final_size)
        }
        if (mAppBarWidth == 0) {
            mAppBarWidth = dependency.width
        }
        if (mOriginalX == 0f) {
            mOriginalX = child!!.x
        }
        if (mFinalX == 0f) {
            mFinalX = mContext.resources.getDimensionPixelSize(R.dimen.avatar_final_x).toFloat()
        }
        if (mOriginalY == 0f) {
            mOriginalY = child!!.y
        }
        if (mFinalY == 0f) {
            if (mToolBarHeight == 0) {
                mToolBarHeight = mContext.resources.getDimensionPixelSize(R.dimen.toolbar_size)
            }
            mFinalY = (mToolBarHeight - mFinalSize) / 2 + mAppBarStartY
        }
        if (mScaleSize == 0f) {
            mScaleSize = (mOriginalSize - mFinalSize) * 1.0f / 2
        }
        if (mFinalViewMarginBottom == 0) {
            mFinalViewMarginBottom = (mToolBarHeight - mFinalSize) / 2
        }
    }

    companion object {

        // 缩放动画变化的支点
        private val ANIM_CHANGE_POINT = 0.2f
    }
}
