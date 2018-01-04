package com.sunc.mvp.contract

import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.User

/**
 * Created by Administrator on 2017/11/14.
 */
interface CarContract {
    interface View {
        fun setData(results: List<Car>)
        fun carUpdated()
        fun onError(msg:String)
    }

    interface Model {
        fun addCar(user: User, name: String, view: CarContract.View)
        fun editCar(id: String, name: String, view: CarContract.View)
        fun deleteCar(id: String, view: CarContract.View)
        fun getData(user: User, view: CarContract.View)
    }

    interface Presenter {
        fun addCar(car: Car)
        fun editCar(car: Car)
        fun deleteCar(car: Car)
        fun getData(user: User)
    }
}