package com.loyalie.connectre.custom_views

import android.animation.Animator

abstract class AnimationEndListener : Animator.AnimatorListener {

    override fun onAnimationRepeat(p0: Animator?) {

    }

    override fun onAnimationCancel(p0: Animator?) {

    }

    override fun onAnimationStart(p0: Animator?) {

    }

    override abstract fun onAnimationEnd(p0: Animator?)
}