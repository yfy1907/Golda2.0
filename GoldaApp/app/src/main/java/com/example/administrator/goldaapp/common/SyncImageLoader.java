package com.example.administrator.goldaapp.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.example.administrator.goldaapp.utils.AssistUtil;
import com.example.administrator.goldaapp.utils.HttpTools;
import com.example.administrator.goldaapp.utils.MD5Util;
import com.example.administrator.goldaapp.utils.StringUtil;

public class SyncImageLoader {

    private Object lock = new Object();

    private boolean mAllowLoad = true;

    private boolean firstLoad = true;

    private int mStartLoadLimit = 0;

    private int mStopLoadLimit = 0;

    final Handler handler = new Handler();

    private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

    public interface OnImageLoadListener {
        public void onImageLoad(Integer t, Drawable drawable);
        public void onError(Integer t);
    }

    public void setLoadLimit(int startLoadLimit,int stopLoadLimit){
        if(startLoadLimit > stopLoadLimit){
            return;
        }
        mStartLoadLimit = startLoadLimit;
        mStopLoadLimit = stopLoadLimit;
    }

    public void restore(){
        mAllowLoad = true;
        firstLoad = true;
    }

    public void lock(){
        mAllowLoad = false;
        firstLoad = false;
    }

    public void unlock(){
        mAllowLoad = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void loadImage(Integer t, String imageUrl, OnImageLoadListener listener) {

        // MyLogger.Log().d("imageUrl="+imageUrl);
        final OnImageLoadListener mListener = listener;
        final String mImageUrl = imageUrl;
        final Integer mt = t;

        new Thread(new Runnable() {

            @Override
            public void run() {
                if(!mAllowLoad){
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                if(mAllowLoad && firstLoad){
                    loadImage(mImageUrl, mt, mListener);
                }
                if(mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit){
                    loadImage(mImageUrl, mt, mListener);
                }
            }

        }).start();
    }

    private void loadImage(final String mImageUrl,final Integer mt,final OnImageLoadListener mListener){

        if (imageCache.containsKey(mImageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(mImageUrl);
            final Drawable d = softReference.get();
            if (d != null) {
                //MyLogger.Log().w("111缓存中存在图片:"+mImageUrl+";=-=-=-=-=-=-="+mt);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mAllowLoad){
                            mListener.onImageLoad(mt, d);
                        }
                    }
                });
                return;
            }
        }
        try {
            final Drawable d = loadImageFromUrl(mImageUrl);
            if(d != null){
                imageCache.put(mImageUrl, new SoftReference<Drawable>(d));
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(mAllowLoad){
                        mListener.onImageLoad(mt, d);
                    }
                }
            });
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onError(mt);
                }
            });
            e.printStackTrace();
        }
    }

    public static Drawable loadImageFromUrl(String url) throws IOException {

        if(StringUtil.isEmpty(url)){
            return null;
        }
        //Log.i("","###url=="+url);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String imagePath = AssistUtil.getMemoryPath() + "uploadImage/"+ MD5Util.MD5(url);
            MyLogger.Log().e("md5url=="+imagePath);
            File f = new File(imagePath);
            if(f.exists()){
                FileInputStream fis = new FileInputStream(f);
                Drawable d = Drawable.createFromStream(fis, "src");
                fis.close();

                //MyLogger.Log().w("222缓存中存在图片:"+url);
                return d;
            }

            Bitmap bitmap = HttpTools.getUrlImage(url);
            if(null != bitmap){
                //MyLogger.Log().i("##333 在线加载图片："+url);
                Drawable d = new BitmapDrawable(bitmap);
                return d;
            }else{
                return null;
            }
//            return loadImageFromUrl(url);
        }else{
            //MyLogger.Log().i("##444 在线加载图片："+url);
            URL m = new URL(url);
            InputStream i = (InputStream) m.getContent();
            Drawable d = Drawable.createFromStream(i, "src");
            return d;
        }

    }
}