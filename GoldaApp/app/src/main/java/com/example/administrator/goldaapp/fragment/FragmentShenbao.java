package com.example.administrator.goldaapp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.bean.JsonBean;
import com.example.administrator.goldaapp.common.JsonFileReader;
import com.example.administrator.goldaapp.common.MyLogger;
import com.example.administrator.goldaapp.utils.AssistUtil;
import com.example.administrator.goldaapp.utils.CaremaUtil;
import com.example.administrator.goldaapp.utils.FileUtil;
import com.example.administrator.goldaapp.adapter.AttachListViewAdapter;
import com.example.administrator.goldaapp.utils.StringUtil;
import com.example.administrator.goldaapp.view.MyDialogFileChose;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentShenbao extends BaseFragment implements MyDialogFileChose.OnButtonClickListener {

    /** Called when the activity is first created. */
    private final static String TAG = "FragmentShenbao";
    private Activity activity;
    private ViewPager viewPager = null;
    private List<View> viewContainter = new ArrayList<View>();   //存放容器
    private ViewPagerAdapter viewPagerAdapter = null;   //声明适配器
    private TabHost mTabHost = null;
    private TabWidget mTabWidget = null;

    // 省、市、区
    private ArrayList<JsonBean> options1Items = new ArrayList<JsonBean>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();


    // 广告牌类型
    private ArrayList<JsonBean> optionsType1Items = new ArrayList<JsonBean>();
    private ArrayList<ArrayList<String>> optionsType2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> optionsType3Items = new ArrayList<ArrayList<ArrayList<String>>>();

    private Unbinder unbinder;
    @BindView(R.id.tv_city_area)
    TextView tv_city_area;  // 省份、城市、地区
    @BindView(R.id.edittext_adress)
    EditText edittext_adress;   // 设置地点
    @BindView(R.id.edittext_area_line)
    EditText edittext_area_line;    // 路段
    @BindView(R.id.edittext_company)
    EditText edittext_company; //申请公司名称
    @BindView(R.id.edittext_company_address)
    EditText edittext_company_address;          // 公司地址
    @BindView(R.id.edittext_person)
    EditText edittext_person; // 法定代表人
    @BindView(R.id.edittext_contact)
    EditText edittext_contact; // 联系电话号码
    @BindView(R.id.edittext_process_contact)
    EditText edittext_process_contact; // 联系人
    @BindView(R.id.edittext_process_tel)
    EditText edittext_process_tel; // 联系人电话号码
    @BindView(R.id.edittext_email)
    EditText edittext_email; // 联系邮箱
    @BindView(R.id.tv_icon_type)
    TextView tv_icon_type; // 类型
    @BindView(R.id.edittext_material)
    EditText edittext_material; // 广告牌材质
    @BindView(R.id.edittext_wt)
    EditText edittext_wt; // 外凸(米)
    @BindView(R.id.edittext_model)
    EditText edittext_model; // 数量(个)
    @BindView(R.id.edittext_facenum)
    EditText edittext_facenum; // 展示面数(面)
    @BindView(R.id.edittext_ad_x)
    EditText edittext_ad_x; // 长度(米)
    @BindView(R.id.edittext_ad_y)
    EditText edittext_ad_y; // 宽度(米)
    @BindView(R.id.edittext_ad_s)
    EditText edittext_ad_s; // 面积(平方米)
    @BindView(R.id.edittext_li_height)
    EditText edittext_li_height; // 离地高度(米)

    /**
     * 附件上传ViewPage
     */
    private MyDialogFileChose dialog;// 图片选择对话框
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    private final int CHOSE_FILE = 5;
    private ListView addAttachListView;
    private AttachListViewAdapter attachListViewAdapter;
    private ArrayList<Map<String, String>> listAttachData;
    private String photofilePath;
    private File dir = new File(Environment.getExternalStorageDirectory(),"golda");//照片保存路径文件夹

    private int choseFileIndex = 0;
    private String path; // 选择文件路径



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

        View view = inflater.inflate(R.layout.activity_fragment_shenbao, container,false);
        // 加载fragment的布局控件（通过layout根元素加载)
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this.activity);
        initMyTabHost(view);

        // 绑定组件
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        initViewPagerContainter();  //初始viewPager
        viewPagerAdapter = new ViewPagerAdapter();
        //设置adapter的适配器
        viewPager.setAdapter(viewPagerAdapter);
        //设置viewPager的监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //当 滑动 切换时
            @Override
            public void onPageSelected(int position) {
                mTabWidget.setCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //TabHost的监听事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tab1")){
                    viewPager.setCurrentItem(0);
                }else{
                    viewPager.setCurrentItem(1);
                }
            }
        });

        //解决开始时不显示viewPager
        mTabHost.setCurrentTab(1);
        mTabHost.setCurrentTab(0);

        initJsonData();

        initAdTypeJsonData();


        /*
         * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        this.activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 锁定屏幕
        this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    // 设置监听
    private void setListener() {
        tv_city_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
            }
        });

        tv_icon_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerViewAdType();
            }
        });
    }

    /**
     * 选择省、市、区级联
     */
    private void showPickerView() {
        if(options1Items.size() == 0){
            Toast.makeText(this.activity,"数据未加载。",Toast.LENGTH_SHORT).show();
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +"<br/>"+
                        options2Items.get(options1).get(options2) +"<br/>"+
                        options3Items.get(options1).get(options2).get(options3);
                tv_city_area.setText(Html.fromHtml(text));
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(14)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 选择广告牌级联
     */
    private void showPickerViewAdType() {
        if(optionsType1Items.size() == 0){
            Toast.makeText(this.activity,"数据未加载。",Toast.LENGTH_SHORT).show();
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = optionsType1Items.get(options1).getPickerViewText() + "  \n"+
                        optionsType2Items.get(options1).get(options2) +"  \n"+
                        optionsType3Items.get(options1).get(options2).get(options3);
                tv_icon_type.setText(Html.fromHtml(text));
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(14)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(optionsType1Items, optionsType2Items, optionsType3Items);//三级选择器
        pvOptions.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    //初始化TabHost
    public void initMyTabHost(View view){
        //绑定id
        mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = mTabHost.getTabWidget();
        /**
         * newTabSpec（）   就是给每个Tab设置一个ID
         * setIndicator()   每个Tab的标题
         * setCount()       每个Tab的标签页布局
         */
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setContent(R.id.tab1).setIndicator("基本信息"));
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setContent(R.id.tab2).setIndicator("图片信息"));
    }


    //初始化viewPager
    public void initViewPagerContainter(){
        //建立两个view的样式，并找到他们
        View view_1 = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.fragment_shenbao_viewpage1,null);
        View view_2 = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.fragment_shenbao_viewpage2,null);

        //加入ViewPage的容器
        viewContainter.add(view_1);
        viewContainter.add(view_2);


        unbinder = ButterKnife.bind(this, view_1);

        // view2 初始化
        initViewPage2UI(view_2);


        // 设置选择省市区监听
        setListener();

    }

    private void initViewPage2UI(View view){
        dialog = new MyDialogFileChose(this.activity);
        dialog.setOnButtonClickListener(this);

        addAttachListView = (ListView) view.findViewById(R.id.addAttachListView);
        listAttachData = new ArrayList<>();

        String[] titleArray = new String[]{"设置申请书","公司营业执照","个人身份证明","效果图","实景图","规格平面图","产权证书或\n房屋租赁协议"
                ,"载体安全证明","相关书面协议","场地租用合同","结构设计图","施工图","施工说明书","建安资质证书","施工保证书","规划拍卖意见",
        "授权人身份证","授权委托书"};
        for(int i = 0; i < titleArray.length; i++ ){
            Map<String,String> map = new HashMap<>();
            map.put("title",titleArray[i]);
            map.put("file_path","");
            map.put("file_name","");
            map.put("file_key","b_attach_"+(i+1));
            map.put("file_id",""+i);
            listAttachData.add(map);
        }
        attachListViewAdapter = new AttachListViewAdapter(activity, AddFileDialogControl, RemoveFileDialogControl,listAttachData);
        addAttachListView.setAdapter(attachListViewAdapter);
    }

    private AttachListViewAdapter.IDialogControl AddFileDialogControl = new AttachListViewAdapter.IDialogControl() {
        @Override
        public void onShowDialog() {
            // TODO Auto-generated method stub
            dialog.show();
        }
        @Override
        public void getPosition(int position) {
            // TODO Auto-generated method stub
            choseFileIndex = position;
        }
    };

    private AttachListViewAdapter.IDialogControl RemoveFileDialogControl = new AttachListViewAdapter.IDialogControl() {
        @Override
        public void onShowDialog() {
            // TODO Auto-generated method stub
            removeFileDialog();
        }
        @Override
        public void getPosition(int position) {
            // TODO Auto-generated method stub
            choseFileIndex = position;
        }
    };

    /*
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */
    protected void removeFileDialog() {
        final int position = choseFileIndex;
        if("".equals(listAttachData.get(position).get("file_name"))){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确认移除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listAttachData.get(position).put("file_path","");
                listAttachData.get(position).put("file_name","");
                attachListViewAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 设置Android6.0的权限申请
     */
    private void setPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Android 6.0申请权限
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA} ,1);
            }else{
                Log.i("","权限申请ok");
                takePhoto();
            }
        }else {
            takePhoto();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }else {
                return;
            }
        }
    }

    @Override
    public void camera() {
        dialog.dismiss();
        setPermissions();
    }

    private void takePhoto(){
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        String filename = df.format(new Date());
        File currentImageFile  = new File(dir, filename + ".jpg");
        if (!currentImageFile .exists()){
            try {
                currentImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        photofilePath = currentImageFile.getAbsolutePath();//获取图片的绝对路径
        Log.e("", "#### photofilePath: "+photofilePath );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(intent, PHOTOHRAPH);
    }

    @Override
    public void gallery() {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOSE_FILE);
    }

    @Override
    public void choseFile() {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOSE_FILE);
    }

    @Override
    public void cancel() {
        dialog.cancel();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == NONE)
            return;

        Log.i("","## choseFileIndex===="+choseFileIndex);

        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 检查SDCard是否可用
            if(!AssistUtil.ExistSDCard()){
                Log.i("", "SD card 不可用！");
                Toast.makeText(activity, "SD卡不可用！", Toast.LENGTH_SHORT).show();
                return;
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(photofilePath);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inSampleSize = 4;
            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(fis,null, opts);
            try {
                if(null != fis){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            MyLogger.Log().i("## 111压缩前图片大小："+ CaremaUtil.getBitmapSize(bitmap));
            Bitmap newPhoto = CaremaUtil.compressImage(bitmap,photofilePath);
            MyLogger.Log().i("## 222压缩后图片大小："+ CaremaUtil.getBitmapSize(newPhoto));
            //iv_image.setImageBitmap(newPhoto);

            String fileName = FileUtil.getFileNameByPath(photofilePath);
            listAttachData.get(choseFileIndex).put("file_path",photofilePath);
            listAttachData.get(choseFileIndex).put("file_name",fileName);
            MyLogger.Log().i("## file_name："+ fileName);
            attachListViewAdapter.notifyDataSetChanged();
            return;
        }
        if (data == null)
            return;

        // 选择文件
        if(requestCode == CHOSE_FILE){
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                String fileName = uri.getScheme();
                listAttachData.get(choseFileIndex).put("file_path",path);
                listAttachData.get(choseFileIndex).put("file_name",fileName);
                attachListViewAdapter.notifyDataSetChanged();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = FileUtil.getPath(activity, uri);

                String fileName = FileUtil.getFileNameByPath(path);
                listAttachData.get(choseFileIndex).put("file_path",path);
                listAttachData.get(choseFileIndex).put("file_name",fileName);
                attachListViewAdapter.notifyDataSetChanged();
                // Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = FileUtil.getRealPathFromURI(uri);
                listAttachData.get(choseFileIndex).put("file_path",path);

                String fileName = FileUtil.getFileNameByPath(path);
                listAttachData.get(choseFileIndex).put("file_name",fileName);
                attachListViewAdapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    //内部类实现viewpager的适配器
    private class ViewPagerAdapter extends PagerAdapter {

        //该方法 决定 并 返回 viewpager中组件的数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //滑动切换的时候，消除当前组件
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewContainter.get(position));
        }

        //每次滑动的时候生成的组件
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewContainter.get(position));
            return viewContainter.get(position);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }


    // 定义一个消息处理handler
    private Handler mHandler = new Handler() {

        @SuppressLint("WrongConstant")
        @Override
        public void handleMessage(Message msg) {
            //
        }
    };

    /**
     * 加载省、市、区级联数据
     */
    private void initJsonData() {   //解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(activity, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    /**
     * 加载广告牌类型级联数据
     */
    private void initAdTypeJsonData() {   //解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(activity, "ad_types_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        optionsType1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            optionsType2Items.add(CityList);
            /**
             * 添加地区数据
             */
            optionsType3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

}
