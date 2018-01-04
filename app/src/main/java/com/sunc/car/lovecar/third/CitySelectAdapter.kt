package com.sunc.car.lovecar.third

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemCityBinding
import com.sunc.car.lovecar.yun.ApiCityModel

/**
 * Created by Administrator on 2017/12/11.
 */
class CitySelectAdapter(private val mList: List<ApiCityModel>): BaseBindingAdapter<ItemCityBinding>() {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemCityBinding> {
        return DataBoundViewHolder(ItemCityBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemCityBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position].cityname
        holder.binding.executePendingBindings()
    }
}