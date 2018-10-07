package com.example.administrator.goldaapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.adapter.BoradRecyclerViewAdapter;
import com.example.administrator.goldaapp.adapter.EndlessRecyclerOnScrollListener;
import com.example.administrator.goldaapp.adapter.LoadMoreAdapter;
import com.example.administrator.goldaapp.bean.BoardBean;
import com.example.administrator.goldaapp.common.MyLogger;
import com.example.administrator.goldaapp.staticClass.StaticMember;
import com.example.administrator.goldaapp.utils.CommonTools;
import com.example.administrator.goldaapp.utils.HttpTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentBoard extends BaseFragment {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    private Activity activity;

    private List<BoardBean> listdata;
    private TextView text_no_data;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycleBoard;
    private LoadMoreAdapter loadMoreAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        View view = inflater.inflate(R.layout.activity_fragment_board, container,false);
        // 加载fragment的布局控件（通过layout根元素加载)
        // 绑定组件

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recycleBoard = (RecyclerView) view.findViewById(R.id.recycle_board);
        text_no_data = (TextView) view.findViewById(R.id.text_no_data);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        listdata = new ArrayList<BoardBean>();

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        getRecycleViewData();
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
//                listdata.clear();
//                loadMoreAdapter.notifyDataSetChanged();

                searchAndShow(StaticMember.USER.getUid());
                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        // 设置加载更多监听
        recycleBoard.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);

                // 显示加载到底的提示
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);

//                if (listdata.size() < 52) {
//                    searchAndShow(StaticMember.USER.getUid());
//                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
//                } else {
//                    // 显示加载到底的提示
//                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
//                }
            }
        });

        searchAndShow(StaticMember.USER.getUid());
    }

    private void getRecycleViewData(){
        loadMoreAdapter = new LoadMoreAdapter(this.activity,listdata);
        recycleBoard.setLayoutManager(new LinearLayoutManager(this.activity));
        recycleBoard.setAdapter(loadMoreAdapter);
        loadMoreAdapter.setOnItemClickListener(onOnItemClickListener);

        if(listdata.size() == 0){
            text_no_data.setVisibility(View.VISIBLE);
        }else{
            text_no_data.setVisibility(View.GONE);
        }
    }

    private LoadMoreAdapter.OnItemClickListener onOnItemClickListener = new LoadMoreAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view) {
//            Toast.makeText(activity, view.getTag() + "", 1000).show();

            int position = (int) view.getTag();
            showMessage("当前点击列表索引："+position);
            MyLogger.Log().i("## 当前点击列表索引："+position);
//            String de_id = listdata.get(position).getDe_id();
            goShenbaoFragment(listdata.get(position));
        }
    };

    private void showMessage(String message){
        Toast.makeText(this.activity,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        String key = getArguments().getString("key");
//        MyLogger.Log().i("#####------ key="+key);
//        if("refresh".equals(key)){
//            getRecycleViewData();
//        }
    }

    private void goShenbaoFragment(BoardBean boardBean){
//        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
//        Fragment shenbaoFragment = new FragmentShenbao();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("BoardBean", boardBean);
//        shenbaoFragment.setArguments(bundle);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content,shenbaoFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }


    public void onItemClick(View view, int postion) {
        String confirmState = listdata.get(postion).getConfirm_status();
        String de_id = listdata.get(postion).getDe_id();
        Log.i("","### confirmState="+confirmState+"; de_id=="+de_id);
    }

    public void searchAndShow(final String uid) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == StaticMember.REQUEST_BOARD_LIST_DATE_RESULT) {
                    
                    Log.e("查到的广告数", listdata.size() + "个");
                    getRecycleViewData();
                    if(null != baseToolbar){
                        Snackbar sn = Snackbar.make(baseToolbar, "共查找到" + listdata.size() + "个广告牌", BaseTransientBottomBar.LENGTH_SHORT);
                        CommonTools.setSnackbarMessageTextColor(sn, getResources().getColor(R.color.orange));
                        sn.show();
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                listdata = HttpTools.getJson(StaticMember.URL + "mob_declare.php", "uid=" + uid, StaticMember.BOARD_LIST);
                handler.sendEmptyMessage(StaticMember.REQUEST_BOARD_LIST_DATE_RESULT);
            }
        }).start();
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

}
