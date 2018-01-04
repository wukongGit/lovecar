package com.sunc.car.lovecar.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.navi.*
import com.amap.api.services.poisearch.PoiResult
import com.sunc.base.BaseBingingFragment
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.FragmentMapBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.di.component.MapModule
import com.sunc.mvp.contract.MapContract
import com.sunc.mvp.presenter.MapPresenter
import javax.inject.Inject
import com.amap.api.navi.model.AMapNaviLocation
import com.amap.api.services.core.PoiItem
import com.squareup.otto.Subscribe
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.utils.AndroidUtils
import com.tbruyelle.rxpermissions.RxPermissions
import rx.functions.Action1


/**
 * Created by Administrator on 2017/11/23.
 */
class MapFragment : BaseBingingFragment<FragmentMapBinding>(), MapContract.View, INaviInfoCallback {

    override fun onGetNavigationText(p0: String?) {
    }

    override fun onLocationChange(p0: AMapNaviLocation?) {
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
    }

    override fun onInitNaviFailure() {
    }

    override fun onArriveDestination(p0: Boolean) {
    }

    override fun onStartNavi(p0: Int) {
    }

    override fun onStopSpeaking() {
    }

    override fun onCalculateRouteFailure(p0: Int) {
    }

    val SERVICE_4S: String by lazy { getString(R.string.s4_hint) }
    val SERVICE_OIL: String by lazy { getString(R.string.oil_hint) }
    val SERVICE_PARKING: String by lazy { getString(R.string.parking_hint) }

    private var oil_on = true
    private var s4_on = true
    private var parking_on = true

    private var mOilList = mutableListOf<Marker>()
    private var mS4List = mutableListOf<Marker>()
    private var mParkingList = mutableListOf<Marker>()

    private var mCurrentMarker: Marker? = null

    private var aMap: AMap? = null
    @Inject lateinit var mPresenter: MapPresenter

