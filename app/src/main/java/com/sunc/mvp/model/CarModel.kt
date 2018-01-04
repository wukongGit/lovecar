package com.sunc.mvp.model

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.User
import com.sunc.mvp.contract.CarContract
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class CarModel
@Inject constructor(private val loginModel: LoginModel): CarContract.Model {
    override fun deleteCar(id: String, view: CarContract.View) {
        loginModel.deleteCar(id, object : UpdateListener() {
            override fun done(p0: BmobException?) {
                if (p0 == null) {
                    view.carUpdated()
                } else {
                    view.onError(p0.errorCode.toString() + "：" + p0.message!!)
                }
            }
        })
    }

    override fun addCar(user: User, name: String, view: CarContract.View) {
        loginModel.addCar(user, name, object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if (p1 == null) {
                    view.carUpdated()
                } else {
                    view.onError(p1.errorCode.toString() + "：" + p1.message!!)
                }
            }
        })
    }

    override fun editCar(id: String, name: String, view: CarContract.View) {
        loginModel.updateCar(id, name, object : UpdateListener() {
            override fun done(p0: BmobException?) {
                if (p0 == null) {
                    view.carUpdated()
                } else {
                    view.onError(p0.errorCode.toString() + "：" + p0.message!!)
                }
            }

        })
    }

    override fun getData(user: User, view: CarContract.View) {
        loginModel.getCarList(user, object : FindListener<Car>() {
            override fun done(p0: MutableList<Car>?, p1: BmobException?) {
                if (p1 == null) {
                    view.setData(p0!!)
                } else {
                    view.onError(p1.errorCode.toString() + "：" + p1.message!!)
                }
            }
        })
    }

}