package com.sunc.di.component

import com.sunc.car.lovecar.login.LoginActivity
import com.sunc.car.lovecar.login.RegisterActivity
import com.sunc.car.lovecar.login.ResetPasswordActivity
import com.sunc.mvp.contract.LoginContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(LoginModule::class))
interface LoginComponent {
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
    fun inject(activity: ResetPasswordActivity)
}

@Module
class LoginModule(private val mView: LoginContract.View){
    @Provides
    fun getView() = mView
}