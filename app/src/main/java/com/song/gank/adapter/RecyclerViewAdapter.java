package com.song.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.song.gank.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by song on 2017/2/4.
 * Email：815464927@qq.com
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.mViewHolder> {

    private Context mContext;
    private List<String> mList;

    public RecyclerViewAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    //增加
    public void addData(int position) {
        mList.add(position, "增加");
        notifyItemInserted(position);
        notifyItemChanged(position,mList.size());
    }

    //删除
    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mList.size());
    }

    //回调监听
    public interface OnItemClickListener {
        void onIteClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewHolder viewHolder = new mViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.flagment1_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
        //设置回调 监听事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onIteClick(view,pos);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(view,pos);
                    return false;
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
        @BindView(R.id.tv_item_flagment1)
        TextView tv;

        public mViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}

