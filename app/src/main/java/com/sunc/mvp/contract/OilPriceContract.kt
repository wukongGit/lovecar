package com.sunc.mvp.contract


/**
 * Created by Administrator on 2017/11/14.
 */
interface OilPriceContract {
    interface View {
        fun setResult(model: Map<String, String>)
        fun setCity(list: List<String>)
    }

    interface Presenter {
        fun getOilPrice(province: String)
        fun getCity()
    }

}