package com.sunc.car.lovecar.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.databinding.DataBindingUtil
import cn.bmob.v3.exception.BmobException
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.User
import com.sunc.car.lovecar.databinding.ActivityLoginBinding
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.di.component.LoginModule
import com.sunc.mvp.contract.LoginContract
import com.sunc.mvp.presenter.LoginPresenter
import com.sunc.utils.AndroidUtils
import com.sunc.utils.Strings
import kotlinx.android.synthetic.main.layout_title_bar.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseBindingActivity<ActivityLoginBinding>(), LoginContract.View {
    @Inject lateinit var mPresenter: LoginPresenter

    override fun loginCallBack(o: Any, e: BmobException?) {
        showProgress(false)
        if (e == null) {
            if (o is User) {
                App.instance.setUser(o)
                mPresenter.getCarList(o)
            }
            if (o is MutableList<*>) {
                if (o.size > 0) {
                    App.instance.setCar(o[0] as Car)
                    EventBus.bus().post(NotifyType(NotifyType.EVENT_CAR_CHANGE))
                }
                finish()
            }
        } else {
            with(mBinding) {
                password.error = e.errorCode.toString() + "：" + e.message
                password.requestFocus()
            }
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityLoginBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initView() {
        getMainComponent().plus(LoginModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.login)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener {
                finish()
            }

            password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin()
                    return@OnEditorActionListener true
                }
                false
            })
            mobileSignInButton.setOnClickListener { attemptLogin() }
            tvRegister.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
            tvForget.setOnClickListener { startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java)) }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (AndroidUtils.isFastDoubleClick()) {
            return
        }
        if (!AndroidUtils.isNetworkConnected(this)) {
            toast(getString(R.string.network_error))
            return
        }
        with(mBinding) {
            mobile.error = null
            password.error = null

            val mobileStr = mobile.text.toString()
            val passwordStr = password.text.toString()
            var cancel = false
            var focusView: View? = null

            // Check for a valid mobile address.
            if (TextUtils.isEmpty(mobileStr)) {
                mobile.error = getString(R.string.error_mobile_required)
                focusView = mobile
                cancel = true
            } else if (!Strings.isMobilePhone(mobileStr)) {
                mobile.error = getString(R.string.error_invalid_mobile)
                focusView = mobile
                cancel = true
            }
            if (cancel) {
                focusView?.requestFocus()
                return
            }

            if (TextUtils.isEmpty(passwordStr)) {
                password.error = getString(R.string.error_password_required)
                focusView = password
                cancel = true
            } else if (!Strings.isLongerEnough(passwordStr, 4)) {
                password.error = getString(R.string.error_invalid_password)
                focusView = password
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
                mPresenter.login(mobileStr, passwordStr)
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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
