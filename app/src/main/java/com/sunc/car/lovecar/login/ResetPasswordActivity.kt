package com.sunc.car.lovecar.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.bmob.v3.exception.BmobException
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityResetPasswordBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.di.component.LoginModule
import com.sunc.mvp.contract.LoginContract
import com.sunc.mvp.presenter.LoginPresenter
import com.sunc.utils.AndroidUtils
import com.sunc.utils.Strings
import kotlinx.android.synthetic.main.layout_title_bar.*
import javax.inject.Inject

class ResetPasswordActivity : BaseBindingActivity<ActivityResetPasswordBinding>(), LoginContract.View {
    @Inject lateinit var mPresenter: LoginPresenter
    override fun loginCallBack(o: Any, e: BmobException?) {
        showProgress(false)
        if (e == null) {
            if (o == -1) {
                toast(getString(R.string.the_mobile_has_not_registered))
            } else {
                toast(getString(R.string.reset_password_success))
                finish()
            }
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityResetPasswordBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
    }

    override fun initView() {
        getMainComponent().plus(LoginModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.forget_password)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener {
                finish()
            }
            mobileSignInButton.setOnClickListener { attemptReset() }
            tvRequest.setOnClickListener {
                attemptSms()
            }
        }
    }

    private fun attemptSms() {
        if (AndroidUtils.isFastDoubleClick()) {
            return
        }
        with(mBinding) {
            tvMobile.error = null
            tvPassword.error = null

            val mobileStr = tvMobile.text.toString()
            val passwordStr = tvPassword.text.toString()
            var cancel = false
            var focusView: View? = null

            // Check for a valid email address.
            if (TextUtils.isEmpty(mobileStr)) {
                tvMobile.error = getString(R.string.error_mobile_required)
                focusView = tvMobile
                cancel = true
            } else if (!Strings.isMobilePhone(mobileStr)) {
                tvMobile.error = getString(R.string.error_invalid_mobile)
                focusView = tvMobile
                cancel = true
            }
            if (cancel) {
                focusView?.requestFocus()
                return
            }

            if (TextUtils.isEmpty(passwordStr)) {
                tvPassword.error = getString(R.string.error_password_required)
                focusView = tvPassword
                cancel = true
            } else if (!Strings.isLongerEnough(passwordStr, 4)) {
                tvPassword.error = getString(R.string.error_invalid_password)
                focusView = tvPassword
                cancel = true
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView?.requestFocus()
            } else {
                mPresenter.requestSms(mobileStr)
            }
        }
    }


    private fun attemptReset() {
        if (AndroidUtils.isFastDoubleClick()) {
            return
        }
        if (!AndroidUtils.isNetworkConnected(this)) {
            toast(getString(R.string.network_error))
            return
        }
        with(mBinding) {
            tvMobile.error = null
            tvPassword.error = null
            tvSms.error = null

            val mobileStr = tvMobile.text.toString()
            val passwordStr = tvPassword.text.toString()
            val smsStr = tvSms.text.toString()
            var cancel = false
            var focusView: View? = null

            // Check for a valid email address.
            if (TextUtils.isEmpty(mobileStr)) {
                tvMobile.error = getString(R.string.error_mobile_required)
                focusView = tvMobile
                cancel = true
            } else if (!Strings.isMobilePhone(mobileStr)) {
                tvMobile.error = getString(R.string.error_invalid_mobile)
                focusView = tvMobile
                cancel = true
            }
            if (cancel) {
                focusView?.requestFocus()
                return
            }

            if (TextUtils.isEmpty(passwordStr)) {
                tvPassword.error = getString(R.string.error_password_required)
                focusView = tvPassword
                cancel = true
            } else if (!Strings.isLongerEnough(passwordStr, 4)) {
                tvPassword.error = getString(R.string.error_invalid_password)
                focusView = tvPassword
                cancel = true
            }
            if (cancel) {
                focusView?.requestFocus()
                return
            }

            if (TextUtils.isEmpty(smsStr)) {
                tvSms.error = getString(R.string.prompt_sms)
                focusView = tvSms
                cancel = true
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView?.requestFocus()
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true)
                mPresenter.resetPassword(smsStr, passwordStr)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        with(mBinding) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

                loginForm.visibility = if (show) View.GONE else View.VISIBLE
                loginForm.animate()
                        .setDuration(shortAnimTime)
                        .alpha((if (show) 0 else 1).toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                loginForm.visibility = if (show) View.GONE else View.VISIBLE
                            }
                        })

                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
                loginProgress.animate()
                        .setDuration(shortAnimTime)
                        .alpha((if (show) 1 else 0).toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
                            }
                        })
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
                loginForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        }
    }

}
