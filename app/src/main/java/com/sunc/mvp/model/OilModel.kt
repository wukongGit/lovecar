package com.sunc.mvp.model

import android.util.Log
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.mvp.contract.OilContract
import org.json.JSONArray
import org.json.JSONException
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class OilModel
@Inject constructor(private val loginModel: LoginModel): OilContract.Model {
    //val TAG = "OilModel"
    override fun getNormalData(car: Car, view: OilContract.View) {
        loginModel.oilStatistic(car, object : QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                //Log.d(TAG, "getNormalData===" + "p0:" + p0?.toString() + ", p1:" + p1?.message)
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val obj = p0.getJSONObject(0)
                            val mountSum = obj.getString("_sumMount")//_(关键字)+首字母大写的列名
                            val feeSum = obj.getString("_sumFee")
                            val maxMount = obj.getString("_maxMount")
                            val maxFee = obj.getString("_minFee")
                            val firstTime = obj.getString("_minUpdatedAt")
                            view.setNormalData(mountSum, feeSum,maxMount, maxFee, firstTime)
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }
                } else {
                    if(p1.errorCode != 101) {
                        view.onError(p1.errorCode.toString() + p1.message!!)
                    }
                }
            }
        })
    }

    override fun getDetailData(car: Car, view: OilContract.View) {
        loginModel.oilDetail(car, object : FindListener<Oil>(){
            override fun done(p0: MutableList<Oil>?, p1: BmobException?) {
                //Log.d(TAG, "getDetailData===" + "p0:" + p0?.toString() + ", p1:" + p1?.message)
                if (p1 == null) {
                    view.setDetailData(p0)
                } else {
                    if(p1.errorCode != 101) {
                        view.onError(p1.errorCode.toString() + p1.message!!)
                    }
                }
            }
        })
    }

}