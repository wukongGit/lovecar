package com.sunc.car.lovecar.login

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.databinding.ItemCarBinding

/**
 * Created by Administrator on 2017/11/27.
 */
class CarAdapter(private val mList: List<Car>): BaseBindingAdapter<ItemCarBinding>() {
    var mEditListener: ((pos: Int) -> Unit)? = null

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemCarBinding> {
        return DataBoundViewHolder(ItemCarBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemCarBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.ivEdit.setOnClickListener {
            mEditListener?.invoke(holder.adapterPosition)
        }
        holder.binding.executePendingBindings()
    }

    fun setOnItemEditListener(listener: ((pos: Int) -> Unit)) {
        mEditListener = listener
    }
}