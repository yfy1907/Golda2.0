package com.example.administrator.goldaapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.adapter.BoradRecyclerViewAdapter;
import com.example.administrator.goldaapp.bean.BoardBean;
import com.example.administrator.goldaapp.staticClass.StaticMember;
import com.example.administrator.goldaapp.utils.HttpTools;

import java.util.ArrayList;
import java.util.List;

public class FragmentBoard extends BaseFragment {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    private Activity activity;


    private RecyclerView recycle_board;
    private LinearLayoutManager layoutManager ;
//    private BoradListViewAdapter boradListViewAdapter;

    private BoradRecyclerViewAdapter boradRecyclerViewAdapter;
    private List<BoardBean> listdata;
    private TextView text_no_data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        View view = inflater.inflate(R.layout.activity_fragment_board, container,false);
        // 加载fragment的布局控件（通过layout根元素加载)
        // 绑定组件
        recycle_board = (RecyclerView) view.findViewById(R.id.recycle_board);
        text_no_data = (TextView) view.findViewById(R.id.text_no_data);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Log.i("","222222222222。。。。。");


        listdata = new ArrayList<BoardBean>();
        for(int i=0; i<30; i++){
            BoardBean board = new BoardBean();
            board.setAddress("地址++++"+i);
            board.setCompany("公司+++++"+i);
            board.setDateline("2018-08-09");
            board.setIcon_class("类型");
            board.setIcon_cnname("xxxxxxxxxxxxxxxxxx");
            board.setIcon_type("type ");
            board.setState_ba("1");
            board.setState_hc("2");
            board.setState_sh("3");
            board.setState_sl("4");
            listdata.add(board);
        }

        layoutManager = new LinearLayoutManager(this.activity);
        recycle_board.setLayoutManager(layoutManager);
        boradRecyclerViewAdapter = new BoradRecyclerViewAdapter(activity,listdata);
        recycle_board.setAdapter(boradRecyclerViewAdapter);
        if(listdata.size() == 0){
            text_no_data.setVisibility(View.VISIBLE);
        }else{
            text_no_data.setVisibility(View.GONE);
        }
        searchAndShow("江苏");
    }


    public void searchAndShow(final String query) {
        Log.i("","## 11111111111111111111111查询巡查广告牌。。。。。");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    
                    Log.e("查到的广告数", listdata.size() + "个");
                    layoutManager = new LinearLayoutManager(activity);
                    recycle_board.setLayoutManager(layoutManager);
                    boradRecyclerViewAdapter = new BoradRecyclerViewAdapter(activity,listdata);
                    recycle_board.setAdapter(boradRecyclerViewAdapter);
                    if(listdata.size() == 0){
                        text_no_data.setVisibility(View.VISIBLE);
                    }else{
                        text_no_data.setVisibility(View.GONE);
                    }
//                    Snackbar sn = Snackbar.make(toolbar, "共查找到" + listdata.size() + "个广告牌", BaseTransientBottomBar.LENGTH_SHORT);
//                    CommonTools.setSnackbarMessageTextColor(sn, getResources().getColor(R.color.orange));
//                    sn.show();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("","## 查询巡查广告牌。。。。。");
                listdata = HttpTools.getJson(StaticMember.URL + "mob_search.php", "query=" + query, StaticMember.BOARD_LIST);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
