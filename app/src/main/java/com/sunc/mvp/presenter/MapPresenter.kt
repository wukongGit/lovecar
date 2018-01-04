package com.sunc.mvp.presenter

import com.sunc.mvp.contract.MapContract
import com.sunc.mvp.model.MapModel
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class MapPresenter
@Inject constructor(private val mModel: MapModel,
                    private val mView: MapContract.View): MapContract.Presenter {
    override fun stopLocate() {
        mModel.stopLocate()
    }

    override fun nearby(content: String, latitude: Double, longitude: Double, page: Int) {
        mModel.nearby(content, latitude, longitude, page, mView)
    }

    override fun locate() {
        mModel.locate(mView)
    }
}