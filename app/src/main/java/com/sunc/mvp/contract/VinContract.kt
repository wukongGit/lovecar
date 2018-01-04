package com.sunc.mvp.contract


/**
 * Created by Administrator on 2017/11/14.
 */
interface VinContract {
    interface View {
        fun setResult(model: Map<String, String>)
    }

    interface Presenter {
        fun getVinInfo(vin: String)
    }

}