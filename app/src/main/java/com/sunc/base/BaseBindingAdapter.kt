package com.sunc.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by Administrator on 2017/11/14.
 */
abstract class BaseBindingAdapter<B : ViewDataBinding> : RecyclerView.Adapter<DataBoundViewHolder<B>>() {
    var mListener: ((pos: Int) -> Unit)? = null
    var mLongClickListener: ((pos: Int) -> Unit)? = null

    override fun onBindViewHolder(holder: DataBoundViewHolder<B>, position: Int) {
        holder.binding.root.setOnClickListener {
            mListener?.invoke(holder.adapterPosition)
        }
        holder.binding.root.setOnLongClickListener {
            mLongClickListener?.invoke(holder.adapterPosition)
            true
        }
    }

    fun setOnItemClickListener(listener: ((pos: Int) -> Unit)) {
        mListener = listener
    }

    fun setOnItemLongClickListener(listener: (pos: Int) -> Unit) {
        mLongClickListener = listener
    }
}