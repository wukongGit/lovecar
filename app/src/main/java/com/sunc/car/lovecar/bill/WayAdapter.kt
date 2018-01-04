package com.sunc.car.lovecar.bill

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemWayBinding
import com.sunc.car.lovecar.my.ServiceItem

/**
 * Created by Administrator on 2017/11/22.
 */
class WayAdapter(private val mList: List<ServiceItem>): BaseBindingAdapter<ItemWayBinding>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemWayBinding> {
        return DataBoundViewHolder(ItemWayBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemWayBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}