package com.example.administrator.goldaapp.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.fragment.FragmentShenbao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 选择附件ListView
 */
public class AttachListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Map<String, String>> listData;

    private IDialogControl dialogControl;
    private IDialogControl dialog2Control;

    @SuppressLint("WrongConstant")
    public AttachListViewAdapter(Activity activity, IDialogControl dialogControl,IDialogControl dialog2Control, ArrayList<Map<String, String>> paramList) {
        this.listData = paramList;
        this.dialogControl = dialogControl;
        this.dialog2Control = dialog2Control;
        this.inflater = ((LayoutInflater) activity.getSystemService("layout_inflater"));
    }

    public int getCount() {
        return this.listData.size();
    }

    public Object getItem(int paramInt) {
        return this.listData.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null){
            paramView = this.inflater.inflate(R.layout.attach_item, null);
        }
        ((TextView) paramView.findViewById(R.id.tv_title)).setText(listData.get(paramInt).get("title").toString());
        TextView fileName = ((TextView) paramView.findViewById(R.id.tv_add_attach));
        fileName.setText(listData.get(paramInt).get("file_name").toString());
        fileName.setOnClickListener(new RemoveFileImageOnClickListener(paramInt));
        ((ImageView) paramView.findViewById(R.id.iv_add_attach)).setOnClickListener(new AddFileImageOnClickListener(paramInt));
        return paramView;
    }

    class RemoveFileImageOnClickListener implements View.OnClickListener{
        private int position;
        public RemoveFileImageOnClickListener(int pos){
            position = pos;
        }
        @Override
        public void onClick(View view) {
            dialog2Control.onShowDialog();// 显示对话框
            dialog2Control.getPosition(position);
        }
    }

    class AddFileImageOnClickListener implements View.OnClickListener{
        private int position;
        public AddFileImageOnClickListener(int pos){
            position = pos;
        }
        @Override
        public void onClick(View view) {
            dialogControl.onShowDialog();// 显示对话框
            dialogControl.getPosition(position);
        }
    }

    // 内部接口
    public interface IDialogControl {
        void onShowDialog();
        void getPosition(int position);
    }
}
