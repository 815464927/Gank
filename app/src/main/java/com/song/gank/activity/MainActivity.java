package com.song.gank.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.song.gank.R;
import com.song.gank.adapter.XRecyclerViewAdapter;
import com.song.gank.consts.Consts;
import com.song.gank.model.Gank;
import com.song.gank.widege.SpacesItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    private List<Gank> gankData;
    private XRecyclerViewAdapter mAdapter;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        getData(count);
        registerListener();
    }


    private void initData() {
        ButterKnife.bind(this);
        gankData = new ArrayList<>();
        mAdapter = new XRecyclerViewAdapter(this,gankData);

        //瀑布流形式
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //增加item间的间隔
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        mRecyclerView.addHeaderView(header);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void registerListener() {

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener(){

            @Override
            public void onRefresh() {
                getData(1);
            }
            @Override
            public void onLoadMore() {
                getData(++count);
            }
        });

        mAdapter.setmOnItemClickListener(new XRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onIteClick(View view, int position) {
                showmDialog(position);
            }
        });
    }


    private void showmDialog(final int position){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("选一个呗");
        normalDialog.setMessage("先看美女呢,还是先学习呢？");
        normalDialog.setPositiveButton("学习",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoStudy(position);
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("看图",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoSeekMeiZhi(position);
                        dialog.dismiss();
                    }
                });
        normalDialog.show();
    }

    /**学习*/
    private void gotoStudy(int position){
        Intent intent = new Intent(MainActivity.this,GankDayActivity.class);
        intent.putExtra(Consts.GANK_DAY,gankData.get(position).getTime());
        startActivity(intent);
    }

    /**看图*/
    private void gotoSeekMeiZhi(int position){
        Intent intent = new Intent(MainActivity.this,ImageActivity.class);
        intent.putExtra(Consts.BIG_IMAGE,gankData.get(position).getUrl());
        intent.putExtra(Consts.BIG_DES,gankData.get(position).getDecs());
        startActivity(intent);
    }

    private void getData(final int count) {
        if(count!=0)
            this.count = count;
        //http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1(福利图片)
        OkHttpUtils.get().url(Consts.gankUrl+count)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(count == 1){
                    mRecyclerView.refreshComplete();
                }else{
                    mRecyclerView.loadMoreComplete();
                }
            }
            @Override
            public void onResponse(String response, int id) {
                //LogUtils.debug("gank", response);
                try {
                    if (count==1){
                        gankData.clear();
                    }
                    JSONObject object = new JSONObject(response);
                    JSONArray jarray = object.getJSONArray("results");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject item = jarray.getJSONObject(i);
                        String desc = item.getString("desc");
                        String url = item.getString("url");
                        String who = item.getString("who");
                        String time = item.getString("publishedAt").substring(0,10).replace("-","/");
                        gankData.add(new Gank(desc,url,who,time));
                        if(count == 1){
                            mRecyclerView.refreshComplete();
                        }else{
                            mRecyclerView.loadMoreComplete();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
