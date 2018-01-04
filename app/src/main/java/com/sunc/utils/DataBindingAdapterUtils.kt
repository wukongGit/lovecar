package com.sunc.utils

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sunc.car.lovecar.R

/**
 * Created by Administrator on 2017/11/22.
 */
@BindingAdapter("load_asset")
fun loadAsset(imageView: ImageView, id:Int) =
        Glide.with(imageView.context).load(id).into(imageView)

@BindingAdapter("load_asset")
fun loadAsset(imageView: ImageView, id:String) =
        Glide.with(imageView.context).load(AndroidUtils.getIconByReflect(id)).into(imageView)

@BindingAdapter("load_bitmap")
fun loadBitmap(imageView: ImageView, bitmap :Bitmap) =
        imageView.setImageBitmap(bitmap)

@BindingAdapter("load_image")
fun loadImage(imageView: ImageView, url: String?) =
        Glide.with(imageView.context).load(url)
                .into(imageView)

@BindingAdapter("int_string")
fun int2String(textView: TextView, num: Int) {
    textView.text = num.toString().plus("m")
}
