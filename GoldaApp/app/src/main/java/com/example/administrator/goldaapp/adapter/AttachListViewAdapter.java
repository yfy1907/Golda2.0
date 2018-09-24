package com.example.administrator.goldaapp.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.common.MyLogger;
import com.example.administrator.goldaapp.common.SyncImageLoader;
import com.example.administrator.goldaapp.staticClass.StaticMember;
import com.example.administrator.goldaapp.utils.StringUtil;

import org.apache.harmony.awt.internal.nls.Messages;

import java.util.ArrayList;
import java.util.Map;

/**
 * 选择附件ListView
 */
public class AttachListViewAdapter extends BaseAdapter {

    private Activity activity;

    private ListView mListView;
    private LayoutInflater inflater;
    private ArrayList<Map<String, String>> listData;

    private IDialogControl dialogControl;
    private IDialogControl dialog2Control;

    private SyncImageLoader syncImageLoader = new SyncImageLoader();

    @SuppressLint("WrongConstant")
    public AttachListViewAdapter(Activity activity, IDialogControl dialogControl,IDialogControl dialog2Control,
                                 ArrayList<Map<String, String>> paramList, ListView listView) {

        this.mListView = listView;
        this.activity = activity;
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

    public View getView(final int paramInt, View convertView, ViewGroup parent) {

        if (mListView == null) {
            mListView = (ListView) parent;
        }

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.attach_item, null);
        }

        viewHolder.tv_title = ((TextView) convertView.findViewById(R.id.tv_title));
        viewHolder.iv_add_attach = ((ImageView) convertView.findViewById(R.id.iv_add_attach));
        viewHolder.iv_view_small = ((ImageView) convertView.findViewById(R.id.iv_view_small));
        convertView.setTag(paramInt);

        // 设置添加附件标题
        viewHolder.tv_title.setText(listData.get(paramInt).get("title"));
        // 设置添加附件点击
        viewHolder.iv_add_attach.setOnClickListener(new AddFileImageOnClickListener(paramInt));
        // 设置点击缩略图删除
        viewHolder.iv_view_small.setOnClickListener(new RemoveFileImageOnClickListener(paramInt));

        return convertView;
    }


    public class ViewHolder {
        private TextView tv_title;
        private ImageView iv_add_attach;
        private ImageView iv_view_small;
    }

    public void update(int index, ListView listview){
        //得到第一个可见item项的位置
        int visiblePosition = listview.getFirstVisiblePosition();
        //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
        View view = listview.getChildAt(index - visiblePosition);
        ImageView iv_view_small = ((ImageView) view.findViewById(R.id.iv_view_small));
        setData(iv_view_small,index);

//        ViewHolder holder = (ViewHolder) view.getTag();
//        holder.iv_view_small =((ImageView) view.findViewById(R.id.iv_view_small));
//        holder.iv_view_small.setVisibility(View.VISIBLE);
//        setData(holder,index);
    }

    private void setData(ImageView imageview,int index){
        String imageUrl = listData.get(index).get("file_name");
        if(!StringUtil.isEmpty(imageUrl)){
            MyLogger.Log().d("## 加载图片地址====="+imageUrl);
            if(!imageUrl.contains(StaticMember.ImageURL)){
                syncImageLoader.loadImage(index,StaticMember.ImageURL+imageUrl,imageLoadListener);
            }else{
                syncImageLoader.loadImage(index,imageUrl,imageLoadListener);
            }
            imageview.setVisibility(View.VISIBLE);
        }else{
            imageview.setVisibility(View.GONE);
        }
    }

    SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener(){

        @Override
        public void onImageLoad(Integer t, Drawable drawable) {
//            ViewHolder holder = (ViewHolder) getItem(t);
            View view = mListView.findViewWithTag(t);
            if(view != null){
                MyLogger.Log().i("=-=============加载图片了。。。");
                ImageView iv = (ImageView) view.findViewById(R.id.iv_view_small);
                iv.setVisibility(View.VISIBLE);
                iv.setBackground(drawable);
            }
        }
        @Override
        public void onError(Integer t) {
//            ViewHolder holder = (ViewHolder) getItem(t);
            View view = mListView.findViewWithTag(t);
            if(view != null){
                MyLogger.Log().i("=-=============加载图错误了。。。");
                ImageView iv = (ImageView) view.findViewById(R.id.iv_view_small);
                iv.setVisibility(View.GONE);
            }
        }
    };

    public void loadImage(){
        int start = mListView.getFirstVisiblePosition();
        int end =mListView.getLastVisiblePosition();
        if(end >= getCount()){
            end = getCount() -1;
        }
        syncImageLoader.setLoadLimit(start, end);
        syncImageLoader.unlock();
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    Log.i("","##SCROLL_STATE_FLING==="+SCROLL_STATE_FLING);
                    syncImageLoader.lock();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    Log.i("","##SCROLL_STATE_IDLE==="+SCROLL_STATE_IDLE);
                    loadImage();
                    //loadImage();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    syncImageLoader.lock();
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub

        }
    };

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
