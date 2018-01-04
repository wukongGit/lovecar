package com.sunc.car.lovecar.bill

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityEditWayBinding
import com.sunc.car.lovecar.my.ServiceItem
import com.sunc.car.lovecar.toast
import com.sunc.utils.AndroidUtils
import kotlinx.android.synthetic.main.layout_title_bar.*

class EditWayActivity : BaseBindingActivity<ActivityEditWayBinding>() {
    companion object {
        val RESULT_DATA = "RESULT_DATA"
        val RESULT_ICON = "RESULT_ICON"
    }

    private var mList = ArrayList<ServiceItem>()
    private lateinit var mAdapter: WayAdapter
    private lateinit var mCurrentItem : ServiceItem

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityEditWayBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_edit_way)
    }

    override fun initView() {
        initWays()
        with(mBinding) {
            head_title.text = getString(R.string.edit_icon)
            head_menu_tv.visibility = View.VISIBLE
            head_menu_tv.text = getString(R.string.ok)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            head_menu_tv.setOnClickListener{
                val way = etWay.text.toString()
                if (way.isEmpty()) {
                    toast(getString(R.string.way_required))
                    return@setOnClickListener
                }
                val intent = Intent()
                intent.putExtra(RESULT_DATA, way)
                intent.putExtra(RESULT_ICON, mCurrentItem.icon)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            recyclerView.layoutManager = GridLayoutManager(this@EditWayActivity, 5)
            mAdapter = WayAdapter(mList)
            recyclerView.adapter = mAdapter
            mAdapter.setOnItemClickListener { pos ->
                mCurrentItem = mList[pos]
                ivIcon.setImageResource(AndroidUtils.getIconByReflect(mCurrentItem.icon))
            }
        }
    }

    private fun initWays() {
        mList.clear()
        mList.add(ServiceItem("", "ic_self_01"))
        mList.add(ServiceItem("", "ic_self_02"))
        mList.add(ServiceItem("", "ic_self_03"))
        mList.add(ServiceItem("", "ic_self_04"))
        mList.add(ServiceItem("", "ic_self_05"))
        mList.add(ServiceItem("", "ic_self_06"))
        mList.add(ServiceItem("", "ic_self_07"))
        mList.add(ServiceItem("", "ic_self_08"))
        mList.add(ServiceItem("", "ic_self_09"))
        mList.add(ServiceItem("", "ic_self_10"))
        mList.add(ServiceItem("", "ic_self_11"))
        mList.add(ServiceItem("", "ic_self_12"))
        mList.add(ServiceItem("", "ic_self_13"))
        mList.add(ServiceItem("", "ic_self_14"))
        mList.add(ServiceItem("", "ic_self_15"))
        mList.add(ServiceItem("", "ic_self_16"))
        mList.add(ServiceItem("", "ic_self_17"))
        mList.add(ServiceItem("", "ic_self_18"))
        mList.add(ServiceItem("", "ic_self_19"))
        mList.add(ServiceItem("", "ic_self_20"))
        mList.add(ServiceItem("", "ic_self_21"))
        mList.add(ServiceItem("", "ic_self_22"))
        mList.add(ServiceItem("", "ic_self_23"))
        mList.add(ServiceItem("", "ic_self_24"))
        mList.add(ServiceItem("", "ic_self_25"))
        mList.add(ServiceItem("", "ic_self_26"))
        mList.add(ServiceItem("", "ic_self_27"))
        mList.add(ServiceItem("", "ic_self_28"))
        mList.add(ServiceItem("", "ic_self_29"))
        mList.add(ServiceItem("", "ic_self_30"))
        mList.add(ServiceItem("", "ic_self_31"))
        mCurrentItem = mList[0]
    }

}