    override fun onPoiSearched(content: String, p0: PoiResult?) {
        val list = p0?.pois
        when (content) {
            SERVICE_4S -> {
                for (item in list!!) {
                    val point = LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude)
                    val title = item.title
                    val snippet = item.provinceName + item.cityName + item.adName + item.snippet + " | " + getString(R.string.distance) + item.distance + "m\n" + item.tel + "\n" + item.typeDes
                    var marker = aMap?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_4s)).title(title).snippet(snippet).position(point))
                    mS4List.add(marker!!)
                }
            }
            SERVICE_OIL -> {
                for (item in list!!) {
                    val point = LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude)
                    val title = item.title
                    val snippet = item.provinceName + item.cityName + item.adName + item.snippet + " | " + getString(R.string.distance) + item.distance + "m\n" + item.tel + "\n" + item.typeDes
                    var marker = aMap?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_oil)).title(title).snippet(snippet).position(point))
                    mOilList.add(marker!!)
                }
            }
            SERVICE_PARKING -> {
                for (item in list!!) {
                    val point = LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude)
                    val title = item.title
                    val snippet = item.provinceName + item.cityName + item.adName + item.snippet + " | " + getString(R.string.distance) + item.distance + "m\n" + item.tel + "\n" + item.typeDes
                    var marker = aMap?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_parking)).title(title).snippet(snippet).position(point))
                    mParkingList.add(marker!!)
                }
            }
        }
    }

    override fun onLocationChanged(results: AMapLocation) {
        if (results.errorCode == 0) {
            App.instance.mLatLng[0] = results.latitude
            App.instance.mLatLng[1] = results.longitude
            val latLng = LatLng(results.latitude, results.longitude)
            aMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            //aMap?.addMarker(MarkerOptions().position(latLng))
            if (oil_on) {
                mPresenter.nearby(SERVICE_OIL, results.latitude, results.longitude, 1)
            }
            if (s4_on) {
                mPresenter.nearby(SERVICE_4S, results.latitude, results.longitude, 1)
            }
            if (parking_on) {
                mPresenter.nearby(SERVICE_PARKING, results.latitude, results.longitude, 1)
            }

        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            context.toast("location Error, ErrCode:" + results.errorCode + ", errInfo:" + results.errorInfo)
        }
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        context.getMainComponent().plus(MapModule(this)).inject(this)
        with(mBinding) {
            mapView.onCreate(savedInstanceState)
            if (aMap == null) {
                aMap = mapView.map
            }
            aMap!!.isMyLocationEnabled = true
            //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW)
            aMap!!.setOnMarkerClickListener { p0 ->
                aMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(p0.position, aMap!!.cameraPosition.zoom, aMap!!.cameraPosition.tilt, 0.0f)))
                mBinding.item = p0
                ivIcon.setImageBitmap(p0.icons[0].bitmap)
                mCurrentMarker = p0
                false
            }
            touchIndicator.setTouchIndicatorListener {
                hideMarker()
            }
            llSearch.setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java)
                startActivityForResult(intent, 100)
            }
            btnNavigation.setOnClickListener {
                val cameraPosition = aMap!!.cameraPosition
                val bearing = 0.0f  // 地图默认方向
                aMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing)))

            }
            btnOil.isSelected = true
            btnOil.setOnClickListener {
                oil_on = !oil_on
                btnOil.isSelected = oil_on
                markersVisible(mOilList, oil_on)
            }
            btn4s.isSelected = true
            btn4s.setOnClickListener {
                s4_on = !s4_on
                btn4s.isSelected = s4_on
                markersVisible(mS4List, s4_on)
            }
            btnParking.isSelected = true
            btnParking.setOnClickListener {
                parking_on = !parking_on
                btnParking.isSelected = parking_on
                markersVisible(mParkingList, parking_on)
            }
            cardView.setOnClickListener {
                hideMarker()
                val startPoint = LatLng(App.instance.mLatLng[0], App.instance.mLatLng[1])
                val endPoint = mBinding.item.position
                val start = Poi(getString(R.string.current_point), startPoint, "")//起点
                val end = Poi(mBinding.item.title, endPoint, "")//终点
                val amapNaviParams = AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI)
                AmapNaviPage.getInstance().showRouteActivity(App.instance, amapNaviParams, this@MapFragment)
            }
            if (!AndroidUtils.isNetworkConnected(activity)) {
                activity.toast(getString(R.string.network_error))
                return
            }
            RxPermissions.getInstance(context)
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe { aBoolean ->
                        if (aBoolean!!) {
                            mPresenter.locate()
                        }
                    }
        }
    }

    private fun hideMarker() {
        mCurrentMarker?.hideInfoWindow()
    }

    private fun markersVisible(markers: MutableList<Marker>, visible: Boolean) {
        for (marker in markers) {
            marker.isVisible = visible
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        with(mBinding) {
            mapView?.onSaveInstanceState(outState)
            mPresenter.stopLocate()
        }
    }

    override fun onResume() {
        super.onResume()
        with(mBinding) {
            mapView?.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        with(mBinding) {
            mapView?.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(mBinding) {
            mPresenter.stopLocate()
            mapView?.onDestroy()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            aMap!!.isMyLocationEnabled = false
            val poiItem = data!!.getParcelableExtra<PoiItem>(SearchActivity.SELECTED_POSITION)
            val point = LatLng(poiItem.latLonPoint.latitude, poiItem.latLonPoint.longitude)
            val title = poiItem.title
            val snippet = poiItem.provinceName + poiItem.cityName + poiItem.adName + poiItem.snippet + " | " + getString(R.string.distance) + poiItem.distance + "m\n" + poiItem.tel + "\n" + poiItem.typeDes
            var marker = aMap?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_4s)).title(title).snippet(snippet).position(point))
            aMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(LatLng(poiItem.latLonPoint.latitude, poiItem.latLonPoint.longitude), aMap!!.cameraPosition.zoom, aMap!!.cameraPosition.tilt, 0.0f)))
            mBinding.item = marker
            with(mBinding) {
                ivIcon.setImageBitmap(marker!!.icons[0].bitmap)
            }
        }
    }

}