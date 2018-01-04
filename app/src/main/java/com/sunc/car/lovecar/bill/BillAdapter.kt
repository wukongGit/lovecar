package com.sunc.car.lovecar.bill

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.databinding.ItemBillBinding

/**
 * Created by Administrator on 2017/11/14.
 */
class BillAdapter(private val mList: List<Bill>) : BaseBindingAdapter<ItemBillBinding>() {
    private var mMonthList: List<MonthBill> ? = null

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemBillBinding> {
        return DataBoundViewHolder(ItemBillBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemBillBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = mList[position]
        val month = item.month
        holder.binding.monthItem = null
        if (position == 0) {
            bindMonth(month, holder)
        } else {
            val lastMonth = mList[position - 1].month
            if (month != lastMonth) {
                bindMonth(month, holder)
            }
        }
        holder.binding.item = item
        holder.binding.executePendingBindings()
    }

    fun setMonthList(monthList: List<MonthBill>) {
        mMonthList = monthList
    }

    private fun bindMonth(month: String, holder: DataBoundViewHolder<ItemBillBinding>) {
        if (mMonthList != null) {
            for (m in mMonthList!!) {
                if (month == m.month) {
                    holder.binding.monthItem = m
                    return
                }
            }
        }
    }
}