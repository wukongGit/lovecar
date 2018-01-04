package com.sunc.car.lovecar.login

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.databinding.ViewRecyclerBinding
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.di.component.CarModule
import com.sunc.mvp.contract.CarContract
import com.sunc.mvp.presenter.CarPresenter
import com.sunc.utils.DimenUtils
import com.sunc.view.SizeDivider
import kotlinx.android.synthetic.main.layout_title_bar.*
import kotlinx.android.synthetic.main.view_recycler.*
import java.util.ArrayList
import javax.inject.Inject

class CarListActivity : BaseBindingActivity<ViewRecyclerBinding>(), CarContract.View {
    val REQUEST_CODE_ADD = 100
    val REQUEST_CODE_EDIT = 101

    @Inject lateinit var mPresenter: CarPresenter

    override fun setData(results: List<Car>) {
        if (results.isNotEmpty()) {
            with(mBinding) {
                emptyView.visibility = View.GONE
            }
            mList.clear()
            mList.addAll(results)
            mList.let {
                mAdapter.notifyDataSetChanged()
            }
        } else {
            with(mBinding) {
                emptyView.visibility = View.VISIBLE
            }
        }
    }

    override fun carUpdated() {
        mPresenter.getData(App.instance.getUser()!!)
    }

    override fun onError(msg: String) {
        toast(msg)
    }

    private var mList = ArrayList<Car>()
    private lateinit var mAdapter: CarAdapter

    override fun createDataBinding(savedInstanceState: Bundle?): ViewRecyclerBinding {
        return DataBindingUtil.setContentView(this, R.layout.view_recycler)
    }

    override fun initView() {
        mAdapter = CarAdapter(mList)
        getMainComponent().plus(CarModule(this)).inject(this)
        with(mBinding) {
            layout_title.visibility = View.VISIBLE
            head_menu.visibility = View.VISIBLE
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            head_menu.setOnClickListener {
                startCarEditActivity(null, REQUEST_CODE_ADD)
            }
            head_title.text = getString(R.string.car_manage)
            recyclerView.layoutManager = LinearLayoutManager(this@CarListActivity)
            recyclerView.addItemDecoration(SizeDivider(object : SizeDivider.SimpleSizeProvider() {
                override fun dividerSize(position: Int, parent: RecyclerView): Int {
                    return DimenUtils.dp2px(this@CarListActivity, 16f)
                }
            }))
            recyclerView.adapter = mAdapter
            mAdapter.setOnItemClickListener { pos ->
                App.instance.setCar(mList[pos])
                EventBus.bus().post(NotifyType(NotifyType.EVENT_CAR_CHANGE))
                finish()
            }
            mAdapter.setOnItemLongClickListener { pos ->
                alertDialog(mList[pos])
            }
            mAdapter.setOnItemEditListener { pos ->
                startCarEditActivity(mList[pos], REQUEST_CODE_EDIT)
            }
            mPresenter.getData(App.instance.getUser()!!)
        }
    }

    private fun alertDialog(car: Car) {
        val dialog = AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_alert).plus(car.name))
                .setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    mPresenter.deleteCar(car)
                }
                .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2D84D6"))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#333333"))
        }
        dialog.show()
    }

    private fun startCarEditActivity(car: Car?, requestCode: Int) {
        val intent = Intent(this@CarListActivity, CarEditActivity::class.java)
        if (car != null) {
            intent.putExtra(CarEditActivity.CAR, car)
        }
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val car = data?.getSerializableExtra(CarEditActivity.CAR) as Car
            when (requestCode) {
                REQUEST_CODE_ADD -> mPresenter.addCar(car)
                REQUEST_CODE_EDIT -> mPresenter.editCar(car)
            }
        }
    }
}