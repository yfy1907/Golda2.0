package com.example.administrator.goldaapp.common;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * <pre>将图片设置为ImageView的背景</pre>
 */
@SuppressLint("NewApi")
public class BgImageViewAware extends ImageViewAware {

    private ImageView imageViewRef;

    public BgImageViewAware(ImageView imageView) {
        this(imageView, true);
        this.imageViewRef = imageView;
    }

    public BgImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        super(imageView, checkActualViewSize);
        this.imageViewRef = imageView;
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        //重写父类方法，将图片设为背景
        if(Looper.myLooper() == Looper.getMainLooper()) {
            ImageView imageView = (ImageView)this.imageViewRef;
//            ImageView imageView = (ImageView)this.imageViewRef.get();
            if(imageView != null) {
                imageView.setBackground(new BitmapDrawable(bitmap));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setImageDrawable(Drawable drawable) {
        return super.setImageDrawable(drawable);
    }
}