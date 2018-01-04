package com.sunc.car.lovecar.third

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemCarModelBinding
import com.sunc.car.lovecar.yun.ApiCarModel

/**
 * Created by Administrator on 2017/12/11.
 */
class CarModelAdapter(private val mList: List<ApiCarModel>): BaseBindingAdapter<ItemCarModelBinding>() {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemCarModelBinding> {
        return DataBoundViewHolder(ItemCarModelBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemCarModelBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}