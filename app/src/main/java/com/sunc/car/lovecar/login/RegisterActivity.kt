package com.sunc.car.lovecar.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.bmob.v3.exception.BmobException
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.User
import com.sunc.car.lovecar.databinding.ActivityRegisterBinding
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
class RegisterActivity : BaseBindingActivity<ActivityRegisterBinding>(), LoginContract.View {
    @Inject lateinit var mPresenter: LoginPresenter

    override fun loginCallBack(o: Any, e: BmobException?) {
        showProgress(false)
        if (e == null) {
            if (o is User) {
                toast(getString(R.string.register_success))
                mPresenter.addCar(o, this@RegisterActivity.getString(R.string.my_love_car).plus("1"))
            }
            if (o is String) {
                finish()
            }
        } else {
            toast(e.message!!)
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityRegisterBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_register)
    }

    override fun initView() {
        getMainComponent().plus(LoginModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.register)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener {
                finish()
            }
            mobileSignInButton.setOnClickListener { attemptRegister() }
            tvAgree.setOnClickListener {
                var intent = Intent(this@RegisterActivity, GuideActivity::class.java)
                intent.putExtra(GuideActivity.PARAM_TITLE, getString(R.string.services))
                intent.putExtra(GuideActivity.PARAM_FILE, "guide.json")
                startActivity(intent)
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptRegister() {
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
            tvPasswordAgain.error = null

            val mobileStr = tvMobile.text.toString()
            val passwordStr = tvPassword.text.toString()
            val passwordAgainStr = tvPasswordAgain.text.toString()
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

            if (TextUtils.isEmpty(passwordAgainStr)) {
                tvPasswordAgain.error = getString(R.string.prompt_password_again)
                focusView = tvPasswordAgain
                cancel = true
            } else if (passwordAgainStr != passwordStr) {
                tvPasswordAgain.error = getString(R.string.error_invalid_password_not_equal)
                focusView = tvPasswordAgain
                cancel = true
            }
            if (cancel) {
                focusView?.requestFocus()
                return
            }

            if (!cbAgree.isChecked) {
                toast(getString(R.string.please_read_and_agree))
                return
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView?.requestFocus()
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true)
                mPresenter.register(mobileStr, passwordStr)
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