package com.sunc.car.lovecar.bill

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.sunc.base.BaseBingingFragment
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.databinding.FragmentBillBinding
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.login.LoginActivity
import com.sunc.car.lovecar.toast
import com.sunc.di.component.BillModule
import com.sunc.mvp.contract.BillContract
import com.sunc.mvp.presenter.BillPresenter
import com.sunc.utils.AndroidUtils
import lecho.lib.hellocharts.model.SliceValue
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class BillFragment : BaseBingingFragment<FragmentBillBinding>(), BillContract.View {
    override fun setPieOutStatisticData(total: List<SliceValue>) {
    }

    override fun setPieInStatisticData(total: List<SliceValue>) {
    }

    override fun setMonthStatisticData(total: List<MonthBill>) {
        mAdapter.setMonthList(total)
        mAdapter.notifyDataSetChanged()
    }

    override fun setOutStatisticData(total: String) {
        with(mBinding) {
            mTotalOut = total
            val totalF = Math.abs(total.toFloat())
            tvOut.text = totalF.toString()
            tvOutTop.text = getString(R.string.bill_total_out).plus("：").plus(totalF.toString())

            val out = (tvIn.text.toString().toFloat() - totalF).toString()
            tvTotalTop.text = "：".plus(out)
            tvTotal.text = out
        }
    }

    override fun setInStatisticData(total: String) {
        with(mBinding) {
            mTotalIn = total
            tvIn.text = total
            tvInTop.text = getString(R.string.bill_total_in).plus("：").plus(total)

            val out = (total.toFloat() - tvOut.text.toString().toFloat()).toString()
            tvTotalTop.text = "：".plus(out)
            tvTotal.text = out
        }
    }

    override fun onError(msg: String) {
        activity.toast(msg)
    }

    override fun setData(results: List<Bill>?) {
        if (results != null && results.isNotEmpty()) {
            if (mPage == 0) {
                mList.clear()
            }
            mPage++
            mList.addAll(results)
            mList.let {
                mAdapter.notifyDataSetChanged()
            }
        }
        if (mList.isEmpty()) {
            with(mBinding) {
                tvEmpty.visibility = View.VISIBLE
            }
        } else {
            with(mBinding) {
                tvEmpty.visibility = View.GONE
            }
        }
    }

    private var mList = ArrayList<Bill>()
    private lateinit var mAdapter: BillAdapter
    @Inject lateinit var mPresenter: BillPresenter
    private var mPage = 0
    private var mTotalOut = ""
    private var mTotalIn = ""

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentBillBinding {
        return FragmentBillBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mAdapter = BillAdapter(mList)
        context.getMainComponent().plus(BillModule(this)).inject(this)
        with(mBinding) {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val scroll = java.lang.Math.abs(verticalOffset).toFloat()
                val alpha = 1 - scroll / appBarLayout.totalScrollRange
                tvIn.alpha = alpha
                tvInTag.alpha = alpha
                tvOut.alpha = alpha
                tvOutTag.alpha = alpha
                llData.alpha = 1 - alpha
                if (alpha > 0.9) {
                    ivAvatar.alpha = 0.1f
                } else {
                    ivAvatar.alpha = 1 - alpha
                }
            }
            ivAvatar.setOnClickListener {
                if (mList.isEmpty() || App.instance.getCar() == null) {
                    return@setOnClickListener
                }
                val intent = Intent(activity, PieActivity::class.java)
                intent.putExtra(PieActivity.TOTAL_INCOME, mTotalIn)
                intent.putExtra(PieActivity.TOTAL_OUTCOME, mTotalOut)
                startActivity(intent)
            }
            floatingButton.setOnClickListener {
                val car = App.instance.getCar()
                if (car == null) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(activity, RecordActivity::class.java))
                }
            }
            tvEmpty.setOnClickListener {
                val car = App.instance.getCar()
                if (car == null) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(activity, RecordActivity::class.java))
                }
            }
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView?.canScrollVertically(1)!!) {
                        val car = App.instance.getCar()
                        if (car != null) {
                            mPresenter.getData(mPage, car)
                        }
                    }
                }
            })
        }
        val car = App.instance.getCar()
        if (car != null && AndroidUtils.isNetworkConnected(activity)) {
            mPresenter.statisticData(car)
            mPresenter.getData(mPage, car)
        }
    }

    @Subscribe
    override fun onNotify(type: NotifyType?) {
        super.onNotify(type)
        when (type?.type) {
            NotifyType.EVENT_CAR_CHANGE, NotifyType.EVENT_BILL_ADDED -> {
                val car = App.instance.getCar()
                if (car != null) {
                    mPresenter.statisticData(car)
                    mPage = 0
                    mPresenter.getData(mPage, car)
                }
            }
        }
    }

}