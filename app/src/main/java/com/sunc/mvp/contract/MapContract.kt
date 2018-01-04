package com.sunc.mvp.contract

import com.amap.api.location.AMapLocation
import com.amap.api.services.poisearch.PoiResult

/**
 * Created by Administrator on 2017/11/14.
 */
interface MapContract {
    interface View {
        fun  onLocationChanged(results: AMapLocation)
        fun onPoiSearched(content: String, p0: PoiResult?)
    }

    interface Model {
        fun stopLocate()
        fun locate(view: MapContract.View)
        fun nearby(content: String, latitude: Double, longitude: Double, page: Int, view: MapContract.View)
    }

    interface Presenter {
        fun stopLocate()
        fun locate()
        fun nearby(content: String, latitude: Double, longitude: Double, page: Int)
    }
}