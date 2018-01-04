package com.sunc.car.lovecar.third

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ViewRecyclerBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.yun.ApiCarModel
import com.sunc.di.component.ServiceModule
import com.sunc.mvp.contract.ServiceContract
import com.sunc.mvp.presenter.ServicePresenter
import com.sunc.utils.DimenUtils
import com.sunc.view.SizeDivider
import kotlinx.android.synthetic.main.layout_title_bar.*
import kotlinx.android.synthetic.main.view_recycler.*
import java.util.ArrayList
import javax.inject.Inject


class CarContainActivity : BaseBindingActivity<ViewRecyclerBinding>(), ServiceContract.View {
    companion object {
        val CAR = "CAR"
    }
    private lateinit var mCarModel: ApiCarModel
    private var mList = ArrayList<ApiCarModel>()
    private lateinit var mAdapter: CarContainAdapter
    @Inject lateinit var mPresenter: ServicePresenter

    override fun setData(results: List<ApiCarModel>?) {
        if (results == null) {
            return
        }
        mList.clear()
        mList.addAll(results)
        mList.let {
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ViewRecyclerBinding {
        return DataBindingUtil.setContentView(this, R.layout.view_recycler)
    }

    override fun initView() {
        mCarModel = intent.getSerializableExtra(CAR) as ApiCarModel
        mAdapter = CarContainAdapter(mList)
        getMainComponent().plus(ServiceModule(this)).inject(this)
        with(mBinding) {
            layout_title.visibility = View.VISIBLE
            head_title.text = mCarModel.name
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            recyclerView.layoutManager = LinearLayoutManager(this@CarContainActivity)
            recyclerView.addItemDecoration(SizeDivider(object : SizeDivider.SimpleSizeProvider() {
                override fun dividerSize(position: Int, parent: RecyclerView): Int {
                    return DimenUtils.dp2px(this@CarContainActivity, 16f)
                }
            }))
            recyclerView.adapter = mAdapter
            mAdapter.setOnItemClickListener { pos ->
                val intent = Intent(this@CarContainActivity, CarDetailActivity::class.java)
                intent.putExtra(CarContainActivity.CAR, mList[pos])
                startActivity(intent)
            }
            mPresenter.getCarModelContain(mCarModel.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }
}
