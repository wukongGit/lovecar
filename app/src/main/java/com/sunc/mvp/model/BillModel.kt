package com.sunc.mvp.model

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.sunc.car.lovecar.bill.MonthBill
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Car
import com.sunc.mvp.contract.BillContract
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.util.ChartUtils
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class BillModel
@Inject constructor(private val loginModel: LoginModel): BillContract.Model {
    //val TAG = "BillModel"
    override fun statisticData(car: Car, view: BillContract.View) {
        loginModel.billOutStatistic(car, object : QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                //Log.d(TAG, "statisticData===p0:" + p0?.toString() + ", p1:" + p1?.errorCode + p1?.message)
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val obj = p0.getJSONObject(0)
                            val totalSum = obj.getString("_sumValue")//_(关键字)+首字母大写的列名
                            view.setOutStatisticData(totalSum)
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

        loginModel.billInStatistic(car, object : QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                //Log.d(TAG, "billInStatistic===p0:" + p0?.toString() + ", p1:" + p1?.errorCode + p1?.message)
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val obj = p0.getJSONObject(0)
                            val totalSum = obj.getString("_sumValue")//_(关键字)+首字母大写的列名
                            view.setInStatisticData(totalSum)
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
        loginModel.billMonthStatistic(car, object : QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                //Log.d(TAG, "billMonthStatistic===p0:" + p0?.toString() + ", p1:" + p1?.errorCode + p1?.message)
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val list = ArrayList<MonthBill>()
                            val size = p0.length()
                            for (i in 0 until size) {
                                val json = p0.getJSONObject(i)
                                var _sumValue = json.getString("_sumValue")
                                var month = json.getString("month")
                                list.add(MonthBill(_sumValue, month))
                            }
                            view.setMonthStatisticData(list)
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

    override fun billPieStatistic(car: Car, view: BillContract.View) {
        loginModel.billInTotalStatistic(car, object : QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                //Log.d(TAG, "billMonthStatistic===p0:" + p0?.toString() + ", p1:" + p1?.errorCode + p1?.message)
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val values = ArrayList<SliceValue>()
                            val size = p0.length()
                            for (i in 0 until size) {
                                val json = p0.getJSONObject(i)
                                var _sumValue = json.getString("_sumValue")
                                var type = json.getString("type")
                                val sliceValue = SliceValue(_sumValue.toFloat(), ChartUtils.pickColor())
                                sliceValue.setLabel(type.plus("：").plus(_sumValue))
                                values.add(sliceValue)
                            }
                            view.setPieInStatisticData(values)
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
        loginModel.billOutTotalStatistic(car, object : QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                //Log.d(TAG, "billMonthStatistic===p0:" + p0?.toString() + ", p1:" + p1?.errorCode + p1?.message)
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val values = ArrayList<SliceValue>()
                            val size = p0.length()
                            for (i in 0 until size) {
                                val json = p0.getJSONObject(i)
                                var _sumValue = json.getString("_sumValue")
                                var type = json.getString("type")
                                val sliceValue = SliceValue(_sumValue.toFloat(), ChartUtils.pickColor())
                                sliceValue.setLabel(type.plus("：").plus(_sumValue))
                                values.add(sliceValue)
                            }
                            view.setPieOutStatisticData(values)
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

    override fun getData(page: Int, car: Car, view: BillContract.View) {
        loginModel.billList(page, 30, car, object : FindListener<Bill>() {
            override fun done(p0: MutableList<Bill>?, p1: BmobException?) {
                //Log.d(TAG, "billList===p0:" + p0?.toString() + ", p1:" + p1?.errorCode + p1?.message + ", page:" + page)
                if (p1 == null) {
                    view.setData(p0)
                } else {
                    if(p1.errorCode != 101) {
                        view.onError(p1.errorCode.toString() + p1.message!!)
                    }
                }
            }
        })
    }
}