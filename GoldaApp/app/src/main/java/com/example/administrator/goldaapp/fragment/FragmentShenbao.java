package com.example.administrator.goldaapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.adapter.SpinnerCityAdapter;
import com.example.administrator.goldaapp.bean.SpinnerCityListItem;
import com.example.administrator.goldaapp.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentShenbao extends BaseFragment {

    /** Called when the activity is first created. */
    private DBManager dbm;
    private SQLiteDatabase db;
    private Spinner spinner1 = null;
    private Spinner spinner2=null;
    private Spinner spinner3=null;
    private String province=null;
    private String city=null;
    private String district=null;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_shenbao, container,false);
        // 加载fragment的布局控件（通过layout根元素加载)
        spinner1=(Spinner)rootView.findViewById(R.id.spinner1);
        spinner2=(Spinner)rootView.findViewById(R.id.spinner2);
        spinner3=(Spinner)rootView.findViewById(R.id.spinner3);
        spinner1.setPrompt("省");
        spinner2.setPrompt("城市");
        spinner3.setPrompt("地区");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initSpinner1();
    }

    public void initSpinner1(){
        dbm = new DBManager(context);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<SpinnerCityListItem> list = new ArrayList<SpinnerCityListItem>();

        try {
            String sql = "select * from province";
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while (!cursor.isLast()){
                String code=cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[]=cursor.getBlob(2);
                String name=new String(bytes,"gbk");
                SpinnerCityListItem myListItem=new SpinnerCityListItem();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code=cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[]=cursor.getBlob(2);
            String name=new String(bytes,"gbk");
            SpinnerCityListItem myListItem=new SpinnerCityListItem();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);
            if(null != cursor){
                cursor.close();
            }
        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        SpinnerCityAdapter myAdapter = new SpinnerCityAdapter(context,list);
        spinner1.setAdapter(myAdapter);
        spinner1.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
    }
    public void initSpinner2(String pcode){
        dbm = new DBManager(context);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<SpinnerCityListItem> list = new ArrayList<>();

        try {
            String sql = "select * from city where pcode='"+pcode+"'";
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while (!cursor.isLast()){
                String code=cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[]=cursor.getBlob(2);
                String name=new String(bytes,"gbk");
                SpinnerCityListItem myListItem = new SpinnerCityListItem();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code=cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[]=cursor.getBlob(2);
            String name=new String(bytes,"gbk");
            SpinnerCityListItem myListItem=new SpinnerCityListItem();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        SpinnerCityAdapter myAdapter = new SpinnerCityAdapter(context,list);
        spinner2.setAdapter(myAdapter);
        spinner2.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
    }
    public void initSpinner3(String pcode){
        dbm = new DBManager(context);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<SpinnerCityListItem> list = new ArrayList<SpinnerCityListItem>();

        try {
            String sql = "select * from district where pcode='"+pcode+"'";
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while (!cursor.isLast()){
                String code=cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[]=cursor.getBlob(2);
                String name=new String(bytes,"gbk");
                SpinnerCityListItem myListItem=new SpinnerCityListItem();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code=cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[]=cursor.getBlob(2);
            String name=new String(bytes,"gbk");
            SpinnerCityListItem myListItem=new SpinnerCityListItem();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        SpinnerCityAdapter myAdapter = new SpinnerCityAdapter(context,list);
        spinner3.setAdapter(myAdapter);
        spinner3.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
    }

    class SpinnerOnSelectedListener1 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            province=((SpinnerCityListItem) adapterView.getItemAtPosition(position)).getName();
            String pcode =((SpinnerCityListItem) adapterView.getItemAtPosition(position)).getPcode();

            initSpinner2(pcode);
            initSpinner3(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }
    class SpinnerOnSelectedListener2 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            city=((SpinnerCityListItem) adapterView.getItemAtPosition(position)).getName();
            String pcode =((SpinnerCityListItem) adapterView.getItemAtPosition(position)).getPcode();

            initSpinner3(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }

    class SpinnerOnSelectedListener3 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            district=((SpinnerCityListItem) adapterView.getItemAtPosition(position)).getName();
            // Toast.makeText(context, province+" "+city+" "+district, Toast.LENGTH_LONG).show();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }

    // 定义一个消息处理handler
    private Handler mHandler = new Handler() {

        @SuppressLint("WrongConstant")
        @Override
        public void handleMessage(Message msg) {
            //
        }
    };




}
