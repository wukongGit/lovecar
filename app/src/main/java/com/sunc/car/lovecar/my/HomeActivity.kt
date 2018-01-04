package com.sunc.car.lovecar.my

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.squareup.otto.Subscribe
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.User
import com.sunc.car.lovecar.databinding.ActivityHomeBinding
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.login.CarListActivity
import com.sunc.car.lovecar.toast
import kotlinx.android.synthetic.main.layout_title_bar.*

class HomeActivity : BaseBindingActivity<ActivityHomeBinding>() {
    val REQUEST_CODE_USER_MOBILE = 100
    val REQUEST_CODE_USER_SEX = 101
    val REQUEST_CODE_USER_AGE = 102
    val REQUEST_CODE_USER_WORK = 103

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityHomeBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    override fun initView() {
        mBinding.user = App.instance.getUser()
        mBinding.car = App.instance.getCar()
        with(mBinding) {
            btnLoginOut.setOnClickListener {
                App.instance.logout()
                EventBus.bus().post(NotifyType(NotifyType.EVENT_CAR_CHANGE))
                finish()
            }
            head_title.text = getString(R.string.personal_page)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            llCurrentCar.setOnClickListener {
                startActivity(Intent(this@HomeActivity, CarListActivity::class.java))
            }
            rlMobile.setOnClickListener {
                editUserInfo(REQUEST_CODE_USER_MOBILE, tvMobile.text.toString())
            }
            rlSex.setOnClickListener {
                editUserInfo(REQUEST_CODE_USER_SEX, tvSex.text.toString())
            }
            rlAge.setOnClickListener {
                editUserInfo(REQUEST_CODE_USER_AGE, tvAge.text.toString())
            }
            rlWork.setOnClickListener {
                editUserInfo(REQUEST_CODE_USER_WORK, tvWork.text.toString())
            }
            tvFeedback.setOnClickListener {
                startActivity(Intent(this@HomeActivity, FeedbackActivity::class.java))
            }
        }
    }

    private fun editUserInfo(requestCode: Int, value: String?) {
        var intent = Intent(this, UserEditActivity::class.java)
        intent.putExtra(UserEditActivity.USER_VALUE, value)
        if (requestCode == REQUEST_CODE_USER_AGE) {
            intent.putExtra(UserEditActivity.TYPE_NUM, true)
        }
        if (requestCode == REQUEST_CODE_USER_SEX) {
            intent.putExtra(UserEditActivity.TYPE_SEX, true)
        }
        startActivityForResult(intent, requestCode)
    }

    private fun updateUserInfo(name: Int, value: String) {
        val currentUser = App.instance.getUser()
        val user = User()
        when (name) {
            REQUEST_CODE_USER_AGE -> {
                user.age = value.toInt()
                currentUser?.age = value.toInt()
                with(mBinding) {
                    tvAge.text = value
                }
            }
            REQUEST_CODE_USER_SEX -> {
                user.sex = value.toInt()
                currentUser?.sex = value.toInt()
                with(mBinding) {
                    tvSex.text = user.sexDes()
                    if (user.isMale()) {
                        ivAvatar.setImageResource(R.mipmap.bg_male)
                    } else {
                        ivAvatar.setImageResource(R.mipmap.bg_female)
                    }

                }
            }
            REQUEST_CODE_USER_WORK -> {
                user.work = value
                currentUser?.work = value
                with(mBinding) {
                    tvWork.text = value
                }
            }
            REQUEST_CODE_USER_MOBILE -> {
                user.mobilePhoneNumber = value
                currentUser?.work = value
                with(mBinding) {
                    tvMobile.text = value
                }
            }
        }
        user.update(App.instance.getUser()?.objectId, object : UpdateListener() {

            override fun done(e: BmobException?) {
                if (e == null) {
                    App.instance.setUser(currentUser!!)
                } else {
                    toast(getString(R.string.save_failed))
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_USER_AGE, REQUEST_CODE_USER_SEX, REQUEST_CODE_USER_WORK, REQUEST_CODE_USER_MOBILE -> updateUserInfo(requestCode, data?.getStringExtra(UserEditActivity.USER_VALUE)!!)
            }
        }
    }

    @Subscribe
    override fun onNotify(type: NotifyType?) {
        super.onNotify(type)
        when (type?.type) {
            NotifyType.EVENT_CAR_CHANGE -> {
                mBinding.car = App.instance.getCar()
            }
        }
    }

}
