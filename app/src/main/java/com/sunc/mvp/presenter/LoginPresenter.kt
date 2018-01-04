package com.sunc.mvp.presenter

import android.util.Log
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.User
import com.sunc.mvp.contract.LoginContract
import com.sunc.mvp.model.LoginModel
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class LoginPresenter
@Inject constructor(private val mModel: LoginModel,
                    private val mView: LoginContract.View): LoginContract.Presenter {
    val TAG = "LoginPresenter"
    override fun getCarList(user: User) {
        mModel.getCarList(user, object : FindListener<Car>() {
            override fun done(p0: MutableList<Car>?, p1: BmobException?) {
                Log.d(TAG, "getCarList===" + "p0:" + p0?.size + ", p1:" + p1?.message)
                if (p1 == null) {
                    mView.loginCallBack(p0!!, p1)
                } else {
                    mView.loginCallBack("", p1)
                }
            }
        })
    }

    override fun addCar(user: User, name: String) {
        mModel.addCar(user, name, object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                Log.d(TAG, "addCar===" + "p0:" + p0 + ", p1:" + p1?.message)
                if (p1 == null) {
                    mView.loginCallBack(p0!!, p1)
                } else {
                    mView.loginCallBack("", p1)
                }
            }
        })
    }

    override fun requestSms(mobile: String) {
        mModel.requestSms(mobile, mView)
    }

    override fun resetPassword(sms: String, password: String) {
        mModel.resetPassword(sms, password, mView)
    }

    override fun register(username: String, password: String) {
        mModel.register(username, password, mView)
    }

    override fun login(username: String, password: String) {
        mModel.login(username, password, mView)
    }
}