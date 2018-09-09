package com.example.administrator.goldaapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.activity.GreenMarkerDetail;
import com.example.administrator.goldaapp.bean.BoardBean;
import com.example.administrator.goldaapp.utils.CommonTools;
import com.example.administrator.goldaapp.utils.DrawableTool;

import java.util.List;

public class BoradRecyclerViewAdapter extends RecyclerView.Adapter<BoradRecyclerViewAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<BoardBean> list;

    private Activity activity;
    @SuppressLint("WrongConstant")
    public BoradRecyclerViewAdapter(Activity activity, List<BoardBean> paramList) {
        this.list = paramList;
        this.activity = activity;
        this.inflater = ((LayoutInflater) activity.getSystemService("layout_inflater"));
    }

    @Override
    public BoradRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_board_listview_item,parent,false);
        BoradRecyclerViewAdapter.ViewHolder viewHolder = new BoradRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BoradRecyclerViewAdapter.ViewHolder holder, final int position) {
        BoardBean board = list.get(position);

        holder.text_company.setText(board.getCompany());
        holder.text_address.setText(board.getAddress());
        holder.text_board_type.setText(board.getIcon_type()+" "+board.getIcon_class()+" "+board.getIcon_cnname());
        //holder.text_dateline.setText(board.getDateline());

        if ("".equals(list.get(position).getDateline()) || null == list.get(position).getDateline())
            holder.text_dateline.setText("0000/00/00");
        else
            holder.text_dateline.setText(CommonTools.timeStamp2Date(list.get(position).getDateline(), "yyyy/MM/dd"));


        Log.e("TAG", "getItemCount: "+list.size() );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("","## xxx pos="+position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public CardView board_card;
        public TextView text_company;
        public TextView text_dateline;
        public TextView text_address;
        public TextView text_board_type;

        public TextView board_state_sl; // 受理状态
        public TextView board_state_hc; // 核查状态
        public TextView board_state_sh; // 审核状态
        public TextView board_state_ba; // 备案状态

        public Button btn_query;

        public ViewHolder(View itemView) {
            super(itemView);

            board_card = (CardView) itemView.findViewById(R.id.board_card);
            text_company = (TextView) itemView.findViewById(R.id.text_company);
            text_dateline = (TextView) itemView.findViewById(R.id.text_dateline);
            text_address = (TextView) itemView.findViewById(R.id.text_address);
            text_board_type = (TextView) itemView.findViewById(R.id.text_board_type);

            board_state_sl = (TextView) itemView.findViewById(R.id.board_state_sl);
            board_state_hc = (TextView) itemView.findViewById(R.id.board_state_hc);
            board_state_sh = (TextView) itemView.findViewById(R.id.board_state_sh);
            board_state_ba = (TextView) itemView.findViewById(R.id.board_state_ba);
            btn_query = (Button) itemView.findViewById(R.id.btn_query);
        }
    }


}
