package com.sunc.mvp.presenter

import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.User
import com.sunc.mvp.contract.CarContract
import com.sunc.mvp.contract.OilContract
import com.sunc.mvp.model.CarModel
import com.sunc.mvp.model.OilModel
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class CarPresenter
@Inject constructor(private val mModel: CarModel,
                    private val mView: CarContract.View): CarContract.Presenter {
    override fun deleteCar(car: Car) {
        mModel.deleteCar(car.objectId, mView)
    }

    override fun addCar(car: Car) {
        mModel.addCar(App.instance.getUser()!!, car.name, mView)
    }

    override fun editCar(car: Car) {
        mModel.editCar(car.objectId, car.name, mView)
    }

    override fun getData(user: User) {
        mModel.getData(user, mView)
    }
}