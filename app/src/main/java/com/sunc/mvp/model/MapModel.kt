package com.sunc.mvp.model

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.sunc.mvp.contract.MapContract
import javax.inject.Inject
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch



/**
 * Created by Administrator on 2017/11/14.
 */
class MapModel
@Inject constructor(private val mContext: Context): MapContract.Model {
    private var mLocationClient: AMapLocationClient? = null

    override fun stopLocate() {
        mLocationClient?.stopLocation()
        mLocationClient?.onDestroy()
    }

    override fun nearby(content: String, latitude: Double, longitude: Double, page: Int, view: MapContract.View) {
        var query = PoiSearch.Query(content, "", "")
        query.pageSize = 10// 设置每页最多返回多少条poiitem
        query.pageNum = page// 设置查第一页
        val poiSearch = PoiSearch(mContext, query)
        if (latitude != 0.0 && longitude != 0.0) {
            poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(latitude, longitude), 5000)// 设置周边搜索的中心点以及区域
            poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener{
                override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
                }

                override fun onPoiSearched(p0: PoiResult?, p1: Int) {
                    view.onPoiSearched(content, p0)
                }
            })
            poiSearch.searchPOIAsyn()// 开始搜索
        }
    }

    override fun locate(view: MapContract.View) {
        mLocationClient = AMapLocationClient(mContext)
        val locationOption = AMapLocationClientOption()
        mLocationClient!!.setLocationListener({ aMapLocation ->
            view.onLocationChanged(aMapLocation)
        })
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationOption.interval = (1000 * 3600).toLong()
        mLocationClient!!.setLocationOption(locationOption)
        mLocationClient!!.startLocation()
    }
}