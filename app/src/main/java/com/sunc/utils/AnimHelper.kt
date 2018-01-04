package com.sunc.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator

class AnimHelper private constructor() {

    init {
        throw RuntimeException("AnimHelper cannot be initialized!")
    }

    companion object {


        fun zoomOut(view: View) {
            val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
            val set = AnimatorSet()
            set.playTogether(alpha, scaleX, scaleY)
            set.duration = 300
            set.interpolator = AccelerateInterpolator()
            set.start()
        }

        fun zoomIn(view: View) {
            val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
            val set = AnimatorSet()
            set.playTogether(alpha, scaleX, scaleY)
            set.duration = 300
            set.interpolator = AccelerateInterpolator()
            set.start()
        }

        fun setViewX(view: View, originalX: Float, finalX: Float, percent: Float) {
            val calcX = (finalX - originalX) * percent + originalX
            view.x = calcX
        }

        fun setViewY(view: View, originalY: Float, finalY: Float, percent: Float) {
            val calcY = (finalY - originalY) * percent + originalY
            view.y = calcY
        }

        fun scaleView(view: View, originalSize: Float, finalSize: Float, percent: Float) {
            val calcSize = (finalSize - originalSize) * percent + originalSize
            val caleScale = calcSize / originalSize
            view.scaleX = caleScale
            view.scaleY = caleScale
        }
    }
}
