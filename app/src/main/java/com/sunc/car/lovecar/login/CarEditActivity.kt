package com.sunc.car.lovecar.login

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.databinding.ActivityUserEditBinding
import kotlinx.android.synthetic.main.layout_title_bar.*

class CarEditActivity : BaseBindingActivity<ActivityUserEditBinding>() {
    companion object {
        val CAR = "CAR"
    }
    private var mCar: Car? = null

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityUserEditBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_user_edit)
    }

    override fun initView() {
        mCar = intent.getSerializableExtra(CAR) as Car?

        with(mBinding) {
            radioGroup.visibility = View.GONE
            etValue.visibility = View.VISIBLE
            if (mCar != null) {
                etValue.setText(mCar!!.name)
            }
            head_title.text = getString(R.string.car_manage)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            head_menu_tv.visibility = View.VISIBLE
            head_menu_tv.setOnClickListener {
                if (mCar == null) {
                    mCar = Car()
                }
                mCar!!.name = etValue.text.toString()
                val intent = Intent()
                intent.putExtra(CAR, mCar)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

}
