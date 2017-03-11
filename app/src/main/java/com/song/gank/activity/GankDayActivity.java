package com.song.gank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.song.gank.R;
import com.song.gank.adapter.GankDayAdapter;
import com.song.gank.consts.Consts;
import com.song.gank.model.GankDay;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class GankDayActivity extends AppCompatActivity {

    private String time = null;
    private List<GankDay> mData;
    private GankDayAdapter mAdapter;
    @BindView(R.id.rl_gank_day)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_day)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_day);
        ininData();
        registerListener();
    }


    private void ininData() {
        ButterKnife.bind(this);
        mData = new ArrayList<>();
        mAdapter = new GankDayAdapter(this,mData);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter);

        time = getIntent().getStringExtra(Consts.GANK_DAY);
        tv_title.setText(time);
        getData(Consts.gankDayUrl+time);
    }

    private void registerListener() {
        mAdapter.setmOnItemClickListener(new GankDayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GankDayActivity.this,WebActivity.class);
                intent.putExtra(Consts.GANK_WEB,mData.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    public void getData(String url) {
        //http://gank.io/api/day/2017/02/06
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                //LogUtils.debug("gank_day", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("results");
                    decoJsion(jsonObject2.getJSONArray("Android"));
                    decoJsion(jsonObject2.getJSONArray("iOS"));
                    if(jsonObject2.has("瞎推荐")) {
                        decoJsion(jsonObject2.getJSONArray("瞎推荐"));
                    }
                    if(jsonObject2.has("前端")) {
                        decoJsion(jsonObject2.getJSONArray("前端"));
                    }
                    if(jsonObject2.has("拓展资源")) {
                        decoJsion(jsonObject2.getJSONArray("拓展资源"));
                    }
                    if(jsonObject2.has("App")) {
                        decoJsion(jsonObject2.getJSONArray("App"));
                    }
                    mAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void decoJsion(JSONArray jsonArray){
        synchronized (this) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    boolean isShowType = i == 0;
                    GankDay gankDay;
                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                    String desc = (i + 1) + "、" + jsonObject3.getString("desc");
                    String url = jsonObject3.getString("url");
                    String who = "(" + jsonObject3.getString("who") + ")";
                    String type = jsonObject3.getString("type");
                    if (jsonObject3.has("images")) {
                        JSONArray jarryimages = jsonObject3.getJSONArray("images");
                        String[] images = new String[jarryimages.length()];
                        for (int j = 0; j < jarryimages.length(); j++) {
                            images[j] = (String) jarryimages.get(j);
                            //LogUtils.debug("images[" + j + "]", images[j]);
                        }
                        gankDay = new GankDay(desc, url, who, images, type, isShowType);
                        mData.add(gankDay);
                    } else {
                        gankDay = new GankDay(desc, url, who, type, isShowType);
                        mData.add(gankDay);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
