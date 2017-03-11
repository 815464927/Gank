package com.song.gank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.song.gank.R;
import com.song.gank.activity.ImageActivity;
import com.song.gank.consts.Consts;
import com.song.gank.model.GankDay;
import com.song.gank.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by song on 2017/2/6.
 * Email：815464927@qq.com
 */

public class GankDayAdapter extends RecyclerView.Adapter<GankDayAdapter.mHolderView>{


    private static final int DELAY = 138;
    private int mLastPosition = -1;
    private Context mContext;
    private List<GankDay> mList;
    private GankDayAdapter.OnItemClickListener mOnItemClickListener;


    public void setmOnItemClickListener(GankDayAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public GankDayAdapter(Context context, List<GankDay> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public mHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        mHolderView mHolderView = new mHolderView(
                LayoutInflater.from(mContext).inflate(R.layout.gank_day_item,parent,false));
        return mHolderView;
    }

    @Override
    public void onBindViewHolder(mHolderView holder, final int position) {
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view,position);
                }
            });
        }

        holder.tv_type.setText(mList.get(position).getType());
        if(mList.get(position).isShowType()){
            if(holder.tv_type.getVisibility()!=View.VISIBLE){
                holder.tv_type.setVisibility(View.VISIBLE);
            }
        }else{
            if(holder.tv_type.getVisibility()!=View.GONE){
                holder.tv_type.setVisibility(View.GONE);
            }
        }

        String title_who = mList.get(position).getDesc()+mList.get(position).getWho();
        holder.tv_mesg.setText(title_who);
        showItemAnim(holder.tv_mesg,position);
        if(mList.get(position).getImages()!=null) {
            Glide.with(mContext).load(mList.get(position).getImages()[0])
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv);
            holder.iv.setOnClickListener(new onClick(position,0));
            if(mList.get(position).getImages().length>1) {
                //先移除子View避免重复添加
                if(holder.ll_arrayList.getChildCount()>1){
                    for (int i=1;i<holder.ll_arrayList.getChildCount();i++){
                        holder.ll_arrayList.removeViewAt(i);
                    }
                }
                //动态添加图片
                for (int i = 1; i < mList.get(position).getImages().length; i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setPadding(5, 5, 5, 5);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                            Utils.dip2px(mContext, 95), LinearLayout.MarginLayoutParams.MATCH_PARENT));
                    imageView.setOnClickListener(new onClick(position,i));
                    Glide.with(mContext).load(mList.get(position).getImages()[i])
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                    holder.ll_arrayList.addView(imageView);
                }
            }
        }
        holder.ll_arrayList.setVisibility(mList.get(position).getImages()!=null?View.VISIBLE:View.GONE);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //图片的监听事件
    class onClick implements View.OnClickListener {
        private int position, i;

        public onClick(int position, int i) {
            this.position = position;
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ImageActivity.class);
            intent.putExtra(Consts.BIG_IMAGE, mList.get(position).getImages()[i]);
            intent.putExtra(Consts.BIG_DES, mList.get(position).getDesc());
            mContext.startActivity(intent);
        }
    }


    class mHolderView extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.iv_image)
        ImageView iv;
        @BindView(R.id.ll_arraylist)
        LinearLayout ll_arrayList;
        @BindView(R.id.tv_mes)
        TextView tv_mesg;

        public mHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //动画效果
    public void showItemAnim(final View view, final int position) {
        if (position > mLastPosition) {
            view.setAlpha(0);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_in_right);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setAlpha(1);
                        }


                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }


                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    view.startAnimation(animation);
                }
            }, DELAY * position);
            mLastPosition = position;
        }
    }

}
