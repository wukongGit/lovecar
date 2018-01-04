package com.sunc.car.lovecar.my

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityUserEditBinding
import kotlinx.android.synthetic.main.layout_title_bar.*

class UserEditActivity : BaseBindingActivity<ActivityUserEditBinding>() {
    companion object {
        val USER_VALUE = "USER_VALUE"
        val TYPE_NUM = "TYPE_NUM"
        val TYPE_SEX = "TYPE_SEX"
    }
    private var mValue: String? = null
    private var mIsNum = false
    private var mIsSex = false
    private var mSexEdit = 1

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityUserEditBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_user_edit)
    }

    override fun initView() {
        mValue = intent.getStringExtra(USER_VALUE)
        mIsNum = intent.getBooleanExtra(TYPE_NUM, false)
        mIsSex = intent.getBooleanExtra(TYPE_SEX, false)
        with(mBinding) {
            if (mIsSex) {
                radioGroup.visibility = View.VISIBLE
                etValue.visibility = View.GONE
                when(mValue) {
                    getString(R.string.male) -> rbMale.isChecked = true
                    getString(R.string.female) -> rbFemale.isChecked = true
                }
                radioGroup.setOnCheckedChangeListener { p0, _ ->
                    val checkedId = p0.checkedRadioButtonId
                    when(checkedId) {
                        R.id.rb_male -> mSexEdit = 1
                        R.id.rb_female -> mSexEdit = 2
                    }
                }

            } else {
                radioGroup.visibility = View.GONE
                etValue.visibility = View.VISIBLE
                if (mIsNum) {
                    etValue.inputType = InputType.TYPE_CLASS_NUMBER
                }
                etValue.setText(mValue)
            }
            head_title.text = getString(R.string.personal_page)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            head_menu_tv.visibility = View.VISIBLE
            head_menu_tv.setOnClickListener {
                val intent = Intent()
                if (mIsSex) {
                    intent.putExtra(USER_VALUE, mSexEdit.toString())
                } else {
                    intent.putExtra(USER_VALUE, etValue.text.toString())
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

}
