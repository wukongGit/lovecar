package com.sunc.mvp.model

import android.util.Log
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.car.lovecar.bmob.User
import com.sunc.mvp.contract.LoginContract
import javax.inject.Inject
import org.json.JSONArray
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Car
import com.sunc.utils.Md5


/**
 * Created by Administrator on 2017/11/14.
 */
class LoginModel
@Inject constructor(): LoginContract.Model {
    //val TAG = "LoginModel"

    override fun addBill(bill: Bill, listener: SaveListener<String>) {
        bill.save(listener)
    }

    override fun addOil(oil: Oil, listener: SaveListener<String>) {
        oil.save(listener)
    }
    override fun billList(page:Int, limit: Int, car: Car, listener: FindListener<Bill>) {
        val query = BmobQuery<Bill>()
        query.addWhereEqualTo("car", car)
        query.setLimit(limit)
        query.setSkip(page * limit)
        query.order("-createdAt")
        query.findObjects(listener)
    }

    override fun billOutStatistic(car: Car, listener: QueryListener<JSONArray>) {
        val query = BmobQuery<Bill>()
        query.addWhereEqualTo("car", car)
        query.addWhereLessThan("value", 0)
        query.sum(arrayOf("value"))
        query.findStatistics(Bill::class.java, listener)
    }

    override fun billInStatistic(car: Car, listener: QueryListener<JSONArray>) {
        val query = BmobQuery<Bill>()
        query.addWhereEqualTo("car", car)
        query.addWhereGreaterThan("value", 0)
        query.sum(arrayOf("value"))
        query.findStatistics(Bill::class.java, listener)
    }

    override fun billMonthStatistic(car: Car, listener: QueryListener<JSONArray>) {
        val query = BmobQuery<Bill>()
        query.addWhereEqualTo("car", car)
        query.sum(arrayOf("value"))
        query.groupby(arrayOf("month"))
        query.findStatistics(Bill::class.java, listener)
    }

    override fun billOutTotalStatistic(car: Car, listener: QueryListener<JSONArray>) {
        val query = BmobQuery<Bill>()
        query.addWhereEqualTo("car", car)
        query.addWhereLessThan("value", 0)
        query.sum(arrayOf("value"))
        query.groupby(arrayOf("type"))
        query.findStatistics(Bill::class.java, listener)
    }

    override fun billInTotalStatistic(car: Car, listener: QueryListener<JSONArray>) {
        val query = BmobQuery<Bill>()
        query.addWhereEqualTo("car", car)
        query.addWhereGreaterThan("value", 0)
        query.sum(arrayOf("value"))
        query.groupby(arrayOf("type"))
        query.findStatistics(Bill::class.java, listener)
    }

    override fun updateCar(id: String, name: String, listener: UpdateListener) {
        var query = Car()
        query.name = name
        query.update(id, listener)
    }

    override fun addCar(user: User, name: String, listener: SaveListener<String>) {
        val query = Car()
        query.user = user
        query.name = name
        query.save(listener)
    }

    override fun deleteCar(id: String, listener: UpdateListener) {
        val car = Car()
        car.objectId = id
        car.delete(listener)
    }

    override fun getCarList(user: User, listener: FindListener<Car>) {
        var query = BmobQuery<Car>()
        query.addWhereEqualTo("user", BmobPointer(user))
        query.findObjects(listener)
    }

    override fun requestSms(mobile: String, view: LoginContract.View) {
        if (mobile.isEmpty()) {
            return
        }
        val query = BmobQuery<User>()
        query.addWhereEqualTo("mobilePhoneNumber", mobile)
        query.findObjects(object : FindListener<User>() {
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if (p1 == null) {
                    if (p0 != null && !p0.isEmpty()) {
                        BmobSMS.requestSMSCode(mobile, "汽车生活", object : QueryListener<Int>() {
                            override fun done(smsId: Int?, ex: BmobException?) {
                                //Log.d(TAG, "requestSms===" + smsId?.toString() + ", ex:" + ex?.message)
                                if (ex == null) {//验证码发送成功
                                    Log.i("smsId", "短信id：" + smsId!!)//用于查询本次短信发送详情
                                }
                            }
                        })
                    } else {
                        //未注册
                        view.loginCallBack(-1, null)
                    }
                } else {
                    view.loginCallBack("", p1)
                }
            }
        })


    }

    override fun resetPassword(sms: String, password: String, view: LoginContract.View) {
        BmobUser.resetPasswordBySMSCode(sms, Md5.MD5(password), object : UpdateListener() {
            override fun done(e: BmobException?) {
                //Log.d(TAG, "resetPassword===" + e?.message)
                view.loginCallBack("", e)
            }
        })
    }

    override fun register(username: String, password: String, view: LoginContract.View) {
        val user = User()
        user.mobilePhoneNumber = username         //设置手机号码（必填）
        user.mobilePhoneNumberVerified = true
        user.username = username                  //设置用户名，如果没有传用户名，则默认为手机号码
        user.setPassword(Md5.MD5(password))                 //设置用户密码
        user.signUp(object : SaveListener<User?>() {
            override fun done(p0: User?, p1: BmobException?) {
                //Log.d(TAG, "register===" + "p0:" + p0?.username + ", p1:" + p1?.message)
                if (p1 == null) {
                    view.loginCallBack(p0!!, p1)
                } else {
                    view.loginCallBack(user, p1)
                }
            }
        })
    }

    override fun logout() {
        BmobUser.logOut()
    }

    override fun login(username: String, password: String, view: LoginContract.View) {
        val user = User()
        user.username = username
        user.setPassword(Md5.MD5(password))
        user.login(object : SaveListener<User?>() {
            override fun done(user: User?, e: BmobException?) {
                //Log.d(TAG, "login===" + "user:" + user?.username + ", p1:" + e?.message)
                if (e == null) {
                    view.loginCallBack(user!!, null)
                } else {
                    view.loginCallBack("", e)
                }
            }
        })
    }

    override fun oilStatistic(car: Car, listener: QueryListener<JSONArray>) {
        val query = BmobQuery<Oil>()
        query.addWhereEqualTo("car", car)
        query.sum(arrayOf("mount", "fee"))
        query.max(arrayOf("mount", "updatedAt"))
        query.min(arrayOf("fee", "updatedAt"))
        query.findStatistics(Oil::class.java, listener)
    }

    override fun oilDetail(car: Car, listener: FindListener<Oil>) {
        val query = BmobQuery<Oil>()
        query.addWhereEqualTo("car", car)
        query.order("createdAt")
        query.findObjects(listener)
    }
}