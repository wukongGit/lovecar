package com.sunc.car.lovecar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.bill.BillFragment
import com.sunc.car.lovecar.bmob.Config
import com.sunc.car.lovecar.bmob.Feedback
import com.sunc.car.lovecar.databinding.ActivityMainBinding
import com.sunc.car.lovecar.map.MapFragment
import com.sunc.car.lovecar.my.MyFragment
import com.sunc.car.lovecar.oil.OilFragment
import com.sunc.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
/**
 * 主界面
 * Created by Administrator on 2017/11/14.
 */
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    var mLastTime: Long = 0
    lateinit var mFragments: MutableList<Fragment>

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMainBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initView() {
        initFragments()
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = mFragments[position]
            override fun getCount() = mFragments.size
        }
        viewPager.offscreenPageLimit = 4
        navigationView.setOnNavigationItemSelectedListener { item ->
            var tab = 0
            when (item.itemId) {
                R.id.near -> tab = 0
                R.id.consumption -> tab = 1
                R.id.bill -> tab = 2
                R.id.service -> tab = 3
            }
            viewPager.currentItem = tab
            true
        }
    }

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(MapFragment())
        mFragments.add(OilFragment())
        mFragments.add(BillFragment())
        mFragments.add(MyFragment())
    }

    override fun onBackPressed() {
        var time = SystemClock.elapsedRealtime()
        if (time - mLastTime > 2000) {
            toast(getString(R.string.back_press_tips))
            mLastTime = time
            return
        }
        super.onBackPressed()
    }
}
