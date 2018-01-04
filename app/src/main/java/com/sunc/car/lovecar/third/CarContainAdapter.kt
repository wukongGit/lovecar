package com.sunc.car.lovecar.third

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemCarContainBinding
import com.sunc.car.lovecar.databinding.ItemCarModelBinding
import com.sunc.car.lovecar.yun.ApiCarModel

/**
 * Created by Administrator on 2017/12/11.
 */
class CarContainAdapter(private val mList: List<ApiCarModel>): BaseBindingAdapter<ItemCarContainBinding>() {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemCarContainBinding> {
        return DataBoundViewHolder(ItemCarContainBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemCarContainBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}