package com.felix.lessplus.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.felix.lessplus.R
import com.felix.lessplus.model.bean.MusicResponse
import com.felix.lessplus.utils.GlideImageLoader

/**
 * Created by liuhaiyang on 2017/12/13.
 */

class MusicMultiAdapter constructor(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEAD = 1000
    private val TYPE_TITLE = 0
    private val TYPE_IMG_ONE = 1
    private val TYPE_IMG_TWO = 2
    private val TYPE_IMG_THREE = 3
    private val TYPE_IMG_SIX = 6

    private var mHeaderView: View? = null
    private var mMusicList: List<MusicResponse>? = null
    private var mListener : OnItemImgClickListener? = null

    fun addListener(listener: OnItemImgClickListener) {
        this.mListener = listener
    }

    fun setImgClick(type: Int) {
        mListener?.onImgClick(type)
    }

    fun setHeaderView(headerView: View) {
        mHeaderView = headerView
        notifyItemInserted(0)
    }

    fun setAdapterData(lists: List<MusicResponse>) {
        this.mMusicList = lists
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        //head
        if (position == 0) {
            return TYPE_HEAD
        }

        val pos = getRealPosition(position)
        val response = mMusicList!![pos]
        if (!TextUtils.isEmpty(response.title)) {
            return TYPE_TITLE
        }
        if (response.albumList!!.size == 1) {
            return TYPE_IMG_ONE
        }
        if (response.albumList!!.size == 2) {
            return TYPE_IMG_TWO
        }
        if (response.albumList!!.size == 3) {
            return TYPE_IMG_THREE
        }
        if (response.albumList!!.size == 6) {
            return TYPE_IMG_SIX
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (mHeaderView != null && viewType == TYPE_HEAD) {
            return TitleViewHolder(mHeaderView!!)
        }else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_title, parent, false)
            return TitleViewHolder(view)
        } else if (viewType == TYPE_IMG_ONE) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_img_one, parent, false)
            return ImgOneViewHolder(view)
        } else if (viewType == TYPE_IMG_TWO) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_img_two, parent, false)
            return ImgTwoViewHolder(view)
        } else if (viewType == TYPE_IMG_THREE) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_img_three, parent, false)
            return ImgThreeViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_img_six, parent, false)
            return ImgSixViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        //head
        if(getItemViewType(pos) == TYPE_HEAD) return

        val position = getRealPosition(pos)

        val response = mMusicList!![position]
        when (holder) {
            is TitleViewHolder -> holder.mTvTitle.text = response.title
            is ImgOneViewHolder -> {
                holder.mTvOne.text = response.albumList!![0].content
                GlideImageLoader.displayImage(mContext, response.albumList!![0].url!!, holder.mIvOne)
                holder.mIvOne.setOnClickListener { setImgClick(response.albumList!![0].type)}
            }
            is ImgTwoViewHolder -> {

                holder.mTvOne.text = response.albumList!![0].content
                GlideImageLoader.displayImage(mContext, response.albumList!![0].url!!, holder.mIvOne)
                holder.mIvOne.setOnClickListener { setImgClick(response.albumList!![0].type)}

                holder.mTvTwo.text = response.albumList!![1].content
                GlideImageLoader.displayImage(mContext, response.albumList!![1].url!!, holder.mIvTwo)
                holder.mIvTwo.setOnClickListener { setImgClick(response.albumList!![1].type)}
            }
            is ImgThreeViewHolder -> {

                holder.mTvOne.text = response.albumList!![0].content
                GlideImageLoader.displayImage(mContext, response.albumList!![0].url!!, holder.mIvOne)
                holder.mIvOne.setOnClickListener { setImgClick(response.albumList!![0].type)}

                holder.mTvTwo.text = response.albumList!![1].content
                GlideImageLoader.displayImage(mContext, response.albumList!![1].url!!, holder.mIvTwo)
                holder.mIvTwo.setOnClickListener { setImgClick(response.albumList!![1].type)}

                holder.mTvThree.text = response.albumList!![2].content
                GlideImageLoader.displayImage(mContext, response.albumList!![2].url!!, holder.mIvThree)
                holder.mIvThree.setOnClickListener { setImgClick(response.albumList!![2].type)}
            }
            is ImgSixViewHolder -> {

                holder.mTvOne.text = response.albumList!![0].content
                GlideImageLoader.displayImage(mContext, response.albumList!![0].url!!, holder.mIvOne)
                holder.mIvOne.setOnClickListener { setImgClick(response.albumList!![0].type)}

                holder.mTvTwo.text = response.albumList!![1].content
                GlideImageLoader.displayImage(mContext, response.albumList!![1].url!!, holder.mIvTwo)
                holder.mIvTwo.setOnClickListener { setImgClick(response.albumList!![1].type)}

                holder.mTvThree.text = response.albumList!![2].content
                GlideImageLoader.displayImage(mContext, response.albumList!![2].url!!, holder.mIvThree)
                holder.mIvThree.setOnClickListener { setImgClick(response.albumList!![2].type)}

                holder.mTvFour.text = response.albumList!![3].content
                GlideImageLoader.displayImage(mContext, response.albumList!![3].url!!, holder.mIvFour)
                holder.mIvFour.setOnClickListener { setImgClick(response.albumList!![3].type)}

                holder.mTvFive.text = response.albumList!![4].content
                GlideImageLoader.displayImage(mContext, response.albumList!![4].url!!, holder.mIvFive)
                holder.mIvFive.setOnClickListener { setImgClick(response.albumList!![4].type)}

                holder.mTvSix.text = response.albumList!![5].content
                GlideImageLoader.displayImage(mContext, response.albumList!![5].url!!, holder.mIvSix)
                holder.mIvSix.setOnClickListener { setImgClick(response.albumList!![5].type)}
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mMusicList == null) 0 else if (mHeaderView == null) mMusicList!!.size else mMusicList!!.size + 1
    }


    private fun getRealPosition(position: Int): Int {
        return if (mHeaderView == null) position else position - 1
    }

    internal inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var mTvTitle: TextView

        init {
            if (itemView == mHeaderView) {
            } else{
                mTvTitle = itemView.findViewById(R.id.tv_title)
            }

        }
    }

    internal inner class ImgOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mIvOne: ImageView
        var mTvOne: TextView

        init {
            mIvOne = itemView.findViewById(R.id.iv_titlepage)
            mTvOne = itemView.findViewById(R.id.tv_content)
        }
    }

    internal inner class ImgTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mIvOne: ImageView
        var mIvTwo: ImageView
        var mTvOne: TextView
        var mTvTwo: TextView

        init {
            mIvOne = itemView.findViewById(R.id.iv_titlepage_one)
            mIvTwo = itemView.findViewById(R.id.iv_titlepage_two)
            mTvOne = itemView.findViewById(R.id.tv_content_one)
            mTvTwo = itemView.findViewById(R.id.tv_content_two)
        }
    }

    internal inner class ImgThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mIvOne: ImageView
        var mIvTwo: ImageView
        var mIvThree: ImageView
        var mTvOne: TextView
        var mTvTwo: TextView
        var mTvThree: TextView

        init {
            mIvOne = itemView.findViewById(R.id.iv_titlepage_one)
            mIvTwo = itemView.findViewById(R.id.iv_titlepage_two)
            mIvThree = itemView.findViewById(R.id.iv_titlepage_three)
            mTvOne = itemView.findViewById(R.id.tv_content_one)
            mTvTwo = itemView.findViewById(R.id.tv_content_two)
            mTvThree = itemView.findViewById(R.id.tv_content_three)
        }
    }

    internal inner class ImgSixViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mIvOne: ImageView
        var mIvTwo: ImageView
        var mIvThree: ImageView
        var mIvFour: ImageView
        var mIvFive: ImageView
        var mIvSix: ImageView
        var mTvOne: TextView
        var mTvTwo: TextView
        var mTvThree: TextView
        var mTvFour: TextView
        var mTvFive: TextView
        var mTvSix: TextView

        init {
            mIvOne = itemView.findViewById(R.id.iv_titlepage_one)
            mIvTwo = itemView.findViewById(R.id.iv_titlepage_two)
            mIvThree = itemView.findViewById(R.id.iv_titlepage_three)
            mIvFour = itemView.findViewById(R.id.iv_titlepage_four)
            mIvFive = itemView.findViewById(R.id.iv_titlepage_five)
            mIvSix = itemView.findViewById(R.id.iv_titlepage_six)

            mTvOne = itemView.findViewById(R.id.tv_content_one)
            mTvTwo = itemView.findViewById(R.id.tv_content_two)
            mTvThree = itemView.findViewById(R.id.tv_content_three)
            mTvFour = itemView.findViewById(R.id.tv_content_four)
            mTvFive = itemView.findViewById(R.id.tv_content_five)
            mTvSix = itemView.findViewById(R.id.tv_content_six)
        }
    }


    interface OnItemImgClickListener {
        fun onImgClick(type : Int)
    }
}
