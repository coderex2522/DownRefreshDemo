package com.example.downrefreshdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/7/10 14:11
 * 描述:
 */
public class MainActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener,BGAOnItemChildClickListener {
    public static final int LOADING_DURATION = 2000;
    private Toolbar mToolbar;

    private SwipeRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;
    private List<RefreshModel> refreshModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        setTitle(R.string.gridview_demo);

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mDataRv = (RecyclerView) findViewById(R.id.rv_recyclerview_data);
        initData();

        mRefreshLayout.setDelegate(this);
        mAdapter = new SwipeRecyclerViewAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);

        mNewPageNumber = 0;
        mMorePageNumber = 0;
        mAdapter.setData(refreshModelList);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);

        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_moooc);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.imoocstyle);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        mDataRv.setLayoutManager(new LinearLayoutManager(this));
        mDataRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());
    }

    void initData(){
        refreshModelList = new ArrayList<>();
        RefreshModel refreshModel= new RefreshModel("hahahha","nihaoniaho");

        RefreshModel refreshModel1= new RefreshModel("hahahha","nihaoniaho");
        RefreshModel refreshModel2= new RefreshModel("hahahha","nihaoniaho");
        RefreshModel refreshModel3= new RefreshModel("hahahha","nihaoniaho");
        RefreshModel refreshModel4= new RefreshModel("hahahha","nihaoniaho");
        RefreshModel refreshModel5= new RefreshModel("hahahha","nihaoniaho");
        RefreshModel refreshModel6= new RefreshModel("hahahha","nihaoniaho");
        RefreshModel refreshModel7= new RefreshModel("hahahha","nihaoniaho");
        refreshModelList.add(refreshModel);
        refreshModelList.add(refreshModel1);
        refreshModelList.add(refreshModel2);
        refreshModelList.add(refreshModel3);
        refreshModelList.add(refreshModel4);
        refreshModelList.add(refreshModel5);
        refreshModelList.add(refreshModel6);
        refreshModelList.add(refreshModel7);
    }

    List<RefreshModel> newData(){

        List<RefreshModel> refreshModelList1 = new ArrayList<>();
        RefreshModel refreshModel= new RefreshModel("hahahha1","nihaoniaho");

        RefreshModel refreshModel1= new RefreshModel("hahahha1","nihaoniaho");
        RefreshModel refreshModel2= new RefreshModel("hahahha1","nihaoniaho");
        RefreshModel refreshModel3= new RefreshModel("hahahha1","nihaoniaho");
        RefreshModel refreshModel4= new RefreshModel("hahahha1","nihaoniaho");
        RefreshModel refreshModel5= new RefreshModel("hahahha1","nihaoniaho");
        RefreshModel refreshModel6= new RefreshModel("hahahha1","nihaoniaho");
        RefreshModel refreshModel7= new RefreshModel("hahahha1","nihaoniaho");
        refreshModelList1.add(refreshModel);
        refreshModelList1.add(refreshModel1);
        refreshModelList1.add(refreshModel2);
        refreshModelList1.add(refreshModel3);
        refreshModelList1.add(refreshModel4);
        refreshModelList1.add(refreshModel5);
        refreshModelList1.add(refreshModel6);
        refreshModelList1.add(refreshModel7);
        return refreshModelList1;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Toast.makeText(this,"点击了条目 " + mAdapter.getItem(position).title,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            Toast.makeText(this,"没有最新数据了",Toast.LENGTH_SHORT).show();
            return;
        }
        //加载数据用的;

        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endRefreshing();
                List<RefreshModel> list = newData();
                mAdapter.addNewData(list);
                mDataRv.smoothScrollToPosition(0);
            }
        }, MainActivity.LOADING_DURATION);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mMorePageNumber++;
        if (mMorePageNumber > 4) {
            mRefreshLayout.endLoadingMore();
            Toast.makeText(this,"没有更多数据了",Toast.LENGTH_SHORT).show();
            return false;
        }
        mRefreshLayout.endLoadingMore();
        List<RefreshModel> list = newData();
        mAdapter.addMoreData(list);
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endLoadingMore();
                List<RefreshModel> list = newData();
                mAdapter.addMoreData(list);
            }
        }, MainActivity.LOADING_DURATION);

        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            mAdapter.closeOpenedSwipeItemLayoutWithAnim();
            mAdapter.removeItem(position);
        }
    }
}
