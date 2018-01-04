package com.sunc.car.lovecar.third

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityCarDetailBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.yun.ApiCarModel
import com.sunc.car.lovecar.yun.ApiKeyValue
import com.sunc.di.component.ServiceModule
import com.sunc.mvp.contract.ServiceContract
import com.sunc.mvp.presenter.ServicePresenter
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.util.ArrayList
import javax.inject.Inject

class CarDetailActivity : BaseBindingActivity<ActivityCarDetailBinding>(), ServiceContract.View {
    private lateinit var mCarModel: ApiCarModel
    @Inject lateinit var mPresenter: ServicePresenter
    private var mList = ArrayList<ApiKeyValue>()
    private lateinit var mAdapter: KeyValueAdapter

    override fun setData(model: List<ApiCarModel>?) {
        if (model != null && model.isNotEmpty()) {
            mCarModel = model[0]
            val basic = mCarModel.basic
            val body = mCarModel.body
            val engine = mCarModel.engine
            val gearbox = mCarModel.gearbox
            mList.clear()
            for ((key, value) in basic) {
                mList.add(ApiKeyValue(key, value))
            }
            for ((key, value) in body) {
                mList.add(ApiKeyValue(key, value))
            }
            for ((key, value) in engine) {
                mList.add(ApiKeyValue(key, value))
            }
            for ((key, value) in gearbox) {
                mList.add(ApiKeyValue(key, value))
            }
            mList.let {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityCarDetailBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_car_detail)
    }

    override fun initView() {
        mCarModel = intent.getSerializableExtra(CarContainActivity.CAR) as ApiCarModel
        mAdapter = KeyValueAdapter(mList)
        getMainComponent().plus(ServiceModule(this)).inject(this)
        with(mBinding) {
            head_title.text = mCarModel.name
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            recyclerView.layoutManager = LinearLayoutManager(this@CarDetailActivity)
            recyclerView.adapter = mAdapter
            mBinding.item = mCarModel
            mPresenter.getCarDetail(mCarModel.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }
}
