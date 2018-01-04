package com.sunc.mvp.contract

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.car.lovecar.bmob.User
import org.json.JSONArray

/**
 * Created by Administrator on 2017/11/14.
 */
interface LoginContract {
    interface View {
        fun  loginCallBack(o: Any, e: BmobException?)
    }

    interface Model {
        fun register(username: String, password: String, view: LoginContract.View)
        fun resetPassword(sms: String, password: String, view: LoginContract.View)
        fun requestSms(mobile: String, view: LoginContract.View)
        fun login(username: String, password: String, view: LoginContract.View)
        fun logout()
        fun getCarList(user: User, listener: FindListener<Car>)
        fun addCar(user: User, name: String, listener: SaveListener<String>)
        fun updateCar(id: String, name: String, listener: UpdateListener)
        fun deleteCar(id: String, listener: UpdateListener)
        fun oilStatistic(car: Car, listener: QueryListener<JSONArray>)
        fun oilDetail(car: Car, listener: FindListener<Oil>)
        fun billList(page:Int, limit: Int, car: Car, listener: FindListener<Bill>)
        fun billOutStatistic(car: Car, listener: QueryListener<JSONArray>)
        fun billInStatistic(car: Car, listener: QueryListener<JSONArray>)
        fun billMonthStatistic(car: Car, listener: QueryListener<JSONArray>)
        fun billOutTotalStatistic(car: Car, listener: QueryListener<JSONArray>)
        fun billInTotalStatistic(car: Car, listener: QueryListener<JSONArray>)
        fun addBill(bill: Bill, listener: SaveListener<String>)
        fun addOil(oil: Oil, listener: SaveListener<String>)
    }

    interface Presenter {
        fun login(username: String, password: String)
        fun register(username: String, password: String)
        fun resetPassword(sms: String, password: String)
        fun requestSms(mobile: String)
        fun addCar(user: User, name: String)
        fun getCarList(user: User)
    }
}