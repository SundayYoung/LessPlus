package com.felix.lessplus.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.felix.lessplus.di.module.GlideApp
import com.felix.lessplus.di.module.GlideOptions.bitmapTransform
import com.youth.banner.loader.ImageLoader
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Created by liuhaiyang on 2017/12/08.
 * 首页轮播图
 */

class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context, url: Any, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(imageView)
    }

    companion object {
        fun displayImage(context: Context, url: String?, imageView: ImageView) {
            Glide.with(context)
                    .load(url)
//                    .apply(RequestOptions().placeholder(R.drawable.img_two_bi_one))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(imageView)
        }

        fun displayImageWithBlur(context: Context, url: Any, imageView: ImageView) {
            GlideApp.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .apply(bitmapTransform( BlurTransformation(30, 6)))
                    .into(imageView)

        }

        fun callBackBitmap(context: Context, url: String?): RequestBuilder<Bitmap> {

            return Glide.with(context).asBitmap().load(url)
        }
    }
}
