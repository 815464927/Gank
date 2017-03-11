package com.song.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.song.gank.R;
import com.song.gank.model.Gank;
import com.song.gank.widege.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by song on 2017/3/4.
 * Email：815464927@qq.com
 */

public class XRecyclerViewAdapter extends RecyclerView.Adapter<XRecyclerViewAdapter.mViewHolder> {

    private Context mContext;
    private List<Gank> mList;

    public XRecyclerViewAdapter(Context context, List<Gank> list) {
        this.mContext = context;
        this.mList = list;
    }

    //回调监听
    public interface OnItemClickListener {
        void onIteClick(View view, int position);
    }
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewHolder viewHolder = new mViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.xrecyclerview_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        holder.tv.setText(mList.get(position).getDecs());
        Glide.with(mContext).load(mList.get(position).getUrl()).asBitmap().into(holder.iv);
        //设置回调 监听事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onIteClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //构造方法
    class mViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_xrecyclerview)
        TextView tv;
        @BindView(R.id.iv_item_xrecyclerview)
        RatioImageView iv;

        public mViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            iv.setOriginalSize(50,50);
        }
    }

}
