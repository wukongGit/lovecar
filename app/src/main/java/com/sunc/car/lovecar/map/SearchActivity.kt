package com.sunc.car.lovecar.map

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.kyview.interfaces.AdViewInstlListener
import com.kyview.manager.AdViewInstlManager
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivitySearchBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.di.component.MapModule
import com.sunc.mvp.contract.MapContract
import com.sunc.mvp.presenter.MapPresenter
import com.sunc.utils.AndroidUtils
import com.sunc.utils.DBKeys
import com.sunc.utils.DBUtils
import com.sunc.utils.DimenUtils
import com.sunc.view.SizeDivider
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/27.
 */
class SearchActivity: BaseBindingActivity<ActivitySearchBinding>(), MapContract.View, AdViewInstlListener {
    val TAG = "ADVIEW_SearchActivity"
    override fun onAdDismiss(p0: String?) {
        Log.d(TAG, "onAdDismiss:" + p0)
    }

    override fun onAdRecieved(p0: String?) {
        Log.d(TAG, "onAdRecieved:" + p0)
        Observable.timer(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).map {
            if (!mAdHasShow) {
                AdViewInstlManager.getInstance(this).showAd(this, App.instance.key1)
            }
        }.subscribe()
    }

    override fun onAdFailed(p0: String?) {
        Log.d(TAG, "onAdFailed:" + p0)
    }

    override fun onAdDisplay(p0: String?) {
        Log.d(TAG, "onAdDisplay:" + p0)
        mAdHasShow = true
    }

    override fun onAdClick(p0: String?) {
        Log.d(TAG, "onAdClick:" + p0)
    }

    private var mList = ArrayList<PoiItem>()
    private var mHisList = ArrayList<PoiItem>()
    private lateinit var mAdapter: ResultAdapter
    private lateinit var mHisAdapter: ResultAdapter
    @Inject lateinit var mPresenter: MapPresenter
    private var mPage = 1
    private var mKeywords = ""
    private var mAdHasShow = false

    override fun onLocationChanged(results: AMapLocation) {
    }

    override fun onPoiSearched(content: String, p0: PoiResult?) {
        val list = p0?.pois
        if (list != null && list.isNotEmpty()) {
            if (mPage == 1) {
                mList.clear()
            }
            mList.addAll(list)
            mList.let {
                mAdapter.notifyDataSetChanged()
            }
            mPage++
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivitySearchBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_search)
    }

    override fun initView() {
        getMainComponent().plus(MapModule(this)).inject(this)
        mAdapter = ResultAdapter(mList)
        mAdapter.setOnItemClickListener { pos ->
            val clickItem = mList[pos]
            add2His(clickItem)
            intent = Intent()
            intent.putExtra(SELECTED_POSITION, clickItem)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        with(mBinding) {
            rcResult.adapter = mAdapter
            rcResult.layoutManager = LinearLayoutManager(this@SearchActivity)
            rcResult.addItemDecoration(SizeDivider(object : SizeDivider.SimpleSizeProvider() {
                override fun dividerSize(position: Int, parent: RecyclerView): Int {
                    return DimenUtils.dp2px(this@SearchActivity, 16f)
                }
            }))
            rcResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView?.canScrollVertically(1)!!) {
                        mPresenter.nearby(mKeywords, App.instance.mLatLng[0], App.instance.mLatLng[1], mPage)
                    }
                }
            })
            val hisList =  DBUtils.read< ArrayList<PoiItem>>(DBKeys.SEARCH_HISTORY)
            if (hisList != null) {
                mHisList.addAll(hisList)
                rcHistory.visibility = View.VISIBLE
                mHisAdapter = ResultAdapter(mHisList)
                mHisAdapter.setOnItemClickListener { pos ->
                    val clickItem = mHisList[pos]
                    add2His(clickItem)
                    intent = Intent()
                    intent.putExtra(SELECTED_POSITION, clickItem)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                rcHistory.adapter = mHisAdapter
                rcHistory.layoutManager = LinearLayoutManager(this@SearchActivity)
                rcHistory.addItemDecoration(SizeDivider(object : SizeDivider.SimpleSizeProvider() {
                    override fun dividerSize(position: Int, parent: RecyclerView): Int {
                        return DimenUtils.dp2px(this@SearchActivity, 16f)
                    }
                }))
            }

            tvSearch.setOnClickListener {
                if (!AndroidUtils.isNetworkConnected(this@SearchActivity)) {
                    toast(getString(R.string.network_error))
                    return@setOnClickListener
                }
                rcResult.visibility = View.VISIBLE
                rcHistory.visibility = View.GONE
                mKeywords = etInput.text.toString()
                mPage = 1
                mPresenter.nearby(mKeywords, App.instance.mLatLng[0], App.instance.mLatLng[1], mPage)
            }
        }
        initAd(App.instance.key1)
    }

    private fun add2His(pio: PoiItem) {
        val index = getIndex(mHisList, pio)
        if (index != -1) {
            mHisList.removeAt(index)
        }
        mHisList.add(0,pio)
        if (mHisList.size > 5) {
            DBUtils.write(DBKeys.SEARCH_HISTORY, mHisList.subList(0, 5))
        } else {
            DBUtils.write(DBKeys.SEARCH_HISTORY, mHisList)
        }
    }

    private fun getIndex(list: ArrayList<PoiItem>, item: PoiItem) : Int{
        if (list.isEmpty()) {
            return -1
        }
        return (0 until list.size).firstOrNull { list[it].title == item.title }
                ?: -1
    }

    private fun initAd(key: String) {
        AdViewInstlManager.getInstance(this).requestAd(this, key, this)
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            with(mBinding) {
                llAd?.removeAllViews()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        val SELECTED_POSITION = "SELECTED_POSITION"
    }
}