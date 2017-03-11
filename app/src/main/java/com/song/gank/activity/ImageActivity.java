package com.song.gank.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.song.gank.R;
import com.song.gank.consts.Consts;
import com.song.gank.utils.ToastUtils;
import com.song.gank.widege.ZoomImageView;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.iv_fullscreen)
    ZoomImageView iv;
    private String fileName = null;
    private PopupWindow popuWindow;
    private TextView tv_share,tv_save;
    private String imgeUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        imgeUrl = getIntent().getStringExtra(Consts.BIG_IMAGE);
        fileName = getIntent().getStringExtra(Consts.BIG_DES);
        Glide.with(this).load(imgeUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        initPopuWidege();
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                popuWindow.showAtLocation(iv, Gravity.CENTER,0,0);
                return true;
            }
        });
    }

    /**初始化popu*/
    private void initPopuWidege() {
        View popuView = View.inflate(this,R.layout.popu_view,null);
        popuWindow = new PopupWindow();
        registerListener(popuView);
        popuWindow.setContentView(popuView);
        popuWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popuWindow.setFocusable(true);
        popuWindow.setOutsideTouchable(true);
        popuWindow.setTouchable(true);
        //不设置此属性,OnDismisslistener触发不了
        popuWindow.setBackgroundDrawable(new ColorDrawable(000000));

    }

    /**注册popu的监听事件*/
    private void registerListener(View popuView) {
        tv_share = (TextView) popuView.findViewById(R.id.tv_share);
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popuWindow.dismiss();
                Glide.with(ImageActivity.this).load(imgeUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        loadImageForBitmap(resource,true);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        ToastUtils.showToast(ImageActivity.this,"请再试试");
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
            }
        });
        tv_save = (TextView) popuView.findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popuWindow.dismiss();
                Glide.with(ImageActivity.this).load(imgeUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        loadImageForBitmap(resource,false);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        ToastUtils.showToast(ImageActivity.this,"请再试试");
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
            }
        });
    }

    /**
     * 分享图片和保存图片
     * @param bitmap 图片的资源bitmap
     * @param isShare ture:分享图片 false:保存图片
     */
    private void loadImageForBitmap(Bitmap bitmap, Boolean isShare){
        //下载图片
        File file = new File(Consts.appPath,fileName+".jpg");
        Uri uri = Uri.fromFile(file);
        if(!file.exists()) {
            try {
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if(!isShare){
                ToastUtils.showToast(this,"图片已存在");
                //通知系统图库更新Uri
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                sendBroadcast(intent);
                return;
            }
        }
        if(isShare) {
            showShare(uri);
        }else{
            ToastUtils.showToast(this,"图片已保存"+ Consts.appPath);
            //通知系统图库更新Uri
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            sendBroadcast(intent);
        }
    }

    /**
     * 分享图片
     * @param uri 图片的路径
     */
    private void showShare(Uri uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        startActivity(intent.createChooser(intent,"分享到"));
    }
}
